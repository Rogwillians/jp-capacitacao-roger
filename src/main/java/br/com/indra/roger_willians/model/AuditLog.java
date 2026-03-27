package br.com.indra.roger_willians.model;

import br.com.indra.roger_willians.model.enums.AcaoAuditoria;
import br.com.indra.roger_willians.model.enums.TipoEntidade;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "auditoria_logs")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "tipo_entidade", nullable = false)
    private TipoEntidade tipoEntidade;

    @Column(name = "entidade_id", nullable = false, columnDefinition = "VARCHAR2(36)")
    private UUID entidadeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AcaoAuditoria acao;

    @Column(name = "json_antes", length = 4000)
    private String jsonAntes;

    @Column(name = "json_depois", length = 4000)
    private String jsonDepois;

    @Column(name = "realizado_por", nullable = false, columnDefinition = "VARCHAR2(36)")
    private UUID realizadoPor;

    @CreatedDate
    @Column(name = "data_hora", updatable = false)
    private LocalDateTime dataHora;
}