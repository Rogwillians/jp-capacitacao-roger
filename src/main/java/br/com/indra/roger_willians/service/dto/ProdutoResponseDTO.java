package br.com.indra.roger_willians.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProdutoResponseDTO(
        UUID id,
        String nome,
        String descricao,
        String sku,
        BigDecimal preco,
        Integer quantidadeEstoque

) {}
