package br.com.indra.roger_willians.service;

import br.com.indra.roger_willians.model.HistoricoPreco;
import br.com.indra.roger_willians.model.Produtos;
import br.com.indra.roger_willians.repository.HistoricoPrecoRepository;
import br.com.indra.roger_willians.service.dto.HistoricoPrecoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoricoPrecoService {
    private final HistoricoPrecoRepository historicoPrecoRepository;

    public void registrarHistorico(Produtos produto, BigDecimal precoOld, BigDecimal precoNew) {

        HistoricoPreco historico = new HistoricoPreco();
        historico.setProdutos(produto);
        historico.setPrecoAntigo(precoOld);
        historico.setPrecoNovo(precoNew);
        historico.setDataAlteracao(LocalDateTime.now());


        historicoPrecoRepository.save(historico);
    }

    public HistoricoPrecoDTO getHistoricoPrecoByProdutoId(UUID produtoId){
        Set<HistoricoPreco> historicoPreco = historicoPrecoRepository.findByProdutosId(produtoId)
                .stream().flatMap().toList();

        return historicos.stream()
                .map(historico -> new HistoricoPrecoDTO(
                        historico.getId(),
                        historico.getProduto().getId(),
                        historico.getPrecoAntigo(),
                        historico.getPrecoNovo(),
                        historico.getDataAlteracao()
                ))
                .toList();
    }
}
