package br.com.indra.roger_willians.controller;

import br.com.indra.roger_willians.model.Produtos;
import br.com.indra.roger_willians.service.ProdutosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos")
public class ProdutosController {

    private final ProdutosService produtosService;


    @GetMapping("/listar")
    public ResponseEntity<List<Produtos>> getAll(){
        return ResponseEntity.ok(produtosService.findAll());
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<Produtos> getById(@PathVariable UUID id){
        return ResponseEntity.ok(produtosService.findById(id));
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<Produtos> cadastrarProduto(@RequestBody Produtos produto){

        Produtos novoProduto = produtosService.cadastrarProduto(produto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(novoProduto.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Produtos> atualizarProduto(@PathVariable UUID id, @RequestBody Produtos produto){
        return ResponseEntity.ok(produtosService.atualizarProduto(produto));
    }

    @PatchMapping("/atualiza-preco/{id}")
    public ResponseEntity<Produtos> atualizarPreco(@PathVariable UUID id, @RequestParam BigDecimal preco){

        return ResponseEntity.ok(produtosService.atualizarPreco(id, preco));
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable UUID id) {

        produtosService.deletarProduto(id);

        return ResponseEntity.noContent().build();
    }
}
