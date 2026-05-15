package com.danielnac.multidisciplinar.dto;

import com.danielnac.multidisciplinar.enums.Cargo;

public record RegistroRequest(String nome, String email, String senha, Cargo cargo, Integer unidadeId) {}
