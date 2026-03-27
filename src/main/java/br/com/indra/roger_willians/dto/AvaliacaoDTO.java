package br.com.indra.roger_willians.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AvaliacaoDTO(
        @NotNull(message = "O ID do produto é obrigatório.")
        UUID produtoId,

        @NotNull(message = "O ID do pedido é obrigatório.")
        UUID pedidoId,

        @NotNull(message = "A nota é obrigatória.")
        @Min(value = 1, message = "A nota mínima é 1.")
        @Max(value = 5, message = "A nota máxima é 5.")
        Integer nota,

        String comentario
) {}
