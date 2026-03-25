package br.com.indra.roger_willians.service;

import br.com.indra.roger_willians.exception.RecursoNaoEncontradoException;
import br.com.indra.roger_willians.model.Categoria;
import br.com.indra.roger_willians.model.Produto;
import br.com.indra.roger_willians.repository.ProdutoRepository;
import br.com.indra.roger_willians.service.dto.ProdutoDTO;
import br.com.indra.roger_willians.service.dto.ProdutoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final HistoricoPrecoService historicoPrecoService;
    private final CategoriaService categoriaService;

    public List<ProdutoResponseDTO> findAll(){

        return produtoRepository.findAll().stream()
                .map(this::converterParaDTO
                )
                .toList();
    }

    public ProdutoResponseDTO findById(UUID id){
        final var produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        return converterParaDTO(produto);
    }

    public List<ProdutoResponseDTO> buscarPorCategoria(UUID categoriaId) {
        List<Produto> produto = produtoRepository.findByCategoriaId(categoriaId);

        if (produto.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum produto encontrado para a categoria com ID: " + categoriaId);
        }

        return produto.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<ProdutoResponseDTO> buscarAtivo() {
        List<Produto> produto = produtoRepository.findByAtivoTrue();

        if (produto.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum produto ativo encontrado.");
        }

        return produto.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<ProdutoResponseDTO> buscarInativo() {
        List<Produto> produto = produtoRepository.findByAtivoFalse();

        if (produto.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum produto ativo encontrado.");
        }

        return produto.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<ProdutoResponseDTO> buscarPorNomeContendo(String nome) {
        List<Produto> produto = produtoRepository.findByNomeContainingIgnoreCase(nome);

        if (produto.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum produto encontrado com o nome: " + nome);
        }

        return produto.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<ProdutoResponseDTO> buscarPoucoEstoque(Integer quantidadeMinima) {

        if (quantidadeMinima != null && quantidadeMinima < 0) {
            throw new IllegalArgumentException("A quantidade mínima não pode ser menor que zero.");
        }

        if (quantidadeMinima == null) {
            throw new IllegalArgumentException("O parâmetro 'quantidade' é obrigatório.");
        }
        List<Produto> produto = produtoRepository.findByQuantidadeEstoqueLessThanEqual(quantidadeMinima);

        return produto.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public List<ProdutoResponseDTO> buscarPorFaixaDePreco(BigDecimal precoMin, BigDecimal precoMax) {

        if (precoMin.compareTo(precoMax) > 0) {
            throw new IllegalArgumentException("O preço mínimo não pode ser maior que o preço máximo.");
        }

        return produtoRepository.findByPrecoBetween(precoMin, precoMax).stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public ProdutoResponseDTO cadastrarProduto(ProdutoDTO dto) {

        if (produtoRepository.findBySku(dto.sku()).isPresent()) {
            throw new IllegalArgumentException("Já existe um produto cadastrado com o SKU: " + dto.sku());
        }

        Categoria categoria = categoriaService.buscarEntidadePorId(dto.categoriaId());

        Produto produtoNovo = converterParaEntidade(dto, categoria);

        Produto produtoSalvo = produtoRepository.save(produtoNovo);

        return converterParaDTO(produtoSalvo);
    }

    @Transactional
    public ProdutoResponseDTO atualizarProduto(UUID id, ProdutoDTO dto) {
        final var produtoAtual = produtoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));

        if (!produtoAtual.getSku().equals(dto.sku()) &&
                produtoRepository.findBySku(dto.sku()).isPresent()) {
            throw new IllegalArgumentException("Já existe outro produto cadastrado com o SKU: " + dto.sku());
        }

        Categoria categoria = categoriaService.buscarEntidadePorId(dto.categoriaId());

        atualizarDadosEntidade(produtoAtual, dto, categoria);

        Produto produtoAtualizado = produtoRepository.save(produtoAtual);

        return converterParaDTO(produtoAtualizado);
    }


    @Transactional
    public ProdutoResponseDTO atualizarPreco(UUID id, BigDecimal precoNovo) {
        final var produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
        Produto produtoAtualizado;

        BigDecimal precoAntigo = produto.getPreco();

        produto.setPreco(precoNovo);

        if (precoAntigo.compareTo(precoNovo) != 0) {


            produto.setPreco(precoNovo);
            produtoAtualizado = produtoRepository.save(produto);


            historicoPrecoService.registrarHistorico(produtoAtualizado, precoAntigo, precoNovo);
        } else{
            throw new IllegalArgumentException("O preço novo é igual ao preço atual.");
        }


        return converterParaDTO(produtoAtualizado);

    }

    @Transactional
    public ProdutoResponseDTO reativarProduto(UUID id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        if(produto.getAtivo()) {
            throw new IllegalArgumentException("O produto já está ativo.");
        }

        produto.setAtivo(true);

        return converterParaDTO(produtoRepository.save(produto));
    }


    @Transactional
    public void deletarProduto(UUID id) {
        final var produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        produtoRepository.delete(produto);
    }

    @Transactional
    public void inativarProduto(UUID id) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        produto.setAtivo(false);

        produtoRepository.save(produto);
    }

    public Produto buscarEntidadePorId(UUID id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
    }

    public void atualizarEntidade(Produto produto) {
        produtoRepository.save(produto);
    }

    private ProdutoResponseDTO converterParaDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getSku(),
                produto.getPreco(),
                produto.getQuantidadeEstoque(),
                produto.getNotaMedia()
        );
    }

    private Produto converterParaEntidade(ProdutoDTO dto, Categoria categoria) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setSku(dto.sku());
        produto.setPreco(dto.preco());
        produto.setPrecoCusto(dto.precoCusto());
        produto.setQuantidadeEstoque(dto.quantidadeEstoque());

        produto.setCategoria(categoria);

        return produto;
    }

    private void atualizarDadosEntidade(Produto produtoExistente, ProdutoDTO dto, Categoria categoria) {
        produtoExistente.setNome(dto.nome());
        produtoExistente.setDescricao(dto.descricao());
        produtoExistente.setSku(dto.sku());
        produtoExistente.setPreco(dto.preco());
        produtoExistente.setPrecoCusto(dto.precoCusto());
        produtoExistente.setQuantidadeEstoque(dto.quantidadeEstoque());

        produtoExistente.setCategoria(categoria);
    }
}
