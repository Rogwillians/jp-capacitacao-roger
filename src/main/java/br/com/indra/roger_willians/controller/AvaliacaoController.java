package br.com.indra.roger_willians.controller;

import br.com.indra.roger_willians.service.dto.AvaliacaoDTO;
import br.com.indra.roger_willians.service.dto.AvaliacaoResponseDTO;
import br.com.indra.roger_willians.service.AvaliacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/avaliacao")
@RequiredArgsConstructor
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvaliacaoResponseDTO criarAvaliacao(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @Valid @RequestBody AvaliacaoDTO dto) {

        return avaliacaoService.criarAvaliacao(usuarioId, dto);
    }

    @GetMapping("/produto/{produtoId}")
    @ResponseStatus(HttpStatus.OK)
    public List<AvaliacaoResponseDTO> listarAvaliacoesPorProduto(
            @PathVariable UUID produtoId) {

        return avaliacaoService.listarPorProduto(produtoId);
    }
}