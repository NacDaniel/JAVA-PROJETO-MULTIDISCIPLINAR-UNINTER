package com.danielnac.multidisciplinar.repository;

import com.danielnac.multidisciplinar.model.Estoque;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class EstoqueRepository extends BaseRepository {

    public Estoque obterPorProdutoEUnidade(Integer produtoId, Integer unidadeId) {
        String sql = "SELECT * FROM estoque WHERE produto_id = :produtoId AND unidade_id = :unidadeId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("produtoId", produtoId)
                .addValue("unidadeId", unidadeId);
        return getJdbcTemplate().query(sql, params, rs -> {
            if (rs.next()) return mapear(rs);
            return null;
        });
    }

    public void incluir(Estoque estoque) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO estoque (produto_id, unidade_id, quantidade, data_atualizacao) ");
        sql.append("VALUES (:produtoId, :unidadeId, :quantidade, NOW())");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("produtoId", estoque.getProdutoId())
                .addValue("unidadeId", estoque.getUnidadeId())
                .addValue("quantidade", estoque.getQuantidade());
        getJdbcTemplate().update(sql.toString(), params);
    }

    public void atualizarQuantidade(Integer produtoId, Integer unidadeId, Integer quantidade) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE estoque SET quantidade = :quantidade, data_atualizacao = NOW() ");
        sql.append("WHERE produto_id = :produtoId AND unidade_id = :unidadeId");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("quantidade", quantidade)
                .addValue("produtoId", produtoId)
                .addValue("unidadeId", unidadeId);
        getJdbcTemplate().update(sql.toString(), params);
    }

    public void decrementar(Integer produtoId, Integer unidadeId, Integer quantidade) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE estoque SET quantidade = quantidade - :quantidade, data_atualizacao = NOW() ");
        sql.append("WHERE produto_id = :produtoId AND unidade_id = :unidadeId");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("quantidade", quantidade)
                .addValue("produtoId", produtoId)
                .addValue("unidadeId", unidadeId);
        getJdbcTemplate().update(sql.toString(), params);
    }

    public void incrementar(Integer produtoId, Integer unidadeId, Integer quantidade) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE estoque SET quantidade = quantidade + :quantidade, data_atualizacao = NOW() ");
        sql.append("WHERE produto_id = :produtoId AND unidade_id = :unidadeId");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("quantidade", quantidade)
                .addValue("produtoId", produtoId)
                .addValue("unidadeId", unidadeId);
        getJdbcTemplate().update(sql.toString(), params);
    }

    private Estoque mapear(ResultSet rs) throws SQLException {
        Estoque e = new Estoque();
        e.setId(rs.getInt("id"));
        e.setProdutoId(rs.getInt("produto_id"));
        e.setUnidadeId(rs.getInt("unidade_id"));
        e.setQuantidade(rs.getInt("quantidade"));
        java.sql.Timestamp ts = rs.getTimestamp("data_atualizacao");
        e.setDataAtualizacao(ts != null ? ts.toLocalDateTime() : null);
        return e;
    }
}
