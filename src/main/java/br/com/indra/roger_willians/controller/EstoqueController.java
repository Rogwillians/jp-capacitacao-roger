package br.com.indra.roger_willians.controller;


import br.com.indra.roger_willians.service.dto.TransacaoEstoqueResponseDTO;
import br.com.indra.roger_willians.service.EstoqueService;
import br.com.indra.roger_willians.service.dto.TransacaoEstoqueDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.OK)
    public TransacaoEstoqueResponseDTO adicionarEstoque(
            @PathVariable UUID produtoId,
            @Valid @RequestBody TransacaoEstoqueDTO dto) {

        return estoqueService.adicionarEstoque(produtoId, dto);
    }

    @PostMapping("/remover/{produtoId}")
    @ResponseStatus(HttpStatus.OK)
    public TransacaoEstoqueResponseDTO removerEstoque(
            @PathVariable UUID produtoId,
            @Valid @RequestBody TransacaoEstoqueDTO dto) {

        return estoqueService.removerEstoque(produtoId, dto);
    }

    @GetMapping("/{produtoId}")
    @ResponseStatus(HttpStatus.OK)
    public List<TransacaoEstoqueResponseDTO> buscarHistoricoEstoque(@PathVariable UUID produtoId) {
        return estoqueService.buscarHistorico(produtoId);
    }
}