package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, UUID> {
    List<Avaliacao> findByProdutoIdOrderByDataCriacaoDesc(UUID produtoId);
    boolean existsByPedidoIdAndProdutoId(UUID pedidoId, UUID produtoId);
    @Query("SELECT COALESCE(AVG(a.nota), 0.0) FROM Avaliacao a WHERE a.produtoId = :produtoId")
    Double findAverageNotaByProdutoId( UUID produtoId);
}
