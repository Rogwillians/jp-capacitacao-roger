package br.com.indra.roger_willians.service;


import br.com.indra.roger_willians.exception.RecursoNaoEncontradoException;
import br.com.indra.roger_willians.model.Carrinho;
import br.com.indra.roger_willians.model.ItemCarrinho;
import br.com.indra.roger_willians.model.Produto;
import br.com.indra.roger_willians.model.enums.StatusCarrinho;
import br.com.indra.roger_willians.repository.CarrinhoRepository;
import br.com.indra.roger_willians.dto.CarrinhoResponseDTO;
import br.com.indra.roger_willians.dto.ItemCarrinhoDTO;
import br.com.indra.roger_willians.dto.ItemCarrinhoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;
    private final ProdutoService produtoService;

    @Transactional
    public Carrinho obterOuCriarCarrinhoAtivo(UUID usuarioId) {
        return carrinhoRepository.findByUsuarioIdAndStatus(usuarioId, StatusCarrinho.ATIVO)
                .orElseGet(() -> {
                    Carrinho novoCarrinho = new Carrinho();
                    novoCarrinho.setUsuarioId(usuarioId);
                    novoCarrinho.setStatus(StatusCarrinho.ATIVO);
                    return carrinhoRepository.save(novoCarrinho);
                });
    }


    @Transactional
    public CarrinhoResponseDTO adicionarItem(UUID usuarioId, ItemCarrinhoDTO dto) {
        Carrinho carrinho = obterOuCriarCarrinhoAtivo(usuarioId);
        Produto produto = produtoService.buscarEntidadePorId(dto.produtoId());

        Optional<ItemCarrinho> itemExistente = carrinho.getItens().stream()
                .filter(item -> item.getProduto().getId().equals(produto.getId()))
                .findFirst();

        if (itemExistente.isPresent()) {

            ItemCarrinho item = itemExistente.get();
            item.setQuantidade(item.getQuantidade() + dto.quantidade());
        } else {

            ItemCarrinho novoItem = converterParaEntidade(dto, carrinho, produto);
            carrinho.getItens().add(novoItem);
        }

        return converterParaDTO(carrinhoRepository.save(carrinho));
    }

    @Transactional
    public CarrinhoResponseDTO atualizarQuantidadeItem(UUID usuarioId, UUID itemId, Integer novaQuantidade) {
        Carrinho carrinho = obterOuCriarCarrinhoAtivo(usuarioId);

        ItemCarrinho item = carrinho.getItens().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado neste carrinho."));

        item.setQuantidade(novaQuantidade);

        return converterParaDTO(carrinhoRepository.save(carrinho));
    }

    @Transactional
    public CarrinhoResponseDTO removerItem(UUID usuarioId, UUID itemId) {
        Carrinho carrinho = obterOuCriarCarrinhoAtivo(usuarioId);

        boolean removido = carrinho.getItens().removeIf(item -> item.getId().equals(itemId));

        if (!removido) {
            throw new RecursoNaoEncontradoException("Item não encontrado neste carrinho.");
        }

        return converterParaDTO(carrinhoRepository.save(carrinho));
    }


    public CarrinhoResponseDTO converterParaDTO(Carrinho carrinho) {

        var itensDTO = carrinho.getItens().stream().map(item -> {
            BigDecimal subtotal = item.getPrecoSnapshot().multiply(BigDecimal.valueOf(item.getQuantidade()));

            return new ItemCarrinhoResponseDTO(
                    item.getId(),
                    item.getProduto().getId(),
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    item.getPrecoSnapshot(),
                    subtotal
            );
        }).toList();

        BigDecimal totalGeral = itensDTO.stream()
                .map(ItemCarrinhoResponseDTO::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CarrinhoResponseDTO(
                carrinho.getId(),
                carrinho.getUsuarioId(),
                carrinho.getStatus(),
                itensDTO,
                totalGeral
        );
    }

    private ItemCarrinho converterParaEntidade(ItemCarrinhoDTO dto, Carrinho carrinho, Produto produto) {

        ItemCarrinho item = new ItemCarrinho();

        item.setCarrinho(carrinho);
        item.setProduto(produto);
        item.setQuantidade(dto.quantidade());


        item.setPrecoSnapshot(produto.getPreco());

        return item;
    }
}