package com.danielnac.multidisciplinar.repository;

import com.danielnac.multidisciplinar.model.Cliente;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ClienteRepository extends BaseRepository {

    public Cliente obterPorId(Integer id) {
        String sql = "SELECT * FROM clientes WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return getJdbcTemplate().query(sql, params, rs -> {
            if (rs.next()) return mapear(rs);
            return null;
        });
    }

    public Cliente obterPorEmail(String email) {
        String sql = "SELECT * FROM clientes WHERE email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource("email", email);
        return getJdbcTemplate().query(sql, params, rs -> {
            if (rs.next()) return mapear(rs);
            return null;
        });
    }

    public List<Cliente> listar() {
        String sql = "SELECT * FROM clientes ORDER BY nome";
        return getJdbcTemplate().query(sql, new MapSqlParameterSource(), (rs, rowNum) -> mapear(rs));
    }

    public void incluir(Cliente cliente) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO clientes (nome, email, telefone, cpf, aceite_lgpd, data_consentimento_lgpd, data_criacao) ");
        sql.append("VALUES (:nome, :email, :telefone, :cpf, :aceiteLgpd, :dataConsentimentoLgpd, NOW())");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nome", cliente.getNome())
                .addValue("email", cliente.getEmail())
                .addValue("telefone", cliente.getTelefone())
                .addValue("cpf", cliente.getCpf())
                .addValue("aceiteLgpd", cliente.getAceiteLgpd())
                .addValue("dataConsentimentoLgpd", cliente.getDataConsentimentoLgpd());
        getJdbcTemplate().update(sql.toString(), params);
    }

    private Cliente mapear(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setId(rs.getInt("id"));
        c.setNome(rs.getString("nome"));
        c.setEmail(rs.getString("email"));
        c.setTelefone(rs.getString("telefone"));
        c.setCpf(rs.getString("cpf"));
        c.setAceiteLgpd(rs.getBoolean("aceite_lgpd"));
        var consentimento = rs.getTimestamp("data_consentimento_lgpd");
        if (consentimento != null) c.setDataConsentimentoLgpd(consentimento.toLocalDateTime());
        c.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        return c;
    }
}
