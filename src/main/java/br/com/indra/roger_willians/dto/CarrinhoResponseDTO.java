package br.com.indra.roger_willians.dto;

import br.com.indra.roger_willians.model.enums.StatusCarrinho;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CarrinhoResponseDTO(
        UUID id,
        UUID usuarioId,
        StatusCarrinho status,
        List<ItemCarrinhoResponseDTO> itens,
        BigDecimal valorTotalGeral
) {}