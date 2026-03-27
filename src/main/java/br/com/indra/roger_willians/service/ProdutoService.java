package br.com.indra.roger_willians.service;

import br.com.indra.roger_willians.exception.RecursoNaoEncontradoException;
import br.com.indra.roger_willians.model.Categoria;
import br.com.indra.roger_willians.model.Produto;
import br.com.indra.roger_willians.model.enums.AcaoAuditoria;
import br.com.indra.roger_willians.model.enums.TipoEntidade;
import br.com.indra.roger_willians.repository.ProdutoRepository;
import br.com.indra.roger_willians.service.dto.ProdutoDTO;
import br.com.indra.roger_willians.service.dto.ProdutoResponseDTO;
import br.com.indra.roger_willians.service.dto.ProdutoSnapShotDTO;
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
    private final AuditLogService auditLogService;

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
    public ProdutoResponseDTO cadastrarProduto(ProdutoDTO dto, UUID usuarioId) {

        if (produtoRepository.findBySku(dto.sku()).isPresent()) {
            throw new IllegalArgumentException("Já existe um produto cadastrado com o SKU: " + dto.sku());
        }

        Categoria categoria = categoriaService.buscarEntidadePorId(dto.categoriaId());

        Produto produtoSalvo = produtoRepository.save(converterParaEntidade(dto, categoria, usuarioId));


        ProdutoSnapShotDTO produtoCriado = snapShot(produtoSalvo);

        auditLogService.registrarLog(
                TipoEntidade.PRODUTO,
                produtoSalvo.getId(),
                AcaoAuditoria.CRIACAO,
                null,
                produtoCriado,
                usuarioId
        );

        return converterParaDTO(produtoSalvo);
    }

    @Transactional
    public ProdutoResponseDTO atualizarProduto(UUID id, UUID usuarioId, ProdutoDTO dto) {
        final var produtoAtual = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));

        if (!produtoAtual.getSku().equals(dto.sku()) &&
                produtoRepository.findBySku(dto.sku()).isPresent()) {
            throw new IllegalArgumentException("Já existe outro produto cadastrado com o SKU: " + dto.sku());
        }
        if(!produtoAtual.getVendedorId().equals(usuarioId)){
            throw new IllegalArgumentException("Apenas o vendedor pode atualizar o produto.");
        }
        ProdutoSnapShotDTO produtoAntigo = snapShot(produtoAtual);

        atualizarDadosEntidade(produtoAtual, dto, categoriaService.buscarEntidadePorId(dto.categoriaId()));

        Produto produtoAtualizado = produtoRepository.save(produtoAtual);

        auditLogService.registrarLog(
                TipoEntidade.PRODUTO,
                produtoAtualizado.getId(),
                AcaoAuditoria.ATUALIZACAO,
                produtoAntigo,
                snapShot(produtoAtualizado),
                usuarioId
        );

        return converterParaDTO(produtoAtualizado);
    }

    @Transactional
    public ProdutoResponseDTO atualizarPreco(UUID id, UUID usuarioId, BigDecimal precoNovo) {
        var produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
        if(!produto.getVendedorId().equals(usuarioId)){
            throw new IllegalArgumentException("Apenas o vendedor pode atualizar o produto.");
        }
        var precoAntigo = produto.getPreco();
        ProdutoSnapShotDTO produtoAntigo = snapShot(produto);

        if (produto.getPreco().compareTo(precoNovo) != 0) {

            produto.setPreco(precoNovo);
            produtoRepository.save(produto);

            historicoPrecoService.registrarHistorico(produto, precoAntigo, precoNovo);
        } else{
            throw new IllegalArgumentException("O preço novo é igual ao preço atual.");
        }

        ProdutoSnapShotDTO produtoNovo = snapShot(produto);

        auditLogService.registrarLog(
                TipoEntidade.PRODUTO,
                produto.getId(),
                AcaoAuditoria.ATUALIZACAO,
                produtoAntigo,
                produtoNovo,
                usuarioId
        );

        return converterParaDTO(produto);

    }

    @Transactional
    public ProdutoResponseDTO reativarProduto(UUID id, UUID usuarioId) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
        if(!produto.getVendedorId().equals(usuarioId)){
            throw new IllegalArgumentException("Apenas o vendedor pode atualizar o produto.");
        }

        if(produto.getAtivo()) {
            throw new IllegalArgumentException("O produto já está ativo.");
        }

        produto.setAtivo(true);

        return converterParaDTO(produtoRepository.save(produto));
    }

    @Transactional
    public void deletarProduto(UUID id, UUID usuarioId) {
        final var produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
        if(!produto.getVendedorId().equals(usuarioId)){
            throw new IllegalArgumentException("Apenas o vendedor pode deletar o produto.");
        }
        ProdutoSnapShotDTO produtodeletado = snapShot(produto);
        produtoRepository.delete(produto);

        auditLogService.registrarLog(
                TipoEntidade.PRODUTO,
                produto.getId(),
                AcaoAuditoria.EXCLUSAO,
                produtodeletado,
                null,
                usuarioId
        );

    }

    @Transactional
    public void inativarProduto(UUID id, UUID usuarioId) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado"));
        if(!produto.getVendedorId().equals(usuarioId)){
            throw new IllegalArgumentException("Apenas o vendedor pode atualizar o produto.");
        }

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
                produto.getNotaMedia(),
                produto.getVendedorId()
        );
    }

    private Produto converterParaEntidade(ProdutoDTO dto, Categoria categoria, UUID usuarioId) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setSku(dto.sku());
        produto.setPreco(dto.preco());
        produto.setPrecoCusto(dto.precoCusto());
        produto.setQuantidadeEstoque(dto.quantidadeEstoque());
        produto.setVendedorId(usuarioId);

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

    private ProdutoSnapShotDTO snapShot(Produto produto) {
        return new ProdutoSnapShotDTO(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getSku(),
                produto.getPreco(),
                produto.getPrecoCusto(),
                produto.getQuantidadeEstoque(),
                produto.getCategoria().getId(),
                produto.getNotaMedia(),
                produto.getVendedorId(),
                produto.getAtivo()
        );
    }
}
