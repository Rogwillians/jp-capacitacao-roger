package br.com.indra.roger_willians.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record AvaliacaoResponseDTO(
        UUID id,
        UUID usuarioId,
        Integer nota,
        String comentario,
        LocalDateTime dataCriacao
) {}
