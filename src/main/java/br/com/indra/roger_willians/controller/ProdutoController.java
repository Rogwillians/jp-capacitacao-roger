package br.com.indra.roger_willians.controller;

import br.com.indra.roger_willians.model.Produto;
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
    public ResponseEntity<List<ProdutoResponseDTO>> getAll(){

        return ResponseEntity.ok(produtoService.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> getById(@PathVariable UUID id){
        return ResponseEntity.ok(produtoService.findById(id));
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
    public ResponseEntity<List<ProdutoResponseDTO>> buscarPorFaixaDePreco(
            @RequestParam(name = "min") BigDecimal precoMin,
            @RequestParam(name = "max") BigDecimal precoMax) {

        List<ProdutoResponseDTO> listaProdutos = produtoService.buscarPorFaixaDePreco(precoMin, precoMax);


        return ResponseEntity.ok(listaProdutos);
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ProdutoResponseDTO> cadastrarProduto(@Valid @RequestBody ProdutoDTO dto){

        ProdutoResponseDTO produtoSalvo = produtoService.cadastrarProduto(dto);


        return ResponseEntity.status(HttpStatus.CREATED).body(produtoSalvo);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(
            @PathVariable UUID id,
            @Valid @RequestBody ProdutoDTO dto) {


        ProdutoResponseDTO produtoAtualizado = produtoService.atualizarProduto(id, dto);

        return ResponseEntity.ok(produtoAtualizado);
    }

    @PatchMapping("/atualiza-preco/{id}")
    public ResponseEntity<Produto> atualizarPreco(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarPrecoDTO dto) {


        Produto produtoAtualizado = produtoService.atualizarPreco(id, dto.preco());

        return ResponseEntity.ok(produtoAtualizado);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable UUID id) {

        produtoService.deletarProduto(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/inativar/{id}")
    public ResponseEntity<Void> inativarProduto(@PathVariable UUID id) {
        produtoService.inativarProduto(id);
        return ResponseEntity.noContent().build();
    }


}
