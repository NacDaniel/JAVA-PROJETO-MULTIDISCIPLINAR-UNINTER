package com.danielnac.multidisciplinar.dto;

import com.danielnac.multidisciplinar.enums.FormasPagamento;

public record PagamentoRequest(FormasPagamento formaPagamento, Boolean simularFalha) {}
