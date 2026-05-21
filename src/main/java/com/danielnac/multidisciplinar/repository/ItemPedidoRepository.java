package com.danielnac.multidisciplinar.repository;

import com.danielnac.multidisciplinar.model.ItemPedido;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemPedidoRepository extends BaseRepository {

    public List<ItemPedido> listarPorPedido(Integer pedidoId) {
        String sql = "SELECT * FROM itens_pedido WHERE pedido_id = :pedidoId";
        MapSqlParameterSource params = new MapSqlParameterSource("pedidoId", pedidoId);
        return getJdbcTemplate().query(sql, params, (rs, rowNum) -> {
            ItemPedido item = new ItemPedido();
            item.setId(rs.getInt("id"));
            item.setPedidoId(rs.getInt("pedido_id"));
            item.setProdutoId(rs.getInt("produto_id"));
            item.setQuantidade(rs.getInt("quantidade"));
            item.setPrecoUnitario(rs.getBigDecimal("preco_unitario"));
            return item;
        });
    }

    public void incluir(ItemPedido item) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO itens_pedido (pedido_id, produto_id, quantidade, preco_unitario) ");
        sql.append("VALUES (:pedidoId, :produtoId, :quantidade, :precoUnitario)");
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("pedidoId", item.getPedidoId())
                .addValue("produtoId", item.getProdutoId())
                .addValue("quantidade", item.getQuantidade())
                .addValue("precoUnitario", item.getPrecoUnitario());
        getJdbcTemplate().update(sql.toString(), params);
    }
}
