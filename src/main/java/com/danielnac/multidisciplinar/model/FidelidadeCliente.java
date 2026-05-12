package com.danielnac.multidisciplinar.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class FidelidadeCliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer clienteId;
    private Integer pontos;
    private LocalDateTime dataAtualizacao;

    public FidelidadeCliente() {}

    public FidelidadeCliente(Integer id, Integer clienteId, Integer pontos, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.clienteId = clienteId;
        this.pontos = pontos;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }

    public Integer getPontos() { return pontos; }
    public void setPontos(Integer pontos) { this.pontos = pontos; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
