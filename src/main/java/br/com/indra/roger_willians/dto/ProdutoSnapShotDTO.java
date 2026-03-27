package br.com.indra.roger_willians.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoSnapShotDTO(UUID id,
                                 String nome,
                                 String descricao,
                                 String sku,
                                 BigDecimal preco,
                                 BigDecimal precoCusto,
                                 Integer quantidadeEstoque,
                                 UUID categoriaId,
                                 BigDecimal notaoMedia,
                                 UUID vendedorId,
                                 Boolean ativo

) {

}
