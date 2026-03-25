package br.com.indra.roger_willians.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProdutoDTO(@NotBlank(message = "O nome do produto é obrigatório.")
                          String nome,

                         @Size(max = 4000, message = "A descrição não pode passar de 4000 caracteres.")
                          String descricao,

                         @NotBlank(message = "O SKU é obrigatório.")
                          String sku,

                         @NotBlank(message = "O preço de venda é obrigatório.")
                          @Positive(message = "O preço de venda deve ser maior que zero.")
                          BigDecimal preco,

                         @PositiveOrZero(message = "O preço de custo não pode ser negativo.")
                          BigDecimal precoCusto,

                         UUID categoriaId,

                         @NotBlank(message = "A quantidade inicial em estoque é obrigatória.")
                          @PositiveOrZero(message = "A quantidade em estoque não pode ser negativa.")
                          Integer quantidadeEstoque) {
}