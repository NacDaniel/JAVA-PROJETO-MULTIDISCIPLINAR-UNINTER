package com.danielnac.multidisciplinar.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private Boolean aceiteLgpd;
    private LocalDateTime dataConsentimentoLgpd;
    private LocalDateTime dataCriacao;

    public Cliente() {}

    public Cliente(Integer id, String nome, String email, String telefone, String cpf,
                   Boolean aceiteLgpd, LocalDateTime dataConsentimentoLgpd, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
        this.aceiteLgpd = aceiteLgpd;
        this.dataConsentimentoLgpd = dataConsentimentoLgpd;
        this.dataCriacao = dataCriacao;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public Boolean getAceiteLgpd() { return aceiteLgpd; }
    public void setAceiteLgpd(Boolean aceiteLgpd) { this.aceiteLgpd = aceiteLgpd; }

    public LocalDateTime getDataConsentimentoLgpd() { return dataConsentimentoLgpd; }
    public void setDataConsentimentoLgpd(LocalDateTime dataConsentimentoLgpd) { this.dataConsentimentoLgpd = dataConsentimentoLgpd; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}
