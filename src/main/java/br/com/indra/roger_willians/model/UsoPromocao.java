package br.com.indra.roger_willians.model;

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

@Table(name = "uso_promocoes")
@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsoPromocao {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "VARCHAR2(36)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promocao_id", nullable = false, columnDefinition = "VARCHAR2(36)")
    private Promocao promocao;

    @Column(name = "usuario_id", nullable = false, columnDefinition = "VARCHAR2(36)")
    private UUID usuarioId;

    @Column(name = "pedido_id", nullable = false, columnDefinition = "VARCHAR2(36)")
    private UUID pedidoId;

    @CreatedDate
    @Column(name = "data_uso", updatable = false)
    private LocalDateTime dataUso;
}