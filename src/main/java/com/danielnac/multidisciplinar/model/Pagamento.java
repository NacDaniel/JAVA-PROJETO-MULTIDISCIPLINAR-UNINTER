package com.danielnac.multidisciplinar.model;

import com.danielnac.multidisciplinar.enums.FormasPagamento;
import com.danielnac.multidisciplinar.enums.StatusPagamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer pedidoId;
    private FormasPagamento formaPagamento;
    private StatusPagamento status;
    private BigDecimal valor;
    private LocalDateTime dataCriacao;

    public Pagamento() {}

    public Pagamento(Integer id, Integer pedidoId, FormasPagamento formaPagamento,
                     StatusPagamento status, BigDecimal valor, LocalDateTime dataCriacao) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.formaPagamento = formaPagamento;
        this.status = status;
        this.valor = valor;
        this.dataCriacao = dataCriacao;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }

    public FormasPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormasPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}
