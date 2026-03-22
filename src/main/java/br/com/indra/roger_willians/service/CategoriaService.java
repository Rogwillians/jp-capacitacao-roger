package br.com.indra.roger_willians.service;

import br.com.indra.roger_willians.model.Categoria;
import br.com.indra.roger_willians.repository.CategoriaRepository;
import br.com.indra.roger_willians.service.dto.CategoriaDTO;
import br.com.indra.roger_willians.service.dto.CategoriaResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriaResponseDTO cadastrarCategoria(CategoriaDTO dto) {
        if (categoriaRepository.existsByNome(dto.nome())) {
            throw new IllegalArgumentException("Já existe uma categoria com o nome: " + dto.nome());
        }

        Categoria categoria = converterParaEntidade(dto);
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        return converterParaDTO(categoriaSalva);
    }

    public List<CategoriaResponseDTO> findAll() {
        return categoriaRepository.findAll().stream()
                .map(this::converterParaDTO)
                .toList();
    }

    public CategoriaResponseDTO findById(UUID id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada."));
        return converterParaDTO(categoria);
    }

    public Categoria buscarEntidadePorId(UUID id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada com o ID: " + id));
    }


    private Categoria converterParaEntidade(CategoriaDTO dto) {
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());

        if (dto.categoriaPaiId() != null) {
            Categoria pai = categoriaRepository.findById(dto.categoriaPaiId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria Pai não encontrada."));
            categoria.setCategoriaPai(pai);
        }

        return categoria;
    }

    private CategoriaResponseDTO converterParaDTO(Categoria categoria) {

        UUID paiId = (categoria.getCategoriaPai() != null) ? categoria.getCategoriaPai().getId() : null;

        return new CategoriaResponseDTO(
                categoria.getId(),
                categoria.getNome(),
                paiId
        );
    }
}

