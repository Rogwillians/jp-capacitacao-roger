package br.com.indra.roger_willians.service.dto;

import br.com.indra.roger_willians.model.enums.TipoTransacao;

import java.time.LocalDateTime;
import java.util.UUID;

public record TransacaoEstoqueResponseDTO(UUID id,
                                          UUID produtoId,
                                          Integer quantidadeAlterada,
                                          TipoTransacao tipoTransacao,
                                          String motivo,
                                          UUID referenciaId,
                                          String criadoPor,
                                          LocalDateTime dataCriacao) {
}