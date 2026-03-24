package br.com.indra.roger_willians.service.dto;

import br.com.indra.roger_willians.model.enums.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PedidoResponseDTO(
        UUID id,
        UUID usuarioId,
        StatusPedido status,
        BigDecimal valorTotal,
        BigDecimal valorDesconto,
        BigDecimal valorFrete,
        String enderecoEntrega,
        List<ItemPedidoResponseDTO> itens,
        LocalDateTime dataCriacao
) {}
