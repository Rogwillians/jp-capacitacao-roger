package br.com.indra.roger_willians.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record CategoriaDTO(
        @NotBlank(message = "O nome da categoria é obrigatório.")
        String nome,

        UUID categoriaPaiId
) {}
