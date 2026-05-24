package com.danielnac.multidisciplinar.service;

import com.danielnac.multidisciplinar.dto.PagamentoRequest;
import com.danielnac.multidisciplinar.dto.PagamentoResponse;
import com.danielnac.multidisciplinar.enums.StatusPagamento;
import com.danielnac.multidisciplinar.enums.StatusPedido;
import com.danielnac.multidisciplinar.exception.BadRequestException;
import com.danielnac.multidisciplinar.exception.NotFoundException;
import com.danielnac.multidisciplinar.model.Pagamento;
import com.danielnac.multidisciplinar.model.Pedido;
import com.danielnac.multidisciplinar.repository.PagamentoRepository;
import com.danielnac.multidisciplinar.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public PagamentoResponse processar(Integer pedidoId, PagamentoRequest request) {
        Pedido pedido = pedidoRepository.obterPorId(pedidoId);

        if (pedido == null) {
            throw new NotFoundException("Pedido não encontrado.");
        }

        if (!StatusPedido.AGUARDANDO_PAGAMENTO.equals(pedido.getStatus())) {
            throw new BadRequestException("Pedido não está aguardando pagamento. Status atual: " + pedido.getStatus());
        }

        if (request.formaPagamento() == null) {
            throw new BadRequestException("O campo 'formaPagamento' é obrigatório.");
        }

        boolean recusado = Boolean.TRUE.equals(request.simularFalha());
        StatusPagamento statusPagamento = recusado ? StatusPagamento.RECUSADO : StatusPagamento.APROVADO;

        Pagamento pagamento = new Pagamento();
        pagamento.setPedidoId(pedidoId);
        pagamento.setFormaPagamento(request.formaPagamento());
        pagamento.setValor(pedido.getValorTotal());
        pagamento.setStatus(statusPagamento);

        Integer pagamentoId = pagamentoRepository.incluir(pagamento);

        if (recusado) {
            return new PagamentoResponse(
                    pagamentoId,
                    pedidoId,
                    request.formaPagamento(),
                    StatusPagamento.RECUSADO,
                    pedido.getStatus(),
                    pedido.getValorTotal(),
                    "Pagamento recusado pelo gateway. O pedido permanece aguardando pagamento."
            );
        }

        pedidoRepository.atualizarStatus(pedidoId, StatusPedido.EM_PREPARACAO);

        return new PagamentoResponse(
                pagamentoId,
                pedidoId,
                request.formaPagamento(),
                StatusPagamento.APROVADO,
                StatusPedido.EM_PREPARACAO,
                pedido.getValorTotal(),
                null
        );
    }
}
