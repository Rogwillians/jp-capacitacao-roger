package br.com.indra.roger_willians.controller;


import br.com.indra.roger_willians.service.dto.TransacaoEstoqueResponseDTO;
import br.com.indra.roger_willians.service.EstoqueService;
import br.com.indra.roger_willians.service.dto.TransacaoEstoqueDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping("/adicionar/{produtoId}")
    public ResponseEntity<TransacaoEstoqueResponseDTO> adicionarEstoque(
            @PathVariable UUID produtoId,
            @Valid @RequestBody TransacaoEstoqueDTO dto) {

        TransacaoEstoqueResponseDTO transacao = estoqueService.adicionarEstoque(produtoId, dto);
        return ResponseEntity.ok(transacao);
    }

    @PostMapping("/remover/{produtoId}")
    public ResponseEntity<TransacaoEstoqueResponseDTO> removerEstoque(
            @PathVariable UUID produtoId,
            @Valid @RequestBody TransacaoEstoqueDTO dto) {

        TransacaoEstoqueResponseDTO transacao = estoqueService.removerEstoque(produtoId, dto);
        return ResponseEntity.ok(transacao);
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<List<TransacaoEstoqueResponseDTO>> buscarHistoricoEstoque(@PathVariable UUID produtoId) {
        return ResponseEntity.ok(estoqueService.buscarHistorico(produtoId));
    }
}