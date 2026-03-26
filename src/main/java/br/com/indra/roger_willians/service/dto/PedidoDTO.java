package br.com.indra.roger_willians.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PedidoDTO(
        @NotBlank(message = "O endereço de entrega é obrigatório.")
        String enderecoEntrega,

        @NotNull(message = "O valor do frete é obrigatório.")
        BigDecimal valorFrete,

        String codigoCupom
) {}
