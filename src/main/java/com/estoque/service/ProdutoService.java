package com.estoque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.estoque.model.Produto;
import com.estoque.repository.ProdutoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Hibernate;
import java.util.List;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public List<Produto> listarProdutos() {
        return produtoRepository.findAllWithCategoriaOrderByNome();
    }

    public List<Produto> listarTodos() {
        return listarProdutos();
    }

    public Produto buscarProduto(Long id) {
        return produtoRepository.findByIdWithCategoria(id)
            .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public Produto salvarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto salvar(Produto produto) {
        return salvarProduto(produto);
    }

    @Transactional
    public Produto atualizarProduto(Produto produto) {
        if (produto.getId() == null) {
            throw new IllegalArgumentException("ID do produto não pode ser nulo");
        }
        
        // Garante que a categoria está carregada antes de atualizar
        Produto existente = buscarProduto(produto.getId());
        
        // Mantém os valores que não devem ser alterados
        if (produto.getCategoria() == null) {
            produto.setCategoria(existente.getCategoria());
        }
        if (produto.getNome() == null) {
            produto.setNome(existente.getNome());
        }
        if (produto.getUnidadeMedida() == null) {
            produto.setUnidadeMedida(existente.getUnidadeMedida());
        }
        if (produto.getPrecoUnitario() == null) {
            produto.setPrecoUnitario(existente.getPrecoUnitario());
        }
        if (produto.getQuantidadeMinima() == null) {
            produto.setQuantidadeMinima(existente.getQuantidadeMinima());
        }
        if (produto.getQuantidadeMaxima() == null) {
            produto.setQuantidadeMaxima(existente.getQuantidadeMaxima());
        }
        
        // Salva o produto atualizado
        return produtoRepository.save(produto);
    }

    public void excluirProduto(Long id) {
        produtoRepository.deleteById(id);
    }
} 