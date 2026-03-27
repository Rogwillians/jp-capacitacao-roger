package br.com.indra.roger_willians.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemPedidoResponseDTO(
        UUID id,
        UUID produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoSnapshot,
        BigDecimal subtotal
) {}
