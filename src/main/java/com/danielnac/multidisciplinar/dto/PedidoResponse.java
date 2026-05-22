package com.danielnac.multidisciplinar.dto;

import com.danielnac.multidisciplinar.enums.StatusPedido;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Integer pedidoId,
        StatusPedido status,
        BigDecimal total,
        List<ItemResponse> itens,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        LocalDateTime createdAt
) {
    public record ItemResponse(Integer produtoId, Integer quantidade, BigDecimal precoUnitario) {}
}
