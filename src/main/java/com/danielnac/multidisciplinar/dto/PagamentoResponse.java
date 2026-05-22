package com.danielnac.multidisciplinar.dto;

import com.danielnac.multidisciplinar.enums.FormasPagamento;
import com.danielnac.multidisciplinar.enums.StatusPagamento;
import com.danielnac.multidisciplinar.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PagamentoResponse(
        Integer pagamentoId,
        Integer pedidoId,
        FormasPagamento formaPagamento,
        StatusPagamento statusPagamento,
        StatusPedido statusPedido,
        BigDecimal valor,
        String mensagem
) {}
