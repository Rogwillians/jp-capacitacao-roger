package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, UUID> {
    boolean existsByPedidoUsuarioIdAndPedidoIdAndProdutoId(UUID usuarioId, UUID pedidoId, UUID produtoId);
}