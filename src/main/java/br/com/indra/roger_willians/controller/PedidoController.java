package br.com.indra.roger_willians.controller;

import br.com.indra.roger_willians.service.dto.PedidoDTO;
import br.com.indra.roger_willians.service.dto.PedidoResponseDTO;
import br.com.indra.roger_willians.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/criar")
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO criarPedido(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @Valid @RequestBody PedidoDTO dto) {

        return pedidoService.criarPedido(usuarioId, dto);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PedidoResponseDTO buscarPedido(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @PathVariable UUID id) {

        return pedidoService.buscarPorId(id, usuarioId);
    }

    @PostMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public PedidoResponseDTO cancelarPedido(
            @RequestHeader("X-Usuario-Id") UUID usuarioId,
            @PathVariable UUID id) {

        return pedidoService.cancelarPedido(id, usuarioId);
    }
}