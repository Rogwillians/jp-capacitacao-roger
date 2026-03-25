package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.Promocao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PromocaoRepository extends JpaRepository<Promocao, UUID> {
    Optional<Promocao> findByCodigo(String codigo);
}