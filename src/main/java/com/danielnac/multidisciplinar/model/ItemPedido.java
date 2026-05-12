package com.danielnac.multidisciplinar.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer pedidoId;
    private Integer produtoId;
    private Integer quantidade;
    private BigDecimal precoUnitario;

    public ItemPedido() {}

    public ItemPedido(Integer id, Integer pedidoId, Integer produtoId, Integer quantidade, BigDecimal precoUnitario) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.produtoId = produtoId;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getPedidoId() { return pedidoId; }
    public void setPedidoId(Integer pedidoId) { this.pedidoId = pedidoId; }

    public Integer getProdutoId() { return produtoId; }
    public void setProdutoId(Integer produtoId) { this.produtoId = produtoId; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }
}
