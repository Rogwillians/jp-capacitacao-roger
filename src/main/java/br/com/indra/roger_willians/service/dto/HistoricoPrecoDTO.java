package br.com.indra.roger_willians.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoricoPrecoDTO {

    private UUID id;
    private String produto;
    private BigDecimal precoAntigo;
    private BigDecimal precoNovo;
    private String dataAlteracao;
}
