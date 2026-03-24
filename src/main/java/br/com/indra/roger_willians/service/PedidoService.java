package br.com.indra.roger_willians.service;

import br.com.indra.roger_willians.exception.RecursoNaoEncontradoException;
import br.com.indra.roger_willians.model.Carrinho;
import br.com.indra.roger_willians.model.ItemCarrinho;
import br.com.indra.roger_willians.model.ItemPedido;
import br.com.indra.roger_willians.model.Pedido;
import br.com.indra.roger_willians.model.enums.StatusCarrinho;
import br.com.indra.roger_willians.model.enums.StatusPedido;
import br.com.indra.roger_willians.repository.CarrinhoRepository;
import br.com.indra.roger_willians.repository.PedidoRepository;
import br.com.indra.roger_willians.service.dto.ItemPedidoResponseDTO;
import br.com.indra.roger_willians.service.dto.PedidoDTO;
import br.com.indra.roger_willians.service.dto.PedidoResponseDTO;
import br.com.indra.roger_willians.service.dto.TransacaoEstoqueDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final CarrinhoService carrinhoService;
    private final CarrinhoRepository carrinhoRepository;
    private final EstoqueService estoqueService;
    private final PromocaoService promocaoService;

    @Transactional
    public PedidoResponseDTO criarPedido(UUID usuarioId, PedidoDTO dto) {
        Carrinho carrinho = carrinhoService.obterOuCriarCarrinhoAtivo(usuarioId);

        if (carrinho.getItens().isEmpty()) {
            throw new IllegalArgumentException("O carrinho está vazio.");
        }

        Pedido pedido = iniciarPedido(usuarioId, dto);

        if (dto.codigoCupom() != null && !dto.codigoCupom().isBlank()) {
            BigDecimal valorDesconto = promocaoService.calcularDesconto(dto.codigoCupom(), carrinho, usuarioId);
            pedido.setValorDesconto(valorDesconto);
        }

        pedido = pedidoRepository.save(pedido);
        BigDecimal totalItens = BigDecimal.ZERO;

        for (ItemCarrinho itemCarrinho : carrinho.getItens()) {

            TransacaoEstoqueDTO transacao = new TransacaoEstoqueDTO(
                    itemCarrinho.getQuantidade(),
                    "Venda (Pedido em criação)",
                    pedido.getId(),
                    usuarioId.toString()
            );

            estoqueService.removerEstoque(itemCarrinho.getProduto().getId(), transacao);

            ItemPedido itemPedido = converterParaEntidade(itemCarrinho, pedido);
            pedido.getItens().add(itemPedido);

            BigDecimal subtotal = itemPedido.getPrecoSnapshot().multiply(BigDecimal.valueOf(itemPedido.getQuantidade()));
            totalItens = totalItens.add(subtotal);
        }

        pedido.setValorTotal(totalItens.add(pedido.getValorFrete()).subtract(pedido.getValorDesconto()));

        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        carrinho.setStatus(StatusCarrinho.FINALIZADO);
        carrinhoRepository.save(carrinho);

        return converterParaDTO(pedidoSalvo);
    }

    public PedidoResponseDTO buscarPorId(UUID id, UUID usuarioId) {
        Pedido pedido = pedidoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado."));
        return converterParaDTO(pedido);
    }

    @Transactional
    public PedidoResponseDTO cancelarPedido(UUID id, UUID usuarioId) {
        Pedido pedido = pedidoRepository.findByIdAndUsuarioId(id, usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado."));

        if (pedido.getStatus() != StatusPedido.CRIADO && pedido.getStatus() != StatusPedido.PAGO) {
            throw new IllegalArgumentException("Não é possível cancelar um pedido com status: " + pedido.getStatus());
        }

        pedido.setStatus(StatusPedido.CANCELADO);

        for (ItemPedido item : pedido.getItens()) {
            TransacaoEstoqueDTO transacao = new TransacaoEstoqueDTO(
                    item.getQuantidade(),
                    "Estorno por Cancelamento de Pedido",
                    pedido.getId(),
                    usuarioId.toString()
            );
            estoqueService.adicionarEstoque(item.getProduto().getId(), transacao);
        }

        return converterParaDTO(pedidoRepository.save(pedido));
    }

    private PedidoResponseDTO converterParaDTO(Pedido pedido) {
        var itensDTO = pedido.getItens().stream().map(item -> new ItemPedidoResponseDTO(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getPrecoSnapshot(),
                item.getPrecoSnapshot().multiply(BigDecimal.valueOf(item.getQuantidade()))
        )).toList();

        return new PedidoResponseDTO(
                pedido.getId(), pedido.getUsuarioId(), pedido.getStatus(),
                pedido.getValorTotal(), pedido.getValorDesconto(), pedido.getValorFrete(),
                pedido.getEnderecoEntrega(), itensDTO, pedido.getDataCriacao()
        );
    }

    private ItemPedido converterParaEntidade(ItemCarrinho itemCarrinho, Pedido pedido) {
        ItemPedido itemPedido = new ItemPedido();

        itemPedido.setPedido(pedido);
        itemPedido.setProduto(itemCarrinho.getProduto());
        itemPedido.setQuantidade(itemCarrinho.getQuantidade());
        itemPedido.setPrecoSnapshot(itemCarrinho.getPrecoSnapshot());

        return itemPedido;
    }

    private Pedido iniciarPedido(UUID usuarioId, PedidoDTO dto) {
        Pedido pedido = new Pedido();

        pedido.setUsuarioId(usuarioId);
        pedido.setEnderecoEntrega(dto.enderecoEntrega());
        pedido.setValorFrete(dto.valorFrete());
        pedido.setValorDesconto(BigDecimal.ZERO);

        return pedido;
    }
}