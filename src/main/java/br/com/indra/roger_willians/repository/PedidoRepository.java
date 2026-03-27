package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
    Optional<Pedido> findByIdAndUsuarioId(UUID id, UUID usuarioId);
}