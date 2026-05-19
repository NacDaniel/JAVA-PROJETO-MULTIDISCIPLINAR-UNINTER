package com.danielnac.multidisciplinar.controller;

import com.danielnac.multidisciplinar.dto.PedidoRequest;
import com.danielnac.multidisciplinar.model.Pedido;
import com.danielnac.multidisciplinar.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class pedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Map<String, Integer>> criar(@RequestBody PedidoRequest request) {
        Integer id = pedidoService.criar(request);
        return ResponseEntity.status(201).body(Map.of("id", id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obterPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(pedidoService.obterPorId(id));
    }
}
