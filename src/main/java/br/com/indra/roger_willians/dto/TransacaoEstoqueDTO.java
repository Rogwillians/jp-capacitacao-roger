package br.com.indra.roger_willians.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record TransacaoEstoqueDTO(@NotNull(message = "A quantidade é obrigatória.")
                                  @Positive(message = "A quantidade deve ser maior que zero.")
                                  Integer quantidade,

                                  String motivo,
                                  UUID referenciaId,
                                  String criadoPor ) {

}
