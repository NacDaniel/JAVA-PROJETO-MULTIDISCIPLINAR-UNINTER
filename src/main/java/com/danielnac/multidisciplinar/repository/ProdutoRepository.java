package com.danielnac.multidisciplinar.repository;

import com.danielnac.multidisciplinar.model.Produto;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProdutoRepository extends BaseRepository {

    public List<Produto> listar() {
        String sql = "SELECT * FROM produtos ORDER BY nome";
        return getJdbcTemplate().query(sql, new MapSqlParameterSource(), (rs, rowNum) -> mapear(rs));
    }

    public List<Produto> listarAtivos() {
        String sql = "SELECT * FROM produtos WHERE ativo = true ORDER BY nome";
        return getJdbcTemplate().query(sql, new MapSqlParameterSource(), (rs, rowNum) -> mapear(rs));
    }

    public Produto obterPorId(Integer id) {
        String sql = "SELECT * FROM produtos WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return getJdbcTemplate().query(sql, params, rs -> {
            if (rs.next()) return mapear(rs);
            return null;
        });
    }

    public void incluir(Produto produto) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO produtos (nome, descricao, preco, categoria, ativo) ");
        sql.append("VALUES (:nome, :descricao, :preco, :categoria, :ativo)");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nome", produto.getNome())
                .addValue("descricao", produto.getDescricao())
                .addValue("preco", produto.getPreco())
                .addValue("categoria", produto.getCategoria())
                .addValue("ativo", produto.getAtivo());
        getJdbcTemplate().update(sql.toString(), params);
    }

    public void atualizar(Produto produto) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE produtos SET nome = :nome, descricao = :descricao, preco = :preco, ");
        sql.append("categoria = :categoria, ativo = :ativo WHERE id = :id");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nome", produto.getNome())
                .addValue("descricao", produto.getDescricao())
                .addValue("preco", produto.getPreco())
                .addValue("categoria", produto.getCategoria())
                .addValue("ativo", produto.getAtivo())
                .addValue("id", produto.getId());
        getJdbcTemplate().update(sql.toString(), params);
    }

    private Produto mapear(ResultSet rs) throws SQLException {
        Produto p = new Produto();
        p.setId(rs.getInt("id"));
        p.setNome(rs.getString("nome"));
        p.setDescricao(rs.getString("descricao"));
        p.setPreco(rs.getBigDecimal("preco"));
        p.setCategoria(rs.getString("categoria"));
        p.setAtivo(rs.getBoolean("ativo"));
        return p;
    }
}
