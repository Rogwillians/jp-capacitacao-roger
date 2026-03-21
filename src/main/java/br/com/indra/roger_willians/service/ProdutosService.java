package br.com.indra.roger_willians.service;

import br.com.indra.roger_willians.model.Produtos;
import br.com.indra.roger_willians.repository.ProdutosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutosService {

    private final ProdutosRepository produtosRepository;
    private final HistoricoPrecoService historicoPrecoService;

    public List<Produtos> findAll(){
        return produtosRepository.findAll();
    }

    public Produtos findById(UUID id){
        return produtosRepository.findById(id).orElse(null);
    }

    public Produtos cadastrarProduto(Produtos produtos){
        return produtosRepository.save(produtos);
    }

    public Produtos atualizarProduto(Produtos produto) {
        return produtosRepository.save(produto);
    }


    @Transactional
    public Produtos atualizarPreco(UUID id, BigDecimal precoNovo) {
        final var produto = produtosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        BigDecimal precoOld = produto.getPreco();

        produto.setPreco(precoNovo);

        Produtos produtoAtualizado = produtosRepository.saveAndFlush(produto);

        historicoPrecoService.registrarHistorico(produtoAtualizado, precoOld, precoNovo);

        return produtoAtualizado;

    }

    public void deletarProduto(UUID id) {
        produtosRepository.deleteById(id);
    }
}
