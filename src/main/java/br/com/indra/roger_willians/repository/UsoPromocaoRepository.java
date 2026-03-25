package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.UsoPromocao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsoPromocaoRepository extends JpaRepository<UsoPromocao, UUID> {
    boolean existsByPromocaoIdAndUsuarioId(UUID promocaoId, UUID usuarioId);
}