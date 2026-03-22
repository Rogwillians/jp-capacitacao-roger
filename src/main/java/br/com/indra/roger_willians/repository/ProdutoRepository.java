package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query("SELECT p FROM Produto p WHERE p.quantidadeEstoque <= :estoqueMin AND p.ativo = true")
    List<Produto> buscarPoucoEstoque(@Param("estoqueMin") Integer quantidade);
    @Query("SELECT p FROM Produto p WHERE p.preco BETWEEN :precoMin AND :precoMax")
    List<Produto> buscarPorFaixaDePreco(@Param("precoMin") BigDecimal precoMin, @Param("precoMax") BigDecimal precoMax);
}
