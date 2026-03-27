package br.com.indra.roger_willians.controller;

import br.com.indra.roger_willians.dto.CategoriaDTO;
import br.com.indra.roger_willians.dto.CategoriaResponseDTO;
import br.com.indra.roger_willians.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDTO cadastrarCategoria(@Valid @RequestBody CategoriaDTO dto,
                                                   @RequestParam UUID usuarioId ) {

        return categoriaService.cadastrarCategoria(dto, usuarioId);
    }

    @GetMapping("/listar")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaResponseDTO> findAll() {
        return categoriaService.findAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoriaResponseDTO findById(@PathVariable UUID id) {
        return categoriaService.findById(id);
    }

    @PutMapping("atualizar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CategoriaResponseDTO atualizarCategoria(@PathVariable UUID id,
                                                   @RequestParam UUID usuarioId,
                                                   @Valid @RequestBody CategoriaDTO dto) {

        return categoriaService.atualizarCategoria(id, usuarioId,dto);
    }

    @DeleteMapping("/deletar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarCategoria(@PathVariable UUID id,
                                 @RequestParam UUID usuarioId) {
        categoriaService.deletarCategoria(id, usuarioId);
    }
}
