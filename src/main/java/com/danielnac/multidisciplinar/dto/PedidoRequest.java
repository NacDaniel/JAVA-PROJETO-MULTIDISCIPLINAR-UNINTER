package com.danielnac.multidisciplinar.dto;

import com.danielnac.multidisciplinar.enums.CanaisAtendimento;

import java.util.List;

public record PedidoRequest(Integer clienteId, Integer unidadeId, CanaisAtendimento canalPedido, List<ItemPedidoRequest> itens) {}
