package com.danielnac.multidisciplinar.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FidelidadeHistorico implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer clienteId;
    private Integer pedidoId;
    private Integer pontos;
    private String descricao;
    private LocalDateTime data;

    public FidelidadeHistorico() {}

    public FidelidadeHistorico(Integer id, Integer clienteId, Integer pedidoId, Integer pontos, String descricao, LocalDateTime data) {
        this.id = id;
        this.clienteId = clienteId;
        this.pedidoId = pedidoId;
        this.pontos = pontos;
        this.descricao = descricao;
        this.data = data;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }

    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }

    public Integer getPontos() { return pontos; }
    public void setPontos(Integer pontos) { this.pontos = pontos; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }
}
