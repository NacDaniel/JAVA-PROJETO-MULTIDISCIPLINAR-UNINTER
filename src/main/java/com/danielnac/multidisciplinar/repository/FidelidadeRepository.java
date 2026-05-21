package com.danielnac.multidisciplinar.repository;

import com.danielnac.multidisciplinar.model.FidelidadeCliente;
import com.danielnac.multidisciplinar.model.FidelidadeHistorico;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FidelidadeRepository extends BaseRepository {

    public FidelidadeCliente obterPorCliente(Integer clienteId) {
        String sql = "SELECT * FROM fidelidade_clientes WHERE cliente_id = :clienteId";
        MapSqlParameterSource params = new MapSqlParameterSource("clienteId", clienteId);
        return getJdbcTemplate().query(sql, params, rs -> {
            if (rs.next()) {
                FidelidadeCliente f = new FidelidadeCliente();
                f.setId(rs.getInt("id"));
                f.setClienteId(rs.getInt("cliente_id"));
                f.setPontos(rs.getInt("pontos"));
                f.setDataAtualizacao(rs.getTimestamp("data_atualizacao").toLocalDateTime());
                return f;
            }
            return null;
        });
    }

    public void incluirOuAtualizar(Integer clienteId, Integer pontosAdicionais) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO fidelidade_clientes (cliente_id, pontos, data_atualizacao) ");
        sql.append("VALUES (:clienteId, :pontos, NOW()) ");
        sql.append("ON DUPLICATE KEY UPDATE pontos = pontos + :pontos, data_atualizacao = NOW()");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("clienteId", clienteId)
                .addValue("pontos", pontosAdicionais);
        getJdbcTemplate().update(sql.toString(), params);
    }

    public List<FidelidadeHistorico> listarHistorico(Integer clienteId) {
        String sql = "SELECT * FROM fidelidade_historico WHERE cliente_id = :clienteId ORDER BY data DESC";
        MapSqlParameterSource params = new MapSqlParameterSource("clienteId", clienteId);
        return getJdbcTemplate().query(sql, params, (rs, rowNum) -> {
            FidelidadeHistorico h = new FidelidadeHistorico();
            h.setId(rs.getInt("id"));
            h.setClienteId(rs.getInt("cliente_id"));
            h.setPedidoId(rs.getInt("pedido_id"));
            h.setPontos(rs.getInt("pontos"));
            h.setDescricao(rs.getString("descricao"));
            h.setData(rs.getTimestamp("data").toLocalDateTime());
            return h;
        });
    }

    public void incluirHistorico(FidelidadeHistorico historico) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO fidelidade_historico (cliente_id, pedido_id, pontos, descricao, data) ");
        sql.append("VALUES (:clienteId, :pedidoId, :pontos, :descricao, NOW())");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("clienteId", historico.getClienteId())
                .addValue("pedidoId", historico.getPedidoId())
                .addValue("pontos", historico.getPontos())
                .addValue("descricao", historico.getDescricao());
        getJdbcTemplate().update(sql.toString(), params);
    }
}
