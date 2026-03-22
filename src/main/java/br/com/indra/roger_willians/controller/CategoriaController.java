package br.com.indra.roger_willians.controller;

import br.com.indra.roger_willians.service.dto.CategoriaDTO;
import br.com.indra.roger_willians.service.dto.CategoriaResponseDTO;
import br.com.indra.roger_willians.service.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<CategoriaResponseDTO> cadastrarCategoria(@Valid @RequestBody CategoriaDTO dto) {
        CategoriaResponseDTO categoriaSalva = categoriaService.cadastrarCategoria(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<CategoriaResponseDTO>> findAll() {
        return ResponseEntity.ok(categoriaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoriaService.findById(id));
    }
}
