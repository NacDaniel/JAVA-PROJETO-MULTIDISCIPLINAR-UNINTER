package com.danielnac.multidisciplinar.controller;

import com.danielnac.multidisciplinar.dto.PagamentoRequest;
import com.danielnac.multidisciplinar.dto.PagamentoResponse;
import com.danielnac.multidisciplinar.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class pagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping("/{pedidoId}")
    public ResponseEntity<PagamentoResponse> processar(
            @PathVariable Integer pedidoId,
            @RequestBody PagamentoRequest request) {

        PagamentoResponse response = pagamentoService.processar(pedidoId, request);
        return ResponseEntity.ok(response);
    }
}
