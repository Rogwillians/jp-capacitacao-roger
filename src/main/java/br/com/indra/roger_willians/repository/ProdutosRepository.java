package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.Produtos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, UUID> {
}
