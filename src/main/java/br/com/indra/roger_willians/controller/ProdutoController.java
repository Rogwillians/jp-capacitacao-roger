package br.com.indra.roger_willians.controller;

import br.com.indra.roger_willians.service.ProdutoService;
import br.com.indra.roger_willians.service.dto.AtualizarPrecoDTO;
import br.com.indra.roger_willians.service.dto.ProdutoDTO;
import br.com.indra.roger_willians.service.dto.ProdutoResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProdutoResponseDTO> getAll(){

        return produtoService.findAll();
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProdutoResponseDTO getById(@PathVariable UUID id){
        return produtoService.findById(id);
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorCategoria(@PathVariable UUID categoriaId) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorCategoria(categoriaId);

        if(produtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarAtivo() {
        List<ProdutoResponseDTO> produtosAtivos = produtoService.buscarAtivo();

        if (produtosAtivos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(produtosAtivos);
    }

    @GetMapping("/inativos")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarInativo() {
        List<ProdutoResponseDTO> produtosInativos = produtoService.buscarInativo();

        if (produtosInativos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(produtosInativos);
    }

    @GetMapping("/por-nome")
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorNomeContendo(@RequestParam String nome) {
        List<ProdutoResponseDTO> produtos = produtoService.buscarPorNomeContendo(nome);

        if (produtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/pouco-estoque")
    public ResponseEntity<List<ProdutoResponseDTO>> listarPoucoEstoque(
            @RequestParam(value = "quantidade", defaultValue = "10") Integer quantidade) {

        List<ProdutoResponseDTO> produtosComEstoqueBaixo = produtoService.buscarPoucoEstoque(quantidade);

        if (produtosComEstoqueBaixo.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(produtosComEstoqueBaixo);
    }

    @GetMapping("/faixa-preco")
    @ResponseStatus(HttpStatus.OK)
    public List<ProdutoResponseDTO> buscarPorFaixaDePreco(
            @RequestParam(name = "min") BigDecimal precoMin,
            @RequestParam(name = "max") BigDecimal precoMax) {

        return produtoService.buscarPorFaixaDePreco(precoMin, precoMax);
    }

    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoResponseDTO cadastrarProduto(@Valid @RequestBody ProdutoDTO dto){

        return produtoService.cadastrarProduto(dto);
    }

    @PutMapping("/atualizar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProdutoResponseDTO atualizarProduto(
            @PathVariable UUID id,
            @Valid @RequestBody ProdutoDTO dto) {

        return produtoService.atualizarProduto(id, dto);
    }

    @PatchMapping("/atualiza-preco/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProdutoResponseDTO atualizarPreco(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarPrecoDTO dto) {

        return produtoService.atualizarPreco(id, dto.preco());
    }

    @DeleteMapping("/deletar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarProduto(@PathVariable UUID id) {

        produtoService.deletarProduto(id);
    }

    @DeleteMapping("/inativar/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativarProduto(@PathVariable UUID id) {
        produtoService.inativarProduto(id);
    }

    @PatchMapping("/reativar/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void reativarProduto(@PathVariable UUID id) {
        produtoService.reativarProduto(id);
    }


}
