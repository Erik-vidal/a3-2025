package com.estoque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.estoque.model.Categoria;
import com.estoque.repository.CategoriaRepository;
import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
    }

    public Categoria salvarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public Categoria atualizarCategoria(Categoria categoria) {
        if (categoria.getId() == null) {
            throw new IllegalArgumentException("ID da categoria não pode ser nulo");
        }
        return categoriaRepository.save(categoria);
    }

    public void excluirCategoria(Long id) {
        categoriaRepository.deleteById(id);
    }
} 