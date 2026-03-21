package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.HistoricoPreco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface HistoricoPrecoRepository extends JpaRepository<HistoricoPreco, UUID> {
     Set<HistoricoPreco> findByProdutosId(UUID produtoId);
}
