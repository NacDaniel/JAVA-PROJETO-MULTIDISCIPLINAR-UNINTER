package com.danielnac.multidisciplinar.service;

import com.danielnac.multidisciplinar.dto.ItemPedidoRequest;
import com.danielnac.multidisciplinar.dto.PedidoRequest;
import com.danielnac.multidisciplinar.dto.PedidoResponse;
import com.danielnac.multidisciplinar.enums.CanaisAtendimento;
import com.danielnac.multidisciplinar.enums.StatusPedido;
import com.danielnac.multidisciplinar.exception.BadRequestException;
import com.danielnac.multidisciplinar.exception.ConflictException;
import com.danielnac.multidisciplinar.exception.ForbiddenException;
import com.danielnac.multidisciplinar.exception.NotFoundException;
import com.danielnac.multidisciplinar.model.Estoque;
import com.danielnac.multidisciplinar.model.ItemPedido;
import com.danielnac.multidisciplinar.model.Pedido;
import com.danielnac.multidisciplinar.model.Produto;
import com.danielnac.multidisciplinar.repository.EstoqueRepository;
import com.danielnac.multidisciplinar.repository.ItemPedidoRepository;
import com.danielnac.multidisciplinar.repository.PedidoRepository;
import com.danielnac.multidisciplinar.repository.ProdutoRepository;
import com.danielnac.multidisciplinar.support.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    public PedidoResponse criar(PedidoRequest request) {
        validarRequest(request);

        BigDecimal valorTotal = calcularTotal(request);

        Pedido pedido = new Pedido();
        pedido.setClienteId(request.clienteId());
        pedido.setUnidadeId(request.unidadeId());
        pedido.setCanalPedido(request.canalPedido());
        pedido.setValorTotal(valorTotal);

        Integer pedidoId = pedidoRepository.incluir(pedido);

        List<PedidoResponse.ItemResponse> itensResponse = new ArrayList<>();
        for (ItemPedidoRequest item : request.itens()) {
            Produto produto = produtoRepository.obterPorId(item.produtoId());

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedidoId(pedidoId);
            itemPedido.setProdutoId(item.produtoId());
            itemPedido.setQuantidade(item.quantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());
            itemPedidoRepository.incluir(itemPedido);

            estoqueRepository.decrementar(item.produtoId(), request.unidadeId(), item.quantidade());
            itensResponse.add(new PedidoResponse.ItemResponse(item.produtoId(), item.quantidade(), produto.getPreco()));
        }

        log.info("Pedido criado: id={} canal={} unidade={} total={} usuario={}",
                pedidoId, request.canalPedido(), request.unidadeId(), valorTotal, SessionUtil.getId());

        return new PedidoResponse(pedidoId, StatusPedido.AGUARDANDO_PAGAMENTO, valorTotal, itensResponse, LocalDateTime.now());
    }

    public Pedido obterPorId(Integer id) {
        Pedido pedido = pedidoRepository.obterPorId(id);
        if (pedido == null) {
            throw new NotFoundException("Pedido não encontrado.");
        }
        return pedido;
    }

    public List<Pedido> listar(Integer unidadeId, Integer clienteId, CanaisAtendimento canal, StatusPedido status) {
        if (unidadeId == null && clienteId == null && canal == null && status == null) {
            throw new BadRequestException("Informe ao menos um filtro: unidadeId, clienteId, canalPedido ou status.");
        }
        return pedidoRepository.listar(unidadeId, clienteId, canal, status);
    }

    public void atualizarStatus(Integer id, StatusPedido novoStatus) {
        Pedido pedido = obterPorId(id);

        String cargo = SessionUtil.getCargo();
        validarTransicaoStatus(pedido.getStatus(), novoStatus, cargo);

        pedidoRepository.atualizarStatus(id, novoStatus);

        log.info("Status do pedido atualizado: id={} de={} para={} usuario={} cargo={}",
                id, pedido.getStatus(), novoStatus, SessionUtil.getId(), cargo);
    }

    public void cancelar(Integer id) {
        Pedido pedido = obterPorId(id);

        String cargo = SessionUtil.getCargo();
        if (!"ATENDENTE".equals(cargo) && !"GERENTE".equals(cargo)) {
            throw new ForbiddenException("Apenas ATENDENTE ou GERENTE podem cancelar pedidos.");
        }

        if (StatusPedido.ENTREGUE.equals(pedido.getStatus()) || StatusPedido.CANCELADO.equals(pedido.getStatus())) {
            throw new BadRequestException("Pedido não pode ser cancelado. Status atual: " + pedido.getStatus());
        }

        List<ItemPedido> itens = itemPedidoRepository.listarPorPedido(id);
        for (ItemPedido item : itens) {
            estoqueRepository.incrementar(item.getProdutoId(), pedido.getUnidadeId(), item.getQuantidade());
        }

        pedidoRepository.atualizarStatus(id, StatusPedido.CANCELADO);

        log.info("Pedido cancelado: id={} usuario={} cargo={}", id, SessionUtil.getId(), cargo);
    }

    private void validarTransicaoStatus(StatusPedido atual, StatusPedido novo, String cargo) {
        boolean isCozinhaOuGerente = "COZINHA".equals(cargo) || "GERENTE".equals(cargo);
        boolean isAtendenteOuGerente = "ATENDENTE".equals(cargo) || "GERENTE".equals(cargo);

        if (StatusPedido.EM_PREPARACAO.equals(atual) && !isCozinhaOuGerente) {
            throw new ForbiddenException("Apenas COZINHA ou GERENTE podem avançar para EM_ENTREGA.");
        }
        if (StatusPedido.EM_ENTREGA.equals(atual) && !isAtendenteOuGerente) {
            throw new ForbiddenException("Apenas ATENDENTE ou GERENTE podem marcar como ENTREGUE.");
        }

        if (StatusPedido.CANCELADO.equals(atual) || StatusPedido.ENTREGUE.equals(atual)) {
            throw new BadRequestException("Não é possível alterar o status de um pedido " + atual + ".");
        }
        if (StatusPedido.CANCELADO.equals(novo)) {
            throw new BadRequestException("Use o endpoint de cancelamento para cancelar um pedido.");
        }

        if (StatusPedido.EM_PREPARACAO.equals(atual) && StatusPedido.EM_ENTREGA.equals(novo)) return;
        if (StatusPedido.EM_ENTREGA.equals(atual) && StatusPedido.ENTREGUE.equals(novo)) return;
        throw new BadRequestException("Transição de status inválida: " + atual + " → " + novo + ".");
    }

    private void validarRequest(PedidoRequest request) {
        if (request.unidadeId() == null) {
            throw new BadRequestException("O campo 'unidadeId' é obrigatório.");
        }
        if (request.canalPedido() == null) {
            throw new BadRequestException("O campo 'canalPedido' é obrigatório.");
        }
        if (request.itens() == null || request.itens().isEmpty()) {
            throw new BadRequestException("O pedido deve ter ao menos um item.");
        }

        for (ItemPedidoRequest item : request.itens()) {
            if (item.produtoId() == null || item.quantidade() == null || item.quantidade() <= 0) {
                throw new BadRequestException("Item inválido: produtoId e quantidade são obrigatórios.");
            }

            Produto produto = produtoRepository.obterPorId(item.produtoId());
            if (produto == null || !produto.getAtivo()) {
                throw new NotFoundException("Produto " + item.produtoId() + " não encontrado ou inativo.");
            }

            Estoque estoque = estoqueRepository.obterPorProdutoEUnidade(item.produtoId(), request.unidadeId());
            if (estoque == null || estoque.getQuantidade() < item.quantidade()) {
                throw new ConflictException("Estoque insuficiente para o produto: " + produto.getNome(), "ESTOQUE_INSUFICIENTE");
            }
        }
    }

    private BigDecimal calcularTotal(PedidoRequest request) {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemPedidoRequest item : request.itens()) {
            Produto produto = produtoRepository.obterPorId(item.produtoId());
            total = total.add(produto.getPreco().multiply(BigDecimal.valueOf(item.quantidade())));
        }
        return total;
    }
}
