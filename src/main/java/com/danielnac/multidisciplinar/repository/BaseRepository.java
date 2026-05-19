package com.danielnac.multidisciplinar.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

public abstract class BaseRepository {

    private static final int QUANTIDADE_ITENS_PAGINA = 15;

    @Autowired
    @Qualifier("MySQL")
    private DataSource dataSource;

    protected NamedParameterJdbcTemplate getJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    protected String adicionarSqlPaginacao(Integer paginaSelecionada) {
        int pagina = (paginaSelecionada != null && paginaSelecionada > 0) ? paginaSelecionada : 1;
        int offset = (pagina - 1) * QUANTIDADE_ITENS_PAGINA;
        return " LIMIT " + QUANTIDADE_ITENS_PAGINA + " OFFSET " + offset;
    }
}
