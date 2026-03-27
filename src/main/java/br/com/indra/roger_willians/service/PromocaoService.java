package br.com.indra.roger_willians.service;

import br.com.indra.roger_willians.exception.RecursoNaoEncontradoException;
import br.com.indra.roger_willians.model.Carrinho;
import br.com.indra.roger_willians.model.ItemCarrinho;
import br.com.indra.roger_willians.model.Promocao;
import br.com.indra.roger_willians.model.enums.TipoAplicacaoCupom;
import br.com.indra.roger_willians.model.enums.TipoDesconto;
import br.com.indra.roger_willians.repository.PromocaoRepository;
import br.com.indra.roger_willians.repository.UsoPromocaoRepository;
import br.com.indra.roger_willians.dto.PromocaoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PromocaoService {

    private final PromocaoRepository promocaoRepository;
    private final UsoPromocaoRepository usoPromocaoRepository;

    public BigDecimal calcularDesconto(String codigoCupom, Carrinho carrinho, UUID usuarioId) {

        Promocao promocao = promocaoRepository.findByCodigo(codigoCupom)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cupom inválido ou não encontrado."));

        validarCupom(promocao, usuarioId);

        List<ItemCarrinho> itensAplicaveis = filtrarItensAplicaveis(promocao, carrinho.getItens());

        if (itensAplicaveis.isEmpty()) {
            throw new IllegalArgumentException("Este cupom não se aplica aos produtos do seu carrinho.");
        }

        BigDecimal subtotalAplicavel = itensAplicaveis.stream()
                .map(item -> item.getPrecoSnapshot().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return calcularValorMatematico(promocao, subtotalAplicavel);
    }

    public Promocao criarPromocao(PromocaoDTO dto) {

        if (promocaoRepository.findByCodigo(dto.codigo().toUpperCase()).isPresent()) {
            throw new IllegalArgumentException("Já existe um cupom com este código.");
        }

        return promocaoRepository.save(converterParaEntidade(dto));
    }

    private void validarCupom(Promocao promocao, UUID usuarioId) {
        if (!promocao.isValidoNoMomento(LocalDateTime.now())) {
            throw new IllegalArgumentException("Este cupom está expirado ou fora da data de validade.");
        }

        if (promocao.getContagemUso() >= promocao.getLimiteUso()) {
            throw new IllegalArgumentException("Este cupom já atingiu o limite máximo de usos.");
        }

        if (usoPromocaoRepository.existsByPromocaoIdAndUsuarioId(promocao.getId(), usuarioId)) {
            throw new IllegalArgumentException("Você já utilizou este cupom anteriormente.");
        }
    }

    private List<ItemCarrinho> filtrarItensAplicaveis(Promocao promocao, List<ItemCarrinho> itens) {
        if (promocao.getTipoAplicacao() == TipoAplicacaoCupom.CARRINHO) {
            return itens;
        }else
            if (promocao.getTipoAplicacao() == TipoAplicacaoCupom.PRODUTO) {
            return itens.stream()
                    .filter(item -> item.getProduto().getId()
                    .equals(promocao.getProdutoAplicavelId()))
                    .toList();
        }else
            if (promocao.getTipoAplicacao() == TipoAplicacaoCupom.CATEGORIA) {
            return itens.stream()
                    .filter(item -> item.getProduto().getCategoria().getId()
                    .equals(promocao.getCategoriaAplicavelId()))
                    .toList();
        }

        return List.of();
    }

    private BigDecimal calcularValorMatematico(Promocao promocao, BigDecimal subtotalAplicavel) {
        BigDecimal valorDesconto;

        if (promocao.getTipo() == TipoDesconto.PERCENTUAL) {


            valorDesconto = subtotalAplicavel.multiply(promocao.getValor()
                                                            .divide(BigDecimal
                                                            .valueOf(100), 2, RoundingMode.HALF_UP));
        } else {

            valorDesconto = promocao.getValor();
        }

        return valorDesconto.compareTo(subtotalAplicavel) > 0 ? subtotalAplicavel : valorDesconto;
    }

    private Promocao converterParaEntidade(PromocaoDTO dto) {
        Promocao promocao = new Promocao();

        promocao.setCodigo(dto.codigo().toUpperCase());
        promocao.setTipo(dto.tipo());
        promocao.setValor(dto.valor());
        promocao.setDataInicio(dto.dataInicio());
        promocao.setDataFim(dto.dataFim());
        promocao.setLimiteUso(dto.limiteUso());
        promocao.setTipoAplicacao(dto.tipoAplicacao());
        promocao.setProdutoAplicavelId(dto.produtoAplicavelId());
        promocao.setCategoriaAplicavelId(dto.categoriaAplicavelId());

        return promocao;
    }
}