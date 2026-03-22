package br.com.indra.roger_willians.service;

import br.com.indra.roger_willians.model.HistoricoPreco;
import br.com.indra.roger_willians.model.Produto;
import br.com.indra.roger_willians.repository.HistoricoPrecoRepository;
import br.com.indra.roger_willians.service.dto.HistoricoPrecoDTO;
import br.com.indra.roger_willians.service.dto.HistoricoPrecoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoricoPrecoService {
    private final HistoricoPrecoRepository historicoPrecoRepository;

    public void registrarHistorico(Produto produto, BigDecimal precoAntigo, BigDecimal precoNovo) {

        HistoricoPrecoDTO dto = new HistoricoPrecoDTO(
                null,
                produto.getId(),
                precoAntigo,
                precoNovo,
                LocalDateTime.now()
        );


        HistoricoPreco historico = converterParaEntidade(dto, produto);

        historicoPrecoRepository.save(historico);
    }

    public List<HistoricoPrecoResponseDTO> buscarHistoricoPorProduto(UUID produtoId) {

        List<HistoricoPreco> historicos = historicoPrecoRepository.findByProdutoIdOrderByDataAlteracaoDesc(produtoId);

        return historicos.stream()
                .map(this::converterParaDTO)
                .toList();
    }


    private HistoricoPrecoResponseDTO converterParaDTO(HistoricoPreco historico) {
        return new HistoricoPrecoResponseDTO(
                historico.getId(),
                historico.getPrecoAntigo(),
                historico.getPrecoNovo(),
                historico.getDataAlteracao()
        );
    }

    private HistoricoPreco converterParaEntidade(HistoricoPrecoDTO dto, Produto produtoExistente) {


        HistoricoPreco historicoPreco = new HistoricoPreco();

        historicoPreco.setProduto(produtoExistente);
        historicoPreco.setPrecoAntigo(dto.precoAntigo());
        historicoPreco.setPrecoNovo(dto.precoNovo());
        historicoPreco.setDataAlteracao(dto.dataAlteracao());

        return historicoPreco;
    }
}
