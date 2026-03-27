package br.com.indra.roger_willians.repository;

import br.com.indra.roger_willians.model.AuditLog;
import br.com.indra.roger_willians.model.enums.TipoEntidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByEntidadeIdOrderByDataHoraDesc( UUID entidadeId);
    List<AuditLog> findByTipoEntidadeOrderByDataHoraDesc(TipoEntidade tipoEntidade);
}
