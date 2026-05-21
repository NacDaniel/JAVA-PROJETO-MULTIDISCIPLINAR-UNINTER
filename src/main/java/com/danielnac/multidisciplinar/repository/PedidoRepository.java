package com.danielnac.multidisciplinar.repository;

import com.danielnac.multidisciplinar.enums.CanaisAtendimento;
import com.danielnac.multidisciplinar.enums.StatusPedido;
import com.danielnac.multidisciplinar.model.Pedido;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PedidoRepository extends BaseRepository {

    public Pedido obterPorId(Integer id) {
        String sql = "SELECT * FROM pedidos WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return getJdbcTemplate().query(sql, params, rs -> {
            if (rs.next()) return mapear(rs);
            return null;
        });
    }

    public List<Pedido> listar(Integer unidadeId, Integer clienteId, CanaisAtendimento canal, StatusPedido status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM pedidos WHERE 1=1");
        MapSqlParameterSource params = new MapSqlParameterSource();

        if (unidadeId != null) {
            sql.append(" AND unidade_id = :unidadeId");
            params.addValue("unidadeId", unidadeId);
        }
        if (clienteId != null) {
            sql.append(" AND cliente_id = :clienteId");
            params.addValue("clienteId", clienteId);
        }
        if (canal != null) {
            sql.append(" AND canal_pedido = :canal");
            params.addValue("canal", canal.name());
        }
        if (status != null) {
            sql.append(" AND status = :status");
            params.addValue("status", status.name());
        }

        sql.append(" ORDER BY data_criacao DESC");
        return getJdbcTemplate().query(sql.toString(), params, (rs, rowNum) -> mapear(rs));
    }

    public Integer incluir(Pedido pedido) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO pedidos (cliente_id, unidade_id, canal_pedido, status, valor_total, data_criacao, data_atualizacao) ");
        sql.append("VALUES (:clienteId, :unidadeId, :canalPedido, :status, :valorTotal, NOW(), NOW())");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("clienteId", pedido.getClienteId())
                .addValue("unidadeId", pedido.getUnidadeId())
                .addValue("canalPedido", pedido.getCanalPedido().name())
                .addValue("status", StatusPedido.AGUARDANDO_PAGAMENTO.name())
                .addValue("valorTotal", pedido.getValorTotal());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(sql.toString(), params, keyHolder);
        return keyHolder.getKey().intValue();
    }

    public void atualizarStatus(Integer id, StatusPedido status) {
        String sql = "UPDATE pedidos SET status = :status, data_atualizacao = NOW() WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("status", status.name())
                .addValue("id", id);
        getJdbcTemplate().update(sql, params);
    }

    private Pedido mapear(ResultSet rs) throws SQLException {
        Pedido p = new Pedido();
        p.setId(rs.getInt("id"));
        p.setClienteId(rs.getInt("cliente_id"));
        p.setUnidadeId(rs.getInt("unidade_id"));
        p.setCanalPedido(CanaisAtendimento.valueOf(rs.getString("canal_pedido")));
        p.setStatus(StatusPedido.valueOf(rs.getString("status")));
        p.setValorTotal(rs.getBigDecimal("valor_total"));
        p.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());
        p.setDataAtualizacao(rs.getTimestamp("data_atualizacao").toLocalDateTime());
        return p;
    }
}
