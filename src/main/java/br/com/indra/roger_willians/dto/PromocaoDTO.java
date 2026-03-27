package br.com.indra.roger_willians.dto;

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

        @NotNull(message = "O tipo de desconto é obrigatório.")
        TipoDesconto tipo,

        @NotNull(message = "O valor do desconto é obrigatório.")
        @Positive(message = "O valor deve ser maior que zero.")
        BigDecimal valor,

        @NotNull(message = "A data de início é obrigatória.")
        LocalDateTime dataInicio,

        @NotNull(message = "A data de fim é obrigatória.")
        @Future(message = "A data de fim deve ser no futuro.")
        LocalDateTime dataFim,

        @NotNull(message = "O limite de uso é obrigatório.")
        @Positive(message = "O limite de uso deve ser maior que zero.")
        Integer limiteUso,

        @NotNull(message = "O tipo de aplicação é obrigatório.")
        TipoAplicacaoCupom tipoAplicacao,

        UUID produtoAplicavelId,
        UUID categoriaAplicavelId
) {}