package br.com.indra.roger_willians.service.dto;

import br.com.indra.roger_willians.model.enums.AcaoAuditoria;
import br.com.indra.roger_willians.model.enums.TipoEntidade;

import java.time.LocalDateTime;
import java.util.UUID;

public record AuditLogResponseDTO(
        UUID id,
        TipoEntidade tipoEntidade,
        UUID entidadeId,
        AcaoAuditoria acao,
        String jsonAntes,
        String jsonDepois,
        UUID realizadoPor,
        LocalDateTime dataHora
) {}