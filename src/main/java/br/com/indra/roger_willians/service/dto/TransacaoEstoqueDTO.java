package br.com.indra.roger_willians.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record TransacaoEstoqueDTO(@NotBlank(message = "A quantidade é obrigatória.")
                                  @Positive(message = "A quantidade deve ser maior que zero.")
                                  Integer quantidade,

                                  String motivo,
                                  UUID referenciaId,
                                  String criadoPor ) {

}
