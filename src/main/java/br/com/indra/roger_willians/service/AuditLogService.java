package br.com.indra.roger_willians.service;

import br.com.indra.roger_willians.exception.RecursoNaoEncontradoException;
import br.com.indra.roger_willians.model.AuditLog;
import br.com.indra.roger_willians.model.enums.AcaoAuditoria;
import br.com.indra.roger_willians.model.enums.TipoEntidade;
import br.com.indra.roger_willians.repository.AuditLogRepository;
import br.com.indra.roger_willians.dto.AuditLogResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Async
    public void registrarLog(TipoEntidade tipoEntidade, UUID entidadeId, AcaoAuditoria acao,
                             Object estadoAnterior, Object estadoNovo, UUID usuarioId) {

        AuditLog log = new AuditLog();
        log.setTipoEntidade(tipoEntidade);
        log.setEntidadeId(entidadeId);
        log.setAcao(acao);
        log.setRealizadoPor(usuarioId);

        try {

            if (estadoAnterior != null) {
                log.setJsonAntes(objectMapper.writeValueAsString(estadoAnterior));
            }
            if (estadoNovo != null) {
                log.setJsonDepois(objectMapper.writeValueAsString(estadoNovo));
            }

            auditLogRepository.save(log);

        } catch (JsonProcessingException e) {

            System.err.println("Erro ao enviar para Auditoria: " + e.getMessage());
        }
    }

    public List<AuditLogResponseDTO> buscarLogsPorTipoEntidade(TipoEntidade tipoEntidade){
        return auditLogRepository.findByTipoEntidadeOrderByDataHoraDesc(tipoEntidade).stream()
                .map(log -> new AuditLogResponseDTO(
                        log.getId(),
                        log.getTipoEntidade(),
                        log.getEntidadeId(),
                        log.getAcao(),
                        log.getJsonAntes(),
                        log.getJsonDepois(),
                        log.getRealizadoPor(),
                        log.getDataHora()
                ))
                .toList();
    }

    public List<AuditLogResponseDTO> buscarLogsPorEntidadeId(UUID id){
        final var auditLog = auditLogRepository.findByEntidadeIdOrderByDataHoraDesc(id);

        if (auditLog.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhum log encontrado para a entidade");
        }

        return auditLog.stream()
                .map(log -> new AuditLogResponseDTO(
                        log.getId(),
                        log.getTipoEntidade(),
                        log.getEntidadeId(),
                        log.getAcao(),
                        log.getJsonAntes(),
                        log.getJsonDepois(),
                        log.getRealizadoPor(),
                        log.getDataHora()
                ))
                .toList();
    }
}