package br.com.indra.roger_willians.service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record AvaliacaoDTO(
        @NotBlank(message = "O ID do produto é obrigatório.")
        UUID produtoId,

        @NotBlank(message = "O ID do pedido é obrigatório.")
        UUID pedidoId,

        @NotBlank(message = "A nota é obrigatória.")
        @Min(value = 1, message = "A nota mínima é 1.")
        @Max(value = 5, message = "A nota máxima é 5.")
        Integer nota,

        String comentario
) {}
