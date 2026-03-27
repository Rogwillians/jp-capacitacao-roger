package br.com.indra.roger_willians.dto;

import java.util.UUID;


public record CategoriaResponseDTO(
        UUID id,
        String nome,
        UUID categoriaPaiId
) {}
