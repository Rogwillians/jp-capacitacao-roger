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

@Entity
@Table(name = "avaliacoes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Avaliacao {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name = "produto_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID produtoId;

    @Column(name = "usuario_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID usuarioId;

    @Column(name = "pedido_id", nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID pedidoId;

    @Column(nullable = false)
    private Integer nota;

    @Column(length = 2000)
    private String comentario;

    @CreatedDate
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;
}