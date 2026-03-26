package br.com.indra.roger_willians.service.dto;

import br.com.indra.roger_willians.model.enums.TipoAplicacaoCupom;
import br.com.indra.roger_willians.model.enums.TipoDesconto;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PromocaoDTO(
        @NotBlank(message = "O código do cupom é obrigatório.")
        String codigo,

        @NotBlank(message = "O tipo de desconto é obrigatório.")
        TipoDesconto tipo,

        @NotNull(message = "O valor do desconto é obrigatório.")
        @Positive(message = "O valor deve ser maior que zero.")
        BigDecimal valor,

        @NotBlank(message = "A data de início é obrigatória.")
        LocalDateTime dataInicio,

        @NotBlank(message = "A data de fim é obrigatória.")
        @Future(message = "A data de fim deve ser no futuro.")
        LocalDateTime dataFim,

        @NotBlank(message = "O limite de uso é obrigatório.")
        @Positive(message = "O limite de uso deve ser maior que zero.")
        Integer limiteUso,

        @NotBlank(message = "O tipo de aplicação é obrigatório.")
        TipoAplicacaoCupom tipoAplicacao,

        UUID produtoAplicavelId,
        UUID categoriaAplicavelId
) {}