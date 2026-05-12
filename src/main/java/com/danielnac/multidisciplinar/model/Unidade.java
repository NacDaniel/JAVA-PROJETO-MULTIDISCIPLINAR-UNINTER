package com.danielnac.multidisciplinar.model;

import java.io.Serializable;

public class Unidade implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String endereco;
    private String cidade;
    private String uf;
    private Boolean ativo;

    public Unidade() {}

    public Unidade(Integer id, String nome, String endereco, String cidade, String uf, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.cidade = cidade;
        this.uf = uf;
        this.ativo = ativo;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
