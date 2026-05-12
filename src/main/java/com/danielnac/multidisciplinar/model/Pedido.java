package com.danielnac.multidisciplinar.model;

import com.danielnac.multidisciplinar.enums.CanaisAtendimento;
import com.danielnac.multidisciplinar.enums.StatusPedido;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer clienteId;
    private Integer unidadeId;
    private CanaisAtendimento canalPedido;
    private StatusPedido status;
    private BigDecimal valorTotal;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Pedido() {}

    public Pedido(Integer id, Integer clienteId, Integer unidadeId, CanaisAtendimento canalPedido,
                  StatusPedido status, BigDecimal valorTotal, LocalDateTime dataCriacao, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.clienteId = clienteId;
        this.unidadeId = unidadeId;
        this.canalPedido = canalPedido;
        this.status = status;
        this.valorTotal = valorTotal;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }

    public Integer getUnidadeId() { return unidadeId; }
    public void setUnidadeId(Integer unidadeId) { this.unidadeId = unidadeId; }

    public CanaisAtendimento getCanalPedido() { return canalPedido; }
    public void setCanalPedido(CanaisAtendimento canalPedido) { this.canalPedido = canalPedido; }

    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }

    public BigDecimal getValorTotal() { return valorTotal; }
    public void setValorTotal(BigDecimal valorTotal) { this.valorTotal = valorTotal; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
