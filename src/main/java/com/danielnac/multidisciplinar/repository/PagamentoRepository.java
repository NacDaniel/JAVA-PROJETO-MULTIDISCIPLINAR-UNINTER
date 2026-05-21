package com.danielnac.multidisciplinar.repository;

import com.danielnac.multidisciplinar.enums.FormasPagamento;
import com.danielnac.multidisciplinar.enums.StatusPagamento;
import com.danielnac.multidisciplinar.model.Pagamento;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PagamentoRepository extends BaseRepository {

    public List<Pagamento> listarPorPedido(Integer pedidoId) {
        String sql = "SELECT * FROM pagamentos WHERE pedido_id = :pedidoId ORDER BY data_criacao DESC";
        MapSqlParameterSource params = new MapSqlParameterSource("pedidoId", pedidoId);
        return getJdbcTemplate().query(sql, params, (rs, rowNum) -> mapear(rs));
    }

    public Pagamento obterPorId(Integer id) {
        String sql = "SELECT * FROM pagamentos WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return getJdbcTemplate().query(sql, params, rs -> {
            if (rs.next()) return mapear(rs);
            return null;
        });
    }

    public Integer incluir(Pagamento pagamento) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO pagamentos (pedido_id, forma_pagamento, status, valor, data_criacao) ");
        sql.append("VALUES (:pedidoId, :formaPagamento, :status, :valor, NOW())");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("pedidoId", pagamento.getPedidoId())
                .addValue("formaPagamento", pagamento.getFormaPagamento().name())
                .addValue("status", pagamento.getStatus().name())
                .addValue("valor", pagamento.getValor());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(sql.toString(), params, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void atualizarStatus(Integer id, StatusPagamento status) {
        String sql = "UPDATE pagamentos SET status = :status WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("status", status.name())
                .addValue("id", id);
        getJdbcTemplate().update(sql, params);
    }

    private Pagamento mapear(ResultSet rs) throws SQLException {
        Pagamento p = new Pagamento();
        p.setId(rs.getInt("id"));
        p.setPedidoId(rs.getInt("pedido_id"));
        p.setFormaPagamento(FormasPagamento.valueOf(rs.getString("forma_pagamento")));
        p.setStatus(StatusPagamento.valueOf(rs.getString("status")));
        p.setValor(rs.getBigDecimal("valor"));
        p.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        return p;
    }
}
