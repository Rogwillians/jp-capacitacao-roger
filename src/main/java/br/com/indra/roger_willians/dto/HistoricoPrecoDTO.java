package br.com.indra.roger_willians.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;



public record HistoricoPrecoDTO(
        UUID id,
        UUID produtoId,
        BigDecimal precoAntigo,
        BigDecimal precoNovo,
        LocalDateTime dataAlteracao
) {}
