package com.danielnac.multidisciplinar.dto;

import com.danielnac.multidisciplinar.enums.StatusPedido;

public record AtualizarStatusRequest(StatusPedido status) {}
