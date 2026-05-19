package com.danielnac.multidisciplinar.service;

import com.danielnac.multidisciplinar.dto.ItemPedidoRequest;
import com.danielnac.multidisciplinar.dto.PedidoRequest;
import com.danielnac.multidisciplinar.exception.BadRequestException;
import com.danielnac.multidisciplinar.exception.NotFoundException;
import com.danielnac.multidisciplinar.model.Estoque;
import com.danielnac.multidisciplinar.model.ItemPedido;
import com.danielnac.multidisciplinar.model.Pedido;
import com.danielnac.multidisciplinar.model.Produto;
import com.danielnac.multidisciplinar.repository.EstoqueRepository;
import com.danielnac.multidisciplinar.repository.ItemPedidoRepository;
import com.danielnac.multidisciplinar.repository.PedidoRepository;
import com.danielnac.multidisciplinar.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    public Integer criar(PedidoRequest request) {
        validarRequest(request);

        BigDecimal valorTotal = calcularTotal(request);

        Pedido pedido = new Pedido();
        pedido.setClienteId(request.clienteId());
        pedido.setUnidadeId(request.unidadeId());
        pedido.setCanalPedido(request.canalPedido());
        pedido.setValorTotal(valorTotal);

        Integer pedidoId = pedidoRepository.incluir(pedido);

        for (ItemPedidoRequest item : request.itens()) {
            Produto produto = produtoRepository.obterPorId(item.produtoId());

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setPedidoId(pedidoId);
            itemPedido.setProdutoId(item.produtoId());
            itemPedido.setQuantidade(item.quantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());
            itemPedidoRepository.incluir(itemPedido);

            estoqueRepository.decrementar(item.produtoId(), request.unidadeId(), item.quantidade());
        }

        return pedidoId;
    }

    public Pedido obterPorId(Integer id) {
        Pedido pedido = pedidoRepository.obterPorId(id);
        if (pedido == null) {
            throw new NotFoundException("Pedido não encontrado.");
        }
        return pedido;
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
                throw new BadRequestException("Estoque insuficiente para o produto: " + produto.getNome());
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
