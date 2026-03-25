package br.com.indra.roger_willians.service;

import br.com.indra.roger_willians.service.dto.AvaliacaoDTO;
import br.com.indra.roger_willians.service.dto.AvaliacaoResponseDTO;
import br.com.indra.roger_willians.model.Avaliacao;
import br.com.indra.roger_willians.model.Produto;
import br.com.indra.roger_willians.repository.AvaliacaoRepository;
import br.com.indra.roger_willians.repository.ItemPedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoService produtoService;

    @Transactional
    public AvaliacaoResponseDTO criarAvaliacao(UUID usuarioId, AvaliacaoDTO dto) {

        boolean comprouProduto = itemPedidoRepository.existsByPedidoUsuarioIdAndPedidoIdAndProdutoId(
                usuarioId, dto.pedidoId(), dto.produtoId()
        );

        if (!comprouProduto) {
            throw new IllegalArgumentException("Você não pode avaliar um produto que não comprou.");
        }

        if (avaliacaoRepository.existsByPedidoIdAndProdutoId(dto.pedidoId(), dto.produtoId())) {
            throw new IllegalArgumentException("Você já avaliou este produto.");
        }

        Avaliacao avaliacao =  avaliacaoRepository.save(converterParaEntidade(usuarioId, dto));

        atualizarMediaProduto(dto.produtoId());

        return converterParaDTO(avaliacao);
    }

    public List<AvaliacaoResponseDTO> listarPorProduto(UUID produtoId) {
        return avaliacaoRepository.findByProdutoIdOrderByDataCriacaoDesc(produtoId)
                .stream()
                .map(this::converterParaDTO)
                .toList();
    }

    private void atualizarMediaProduto(UUID produtoId) {
        Double media = avaliacaoRepository.findAverageNotaByProdutoId(produtoId);
        if (media == null) {
            media = 0.0;
        }
        Produto produto = produtoService.buscarEntidadePorId(produtoId);

        produto.setNotaMedia(BigDecimal.valueOf(media)
                                        .setScale(2, RoundingMode.HALF_UP));

        produtoService.atualizarEntidade(produto);
    }

    private Avaliacao converterParaEntidade(UUID usuarioId, AvaliacaoDTO dto) {
        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setUsuarioId(usuarioId);
        avaliacao.setProdutoId(dto.produtoId());
        avaliacao.setPedidoId(dto.pedidoId());
        avaliacao.setNota(dto.nota());
        avaliacao.setComentario(dto.comentario());
        return avaliacao;
    }

    private AvaliacaoResponseDTO converterParaDTO(Avaliacao avaliacao) {
        return new AvaliacaoResponseDTO(
                avaliacao.getId(),
                avaliacao.getUsuarioId(),
                avaliacao.getNota(),
                avaliacao.getComentario(),
                avaliacao.getDataCriacao()
        );
    }
}