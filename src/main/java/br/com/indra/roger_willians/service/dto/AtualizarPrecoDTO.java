package br.com.indra.roger_willians.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AtualizarPrecoDTO(@NotNull(message = "O novo preço é obrigatório.")
                                @Positive(message = "O preço deve ser maior que zero.")
                                BigDecimal preco) {
}
