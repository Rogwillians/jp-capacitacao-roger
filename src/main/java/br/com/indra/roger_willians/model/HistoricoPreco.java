package br.com.indra.roger_willians.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "historico_preco")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoPreco {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "VARCHAR2(36)")
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Column(name = "preco_antigo")
    private BigDecimal precoAntigo;

    @Column(name = "preco_novo")
    private BigDecimal precoNovo;

    @Column(name = "data_alteracao", updatable = false)
    @CreatedDate
    private LocalDateTime dataAlteracao;
}
