package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
    boolean existsByNome(String nome);
}
