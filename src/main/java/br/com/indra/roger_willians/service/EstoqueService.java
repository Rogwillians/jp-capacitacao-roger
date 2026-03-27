package br.com.indra.roger_willians.service;

import br.com.indra.roger_willians.exception.RecursoNaoEncontradoException;
import br.com.indra.roger_willians.model.Produto;
import br.com.indra.roger_willians.model.TransacaoEstoque;
import br.com.indra.roger_willians.model.enums.TipoTransacao;
import br.com.indra.roger_willians.repository.TransacaoEstoqueRepository;
import br.com.indra.roger_willians.dto.TransacaoEstoqueDTO;
import br.com.indra.roger_willians.dto.TransacaoEstoqueResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final TransacaoEstoqueRepository transacaoEstoqueRepository;
    private final ProdutoService produtoService;

    public List<TransacaoEstoqueResponseDTO> buscarHistorico(UUID produtoId) {

        List<TransacaoEstoque> transacao = transacaoEstoqueRepository.findByProdutoIdOrderByDataCriacaoDesc(produtoId);

        if(transacao.isEmpty()){
            throw new RecursoNaoEncontradoException("Não foram encontradas transações para o produto");
        }

        return transacao.stream()
                .map(this::converterParaDTO)
                .toList();
    }

    @Transactional
    public TransacaoEstoqueResponseDTO adicionarEstoque(UUID produtoId, TransacaoEstoqueDTO dto){

        Produto produto = produtoService.buscarEntidadePorId(produtoId);

        int estoqueAtual = produto.getQuantidadeEstoque() != null ? produto.getQuantidadeEstoque() : 0;
        produto.setQuantidadeEstoque(estoqueAtual + dto.quantidade());

        produtoService.atualizarEntidade(produto);

        return registrarTransacao(produto, dto.quantidade(), TipoTransacao.ENTRADA, dto);
    }

    @Transactional
    public TransacaoEstoqueResponseDTO removerEstoque(UUID produtoId, TransacaoEstoqueDTO dto) {
        Produto produto = produtoService.buscarEntidadePorId(produtoId);

        if (produto.getQuantidadeEstoque() < dto.quantidade()) {
            throw new IllegalArgumentException("Estoque insuficiente. Quantidade atual: " + produto.getQuantidadeEstoque());
        }

        produto.setQuantidadeEstoque(produto.getQuantidadeEstoque() - dto.quantidade());

        return registrarTransacao(produto, -dto.quantidade(), TipoTransacao.SAIDA, dto);
    }

    private TransacaoEstoqueResponseDTO registrarTransacao(Produto produto, Integer delta, TipoTransacao tipo, TransacaoEstoqueDTO dto) {
        TransacaoEstoque transacao = new TransacaoEstoque();
        transacao.setProduto(produto);
        transacao.setQuantidade(delta);
        transacao.setTipoTransacao(tipo);
        transacao.setMotivo(dto.motivo());
        transacao.setReferenciaId(dto.referenciaId());
        transacao.setCriadoPor(dto.criadoPor());

        TransacaoEstoque transacaoSalva = transacaoEstoqueRepository.save(transacao);
        return converterParaDTO(transacaoSalva);
    }

    private TransacaoEstoqueResponseDTO converterParaDTO(TransacaoEstoque transacao) {
        return new TransacaoEstoqueResponseDTO(
                transacao.getId(),
                transacao.getProduto().getId(),
                transacao.getQuantidade(),
                transacao.getTipoTransacao(),
                transacao.getMotivo(),
                transacao.getReferenciaId(),
                transacao.getCriadoPor(),
                transacao.getDataCriacao()
        );
    }
}