package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.Carrinho;
import br.com.indra.roger_willians.model.enums.StatusCarrinho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarrinhoRepository extends JpaRepository<Carrinho, UUID> {
    Optional<Carrinho> findByUsuarioIdAndStatus(UUID usuarioId, StatusCarrinho status);
}
