package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.TransacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransacaoEstoqueRepository extends JpaRepository<TransacaoEstoque, UUID> {

    List<TransacaoEstoque> findByProdutoIdOrderByDataCriacaoDesc(UUID produtoId);
}
