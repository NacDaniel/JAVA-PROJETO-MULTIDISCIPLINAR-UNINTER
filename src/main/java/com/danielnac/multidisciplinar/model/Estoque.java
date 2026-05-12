package com.danielnac.multidisciplinar.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Estoque implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer produtoId;
    private Integer unidadeId;
    private Integer quantidade;
    private LocalDateTime dataAtualizacao;

    public Estoque() {}

    public Estoque(Integer id, Integer produtoId, Integer unidadeId, Integer quantidade, LocalDateTime dataAtualizacao) {
        this.id = id;
        this.produtoId = produtoId;
        this.unidadeId = unidadeId;
        this.quantidade = quantidade;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getProdutoId() { return produtoId; }
    public void setProdutoId(Integer produtoId) { this.produtoId = produtoId; }

    public Integer getUnidadeId() { return unidadeId; }
    public void setUnidadeId(Integer unidadeId) { this.unidadeId = unidadeId; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
