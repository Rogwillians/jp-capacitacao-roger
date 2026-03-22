package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
    Optional<Produto> findBySku(String sku);
    List<Produto> findByAtivoTrue();
    List<Produto> findByCategoriaId(UUID categoriaId);
    List<Produto> findByNomeContainingIgnoreCase(String nome);
    List<Produto> findByQuantidadeEstoqueLessThanEqual(Integer quantidade);
    List<Produto> findByPrecoBetween(BigDecimal precoMin, BigDecimal precoMax);

}
