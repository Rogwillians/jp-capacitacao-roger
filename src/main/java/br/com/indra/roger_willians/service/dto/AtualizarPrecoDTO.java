package br.com.indra.roger_willians.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record AtualizarPrecoDTO(@NotBlank(message = "O novo preço é obrigatório.")
                                @Positive(message = "O preço deve ser maior que zero.")
                                BigDecimal preco) {}
