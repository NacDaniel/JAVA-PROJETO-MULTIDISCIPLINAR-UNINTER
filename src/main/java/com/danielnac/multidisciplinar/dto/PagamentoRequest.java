package com.danielnac.multidisciplinar.dto;

import com.danielnac.multidisciplinar.enums.FormasPagamento;

public record PagamentoRequest(Integer pedidoId, FormasPagamento formaPagamento, Boolean simularFalha) {}
