package br.com.indra.roger_willians.service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemCarrinhoResponseDTO(
        UUID id,
        UUID produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoSnapshot,
        BigDecimal subtotal
) {}
