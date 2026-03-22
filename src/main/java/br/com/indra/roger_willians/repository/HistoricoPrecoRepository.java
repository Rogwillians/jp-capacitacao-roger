package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.HistoricoPreco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoricoPrecoRepository extends JpaRepository<HistoricoPreco, UUID> {
     List<HistoricoPreco> findByProdutoIdOrderByDataAlteracaoDesc(UUID produtoId);

}
