package br.com.indra.roger_willians.model;

import br.com.indra.roger_willians.model.enums.TipoAplicacaoCupom;
import br.com.indra.roger_willians.model.enums.TipoDesconto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "promocoes")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promocao {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "VARCHAR2(36)")
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoDesconto tipo;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDateTime dataFim;

    @Column(name = "limite_uso", nullable = false)
    private Integer limiteUso;

    @Column(name = "contagem_uso", nullable = false)
    private Integer contagemUso = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_aplicacao", nullable = false)
    private TipoAplicacaoCupom tipoAplicacao = TipoAplicacaoCupom.CARRINHO;

    @Column(name = "produto_aplicavel_id", columnDefinition = "VARCHAR2(36)")
    private UUID produtoAplicavelId;

    @Column(name = "categoria_aplicavel_id", columnDefinition = "VARCHAR2(36)")
    private UUID categoriaAplicavelId;

    public boolean isValidoNoMomento(LocalDateTime agora) {
        return !agora.isBefore(dataInicio) && !agora.isAfter(dataFim);
    }
}