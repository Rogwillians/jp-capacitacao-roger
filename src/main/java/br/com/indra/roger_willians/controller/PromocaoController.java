package br.com.indra.roger_willians.controller;

import br.com.indra.roger_willians.service.dto.PromocaoDTO;
import br.com.indra.roger_willians.model.Promocao;
import br.com.indra.roger_willians.service.PromocaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/promocao")
@RequiredArgsConstructor
public class PromocaoController {

    private final PromocaoService promocaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Promocao criarPromocao(@Valid @RequestBody PromocaoDTO dto) {

        return promocaoService.criarPromocao(dto);
    }
}