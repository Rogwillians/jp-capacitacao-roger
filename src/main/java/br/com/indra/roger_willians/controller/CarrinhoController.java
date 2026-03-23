package br.com.indra.roger_willians.controller;


import br.com.indra.roger_willians.model.Carrinho;
import br.com.indra.roger_willians.service.CarrinhoService;
import br.com.indra.roger_willians.service.dto.AtualizarQuantidadeDTO;
import br.com.indra.roger_willians.service.dto.CarrinhoResponseDTO;
import br.com.indra.roger_willians.service.dto.ItemCarrinhoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/carrinhos")
@RequiredArgsConstructor
public class CarrinhoController {

    private final CarrinhoService carrinhoService;


    @GetMapping
    public ResponseEntity<CarrinhoResponseDTO> visualizarCarrinho(
            @RequestHeader("X-Usuario-Id") UUID usuarioId) {

        Carrinho carrinho = carrinhoService.obterOuCriarCarrinhoAtivo(usuarioId);

        CarrinhoResponseDTO response = carrinhoService.converterParaDTO(carrinho);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/itens")
    public ResponseEntity<CarrinhoResponseDTO> adicionarItem(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @Valid @RequestBody ItemCarrinhoDTO dto) {

        CarrinhoResponseDTO carrinhoAtualizado = carrinhoService.adicionarItem(usuarioId, dto);

        return ResponseEntity.ok(carrinhoAtualizado);
    }

    @PutMapping("/itens/{itemId}")
    public ResponseEntity<CarrinhoResponseDTO> atualizarQuantidadeItem(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @PathVariable UUID itemId,
            @Valid @RequestBody AtualizarQuantidadeDTO dto) {

        CarrinhoResponseDTO carrinhoAtualizado = carrinhoService.atualizarQuantidadeItem(usuarioId, itemId, dto.quantidade());
        return ResponseEntity.ok(carrinhoAtualizado);
    }

    @DeleteMapping("/itens/{itemId}")
    public ResponseEntity<CarrinhoResponseDTO> removerItem(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @PathVariable UUID itemId) {

        CarrinhoResponseDTO carrinhoAtualizado = carrinhoService.removerItem(usuarioId, itemId);
        return ResponseEntity.ok(carrinhoAtualizado);
    }
}