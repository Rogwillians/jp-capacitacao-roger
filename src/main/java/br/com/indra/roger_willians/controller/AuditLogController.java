package br.com.indra.roger_willians.controller;

import br.com.indra.roger_willians.model.enums.TipoEntidade;
import br.com.indra.roger_willians.service.AuditLogService;
import br.com.indra.roger_willians.dto.AuditLogResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/audit-log")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("/entidade")
    public ResponseEntity<List<AuditLogResponseDTO>> buscarLogs(
            @RequestParam(name = "entity") TipoEntidade tipoEntidade) {
        List<AuditLogResponseDTO> logs = auditLogService.buscarLogsPorTipoEntidade(tipoEntidade);

        if(logs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(logs);
    }

    @GetMapping("/entidade/{entidadeId}")
    public ResponseEntity<List<AuditLogResponseDTO>> buscarLogsPorEntidadeId(@PathVariable UUID entidadeId) {
        List<AuditLogResponseDTO> logs = auditLogService.buscarLogsPorEntidadeId(entidadeId);

        if (logs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(logs);
    }
}