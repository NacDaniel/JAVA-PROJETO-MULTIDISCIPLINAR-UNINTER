package com.danielnac.multidisciplinar.controller;

import com.danielnac.multidisciplinar.dto.AtualizarStatusRequest;
import com.danielnac.multidisciplinar.dto.PedidoRequest;
import com.danielnac.multidisciplinar.dto.PedidoResponse;
import com.danielnac.multidisciplinar.enums.CanaisAtendimento;
import com.danielnac.multidisciplinar.enums.StatusPedido;
import com.danielnac.multidisciplinar.model.Pedido;
import com.danielnac.multidisciplinar.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class pedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> criar(@RequestBody PedidoRequest request) {
        return ResponseEntity.status(201).body(pedidoService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listar(
            @RequestParam(required = false) Integer unidadeId,
            @RequestParam(required = false) Integer clienteId,
            @RequestParam(required = false) CanaisAtendimento canalPedido,
            @RequestParam(required = false) StatusPedido status) {
        return ResponseEntity.ok(pedidoService.listar(unidadeId, clienteId, canalPedido, status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obterPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.obterPorId(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Integer id,
            @RequestBody AtualizarStatusRequest request) {
        pedidoService.atualizarStatus(id, request.status());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        pedidoService.cancelar(id);
        return ResponseEntity.noContent().build();
    }
}
