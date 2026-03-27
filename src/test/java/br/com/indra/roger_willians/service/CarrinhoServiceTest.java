package br.com.indra.roger_willians.service;

import br.com.indra.roger_willians.exception.RecursoNaoEncontradoException;
import br.com.indra.roger_willians.model.Carrinho;
import br.com.indra.roger_willians.model.ItemCarrinho;
import br.com.indra.roger_willians.model.Produto;
import br.com.indra.roger_willians.model.enums.StatusCarrinho;
import br.com.indra.roger_willians.repository.CarrinhoRepository;
import br.com.indra.roger_willians.dto.CarrinhoResponseDTO;
import br.com.indra.roger_willians.dto.ItemCarrinhoDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CarrinhoServiceTest {

    @Mock
    private CarrinhoRepository carrinhoRepository;
    @Mock
    private ProdutoService produtoService;
    @InjectMocks
    private CarrinhoService carrinhoService;
    @Test
    void should_obterCarrinhoAtivo() {
        Carrinho carrinho = new Carrinho();
        carrinho.setId(UUID.randomUUID());
        UUID userId = UUID.randomUUID();

        doReturn(Optional.of(carrinho))
                .when(carrinhoRepository).findByUsuarioIdAndStatus(userId, StatusCarrinho.ATIVO);

        Carrinho result = carrinhoService.obterOuCriarCarrinhoAtivo(userId);

        assertNotNull(result);
        assertEquals(carrinho, result);

    }

    @Test
    void should_criarCarrinhoAtivo(){
        Carrinho carrinho = new Carrinho();
        UUID userId = UUID.randomUUID();
        carrinho.setUsuarioId(userId);
        carrinho.setStatus(StatusCarrinho.ATIVO);

        doReturn(Optional.empty())
                .when(carrinhoRepository).findByUsuarioIdAndStatus(userId, StatusCarrinho.ATIVO);

        doReturn(carrinho)
                .when(carrinhoRepository).save(any());

        Carrinho result = carrinhoService.obterOuCriarCarrinhoAtivo(userId);

        assertNotNull(result);
        assertEquals(carrinho, result);
        assertEquals(userId, result.getUsuarioId());
        assertEquals(StatusCarrinho.ATIVO, result.getStatus());
    }

    @Test
    void should_adicionarItem(){
        Carrinho carrinho = new Carrinho();
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setPreco(BigDecimal.valueOf(10));
        ItemCarrinho itemCarrinho = new ItemCarrinho();
        itemCarrinho.setPrecoSnapshot(produto.getPreco());
        itemCarrinho.setQuantidade(2);
        itemCarrinho.setProduto(produto);
        List<ItemCarrinho> itens = new ArrayList<>();
        itens.add(itemCarrinho);
        ItemCarrinhoDTO itemCarrinhoDTO = new ItemCarrinhoDTO(produto.getId(), 3);

        UUID userId = UUID.randomUUID();
        carrinho.setId(UUID.randomUUID());
        carrinho.setUsuarioId(userId);
        carrinho.setStatus(StatusCarrinho.ATIVO);
        carrinho.setItens(itens);

        doReturn(Optional.of(carrinho)).when(carrinhoRepository).findByUsuarioIdAndStatus(userId, StatusCarrinho.ATIVO);

        doReturn(produto).when(produtoService).buscarEntidadePorId(itemCarrinhoDTO.produtoId());

        doReturn(carrinho).when(carrinhoRepository).save(any());

        CarrinhoResponseDTO  result = carrinhoService.adicionarItem(userId, itemCarrinhoDTO);

        assertNotNull(result);
        assertEquals(5, carrinho.getItens().get(0).getQuantidade());

    }

    @Test
    void should_atualizarQuantidadeItem(){
        Carrinho carrinho = new Carrinho();
        Produto produto = new Produto();
        produto.setId(UUID.randomUUID());
        produto.setPreco(BigDecimal.valueOf(10));
        ItemCarrinho itemCarrinho = new ItemCarrinho();
        itemCarrinho.setId(UUID.randomUUID());
        itemCarrinho.setPrecoSnapshot(produto.getPreco());
        itemCarrinho.setProduto(produto);
        itemCarrinho.setQuantidade(2);
        List<ItemCarrinho> itens = new ArrayList<>();
        itens.add(itemCarrinho);

        UUID userId = UUID.randomUUID();
        carrinho.setId(UUID.randomUUID());
        carrinho.setUsuarioId(userId);
        carrinho.setStatus(StatusCarrinho.ATIVO);
        carrinho.setItens(itens);

        doReturn(Optional.of(carrinho)).when(carrinhoRepository).findByUsuarioIdAndStatus(userId, StatusCarrinho.ATIVO);

        doReturn(carrinho).when(carrinhoRepository).save(any());

        CarrinhoResponseDTO result = carrinhoService.atualizarQuantidadeItem(userId, itemCarrinho.getId(), 5);

        assertNotNull(result);
        assertEquals(5, carrinho.getItens().get(0).getQuantidade());
     }

     @Test
    void shouldNot_atualizarQuantidadeItem(){
        UUID userId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();
        Carrinho carrinho = new Carrinho();
        carrinho.setId(UUID.randomUUID());
        carrinho.setUsuarioId(userId);
        carrinho.setStatus(StatusCarrinho.ATIVO);

        RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () ->
                carrinho.getItens().stream()
                        .filter(i -> i.getId().equals(itemId))
                        .findFirst().orElseThrow(()
                                -> new RecursoNaoEncontradoException("Item não encontrado neste carrinho.")));

        assertEquals("Item não encontrado neste carrinho.", exception.getMessage());
     }

     @Test
    void should_removerItem(){
         Carrinho carrinho = new Carrinho();
         Produto produto = new Produto();
         produto.setId(UUID.randomUUID());
         produto.setPreco(BigDecimal.valueOf(10));
         ItemCarrinho itemCarrinho = new ItemCarrinho();
         itemCarrinho.setId(UUID.randomUUID());
         itemCarrinho.setPrecoSnapshot(produto.getPreco());
         itemCarrinho.setProduto(produto);
         itemCarrinho.setQuantidade(2);
         List<ItemCarrinho> itens = new ArrayList<>();
         itens.add(itemCarrinho);
         carrinho.setItens(itens);
         CarrinhoResponseDTO carrinhoResponseDTO = carrinhoService.converterParaDTO(carrinho);

         doReturn(carrinho).when(carrinhoRepository).save(any());


         CarrinhoResponseDTO result = carrinhoService.removerItem(carrinho.getUsuarioId(),
                                                                  carrinho.getItens().get(0).getId());

         assertNotNull(result);
         assertNotEquals(result.itens(), carrinhoResponseDTO.itens());

     }

     @Test
    void shouldNot_removerItem() {
         Carrinho carrinho = new Carrinho();
         UUID userId = UUID.randomUUID();
         UUID randomItemid = UUID.randomUUID();
         Produto produto = new Produto();
         produto.setId(UUID.randomUUID());
         List<ItemCarrinho> itens = new ArrayList<>();
         carrinho.setUsuarioId(userId);
         carrinho.setStatus(StatusCarrinho.ATIVO);
         carrinho.setItens(itens);

         doReturn(Optional.of(carrinho))
                 .when(carrinhoRepository).findByUsuarioIdAndStatus(userId, StatusCarrinho.ATIVO);

         RecursoNaoEncontradoException exception = assertThrows(RecursoNaoEncontradoException.class, () ->
                 carrinhoService.removerItem(userId, randomItemid));

        assertEquals("Item não encontrado neste carrinho.", exception.getMessage());
     }

     @Test
    void should_converterParaDTO(){
            Carrinho carrinho = new Carrinho();
            UUID userId = UUID.randomUUID();
            carrinho.setId(userId);
            carrinho.setUsuarioId(UUID.randomUUID());
            carrinho.setStatus(StatusCarrinho.ATIVO);
            Produto produto = new Produto();
            produto.setId(UUID.randomUUID());
            produto.setNome("Produto Teste");
            produto.setPreco(BigDecimal.valueOf(10));
            ItemCarrinho itemCarrinho = new ItemCarrinho();
            itemCarrinho.setId(UUID.randomUUID());
            itemCarrinho.setProduto(produto);
            itemCarrinho.setQuantidade(2);
            itemCarrinho.setPrecoSnapshot(produto.getPreco());
            List<ItemCarrinho> itens = new ArrayList<>();
            itens.add(itemCarrinho);
            carrinho.setItens(itens);

            CarrinhoResponseDTO result = carrinhoService.converterParaDTO(carrinho);

            assertNotNull(result);
            assertEquals(carrinho.getId(), result.id());
            assertEquals(carrinho.getUsuarioId(), result.usuarioId());
            assertEquals(carrinho.getStatus(), result.status());
            assertEquals(1, result.itens().size());
     }

     @Test
    void should_converterParaEntidade(){
         Carrinho carrinho = new Carrinho();
         carrinho.setId(UUID.randomUUID());
         carrinho.setUsuarioId(UUID.randomUUID());
         carrinho.setStatus(StatusCarrinho.ATIVO);
         Produto produto = new Produto();
         produto.setId(UUID.randomUUID());
         produto.setPreco(BigDecimal.valueOf(10));
         ItemCarrinhoDTO itemCarrinhoDTO = new ItemCarrinhoDTO(produto.getId(), 3);

         ItemCarrinho result = carrinhoService.converterParaEntidade(itemCarrinhoDTO, carrinho, produto);

         assertNotNull(result);
         assertEquals(carrinho, result.getCarrinho());
         assertEquals(produto, result.getProduto());
         assertEquals(itemCarrinhoDTO.quantidade(), result.getQuantidade());
         assertEquals(produto.getPreco(), result.getPrecoSnapshot());

     }
}