package br.com.indra.roger_willians.controller;

import br.com.indra.roger_willians.service.CarrinhoService;
import br.com.indra.roger_willians.service.dto.AtualizarQuantidadeDTO;
import br.com.indra.roger_willians.service.dto.CarrinhoResponseDTO;
import br.com.indra.roger_willians.service.dto.ItemCarrinhoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carrinhos")
@RequiredArgsConstructor
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CarrinhoResponseDTO visualizarCarrinho(
            @RequestHeader("X-Usuario-Id") UUID usuarioId) {

        return carrinhoService.converterParaDTO(
                carrinhoService.obterOuCriarCarrinhoAtivo(usuarioId));
    }

    @PostMapping("/itens")
    @ResponseStatus(HttpStatus.OK)
    public CarrinhoResponseDTO adicionarItem(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @Valid @RequestBody ItemCarrinhoDTO dto) {

        return carrinhoService.adicionarItem(usuarioId, dto);
    }

    @PutMapping("/itens/{itemId}")
    public CarrinhoResponseDTO atualizarQuantidadeItem(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @PathVariable UUID itemId,
            @Valid @RequestBody AtualizarQuantidadeDTO dto) {

        return carrinhoService.atualizarQuantidadeItem(usuarioId, itemId, dto.quantidade());
    }

    @DeleteMapping("/itens/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public CarrinhoResponseDTO removerItem(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @PathVariable UUID itemId) {

        return carrinhoService.removerItem(usuarioId, itemId);
    }
}