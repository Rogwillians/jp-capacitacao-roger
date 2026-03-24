package br.com.indra.roger_willians.controller;

import br.com.indra.roger_willians.service.dto.PedidoDTO;
import br.com.indra.roger_willians.service.dto.PedidoResponseDTO;
import br.com.indra.roger_willians.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/criar")
    public ResponseEntity<PedidoResponseDTO> criarPedido(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @Valid @RequestBody PedidoDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.criarPedido(usuarioId, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPedido(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @PathVariable UUID id) {

        return ResponseEntity.ok(pedidoService.buscarPorId(id, usuarioId));
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @PathVariable UUID id) {

        return ResponseEntity.ok(pedidoService.cancelarPedido(id, usuarioId));
    }
}