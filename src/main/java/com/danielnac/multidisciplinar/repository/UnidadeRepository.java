package com.danielnac.multidisciplinar.repository;

import com.danielnac.multidisciplinar.model.Unidade;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UnidadeRepository extends BaseRepository {

    public List<Unidade> listar() {
        String sql = "SELECT * FROM unidades ORDER BY nome";
        return getJdbcTemplate().query(sql, new MapSqlParameterSource(), (rs, rowNum) -> {
            Unidade u = new Unidade();
            u.setId(rs.getInt("id"));
            u.setNome(rs.getString("nome"));
            u.setEndereco(rs.getString("endereco"));
            u.setCidade(rs.getString("cidade"));
            u.setUf(rs.getString("uf"));
            u.setAtivo(rs.getBoolean("ativo"));
            return u;
        });
    }

    public Unidade obterPorId(Integer id) {
        String sql = "SELECT * FROM unidades WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return getJdbcTemplate().query(sql, params, rs -> {
            if (rs.next()) {
                Unidade u = new Unidade();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEndereco(rs.getString("endereco"));
                u.setCidade(rs.getString("cidade"));
                u.setUf(rs.getString("uf"));
                u.setAtivo(rs.getBoolean("ativo"));
                return u;
            }
            return null;
        });
    }

    public void incluir(Unidade unidade) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO unidades (nome, endereco, cidade, uf, ativo) ");
        sql.append("VALUES (:nome, :endereco, :cidade, :uf, :ativo)");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nome", unidade.getNome())
                .addValue("endereco", unidade.getEndereco())
                .addValue("cidade", unidade.getCidade())
                .addValue("uf", unidade.getUf())
                .addValue("ativo", unidade.getAtivo());
        getJdbcTemplate().update(sql.toString(), params);
    }
}
