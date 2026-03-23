package br.com.indra.roger_willians.model;

import br.com.indra.roger_willians.model.enums.TipoTransacao;
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

@Table(name = "transacao_estoque")
@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoEstoque {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "VARCHAR2(36)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "produto_id",nullable = false , columnDefinition = "VARCHAR2(36)")
    private Produto produto;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transacao", nullable = false)
    private TipoTransacao tipoTransacao;

    @Column(length = 255)
    private String motivo;

    @Column(name = "referencia_id", columnDefinition = "VARCHAR2(36)")
    private UUID referenciaId;

    @Column(name = "criado_por")
    private String criadoPor;

    @CreatedDate
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

}
