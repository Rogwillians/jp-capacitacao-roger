package br.com.indra.roger_willians.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record HistoricoPrecoResponseDTO(
        UUID id,
        BigDecimal precoAntigo,
        BigDecimal precoNovo,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm", timezone = "America/Sao_Paulo")
        LocalDateTime dataAlteracao
) {}