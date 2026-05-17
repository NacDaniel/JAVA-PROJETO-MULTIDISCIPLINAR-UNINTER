package com.danielnac.multidisciplinar.repository;

import com.danielnac.multidisciplinar.enums.Cargo;
import com.danielnac.multidisciplinar.model.Usuario;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsuarioRepository extends BaseRepository {

    public Usuario obterPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource("email", email);
        return getJdbcTemplate().query(sql, params, rs -> {
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setCargo(Cargo.valueOf(rs.getString("cargo")));
                u.setUnidadeId(rs.getInt("unidade_id"));
                u.setAtivo(rs.getBoolean("ativo"));
                u.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
                return u;
            }
            return null;
        });
    }

    public Usuario obterPorId(Integer id) {
        String sql = "SELECT * FROM usuarios WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return getJdbcTemplate().query(sql, params, rs -> {
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setEmail(rs.getString("email"));
                u.setSenha(rs.getString("senha"));
                u.setCargo(Cargo.valueOf(rs.getString("cargo")));
                u.setUnidadeId(rs.getInt("unidade_id"));
                u.setAtivo(rs.getBoolean("ativo"));
                u.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
                return u;
            }
            return null;
        });
    }

    public List<Usuario> listar() {
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        return getJdbcTemplate().query(sql, new MapSqlParameterSource(), (rs, rowNum) -> {
            Usuario u = new Usuario();
            u.setId(rs.getInt("id"));
            u.setNome(rs.getString("nome"));
            u.setEmail(rs.getString("email"));
            u.setCargo(Cargo.valueOf(rs.getString("cargo")));
            u.setUnidadeId(rs.getInt("unidade_id"));
            u.setAtivo(rs.getBoolean("ativo"));
            u.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
            return u;
        });
    }

    public void incluir(Usuario usuario) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO usuarios (nome, email, senha, cargo, unidade_id, ativo, data_criacao) ");
        sql.append("VALUES (:nome, :email, :senha, :cargo, :unidadeId, :ativo, NOW())");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nome", usuario.getNome())
                .addValue("email", usuario.getEmail())
                .addValue("senha", usuario.getSenha())
                .addValue("cargo", usuario.getCargo().name())
                .addValue("unidadeId", usuario.getUnidadeId())
                .addValue("ativo", usuario.getAtivo());
        getJdbcTemplate().update(sql.toString(), params);
    }

    public void atualizarSenha(Integer id, String senhaCriptografada) {
        String sql = "UPDATE usuarios SET senha = :senha WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("senha", senhaCriptografada)
                .addValue("id", id);
        getJdbcTemplate().update(sql, params);
    }
}
