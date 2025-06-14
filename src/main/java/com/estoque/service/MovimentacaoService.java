package com.estoque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.estoque.model.*;
import com.estoque.repository.MovimentacaoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimentacaoService {
    @Autowired
    private MovimentacaoRepository movimentacaoRepository;
    
    @Autowired
    private ProdutoService produtoService;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Movimentacao registrarMovimentacao(Movimentacao movimentacao) {
        // Carrega o produto completo do banco, incluindo a categoria
        Produto produto = produtoService.buscarProduto(movimentacao.getProduto().getId());
        
        // Define a data/hora da movimentação
        movimentacao.setDataHora(LocalDateTime.now());
        
        // Atualiza a quantidade do produto
        int quantidadeAtual = produto.getQuantidadeAtual() != null ? produto.getQuantidadeAtual() : 0;
        int novaQuantidade;
        
        if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA) {
            novaQuantidade = quantidadeAtual + movimentacao.getQuantidade();
        } else {
            if (quantidadeAtual < movimentacao.getQuantidade()) {
                throw new IllegalArgumentException("Quantidade insuficiente em estoque");
            }
            novaQuantidade = quantidadeAtual - movimentacao.getQuantidade();
        }
        
        // Atualiza a quantidade do produto
        produto.setQuantidadeAtual(novaQuantidade);
        
        // Primeiro atualiza o produto
        produto = produtoService.atualizarProduto(produto);
        
        // Garante que o produto está atualizado na movimentação
        movimentacao.setProduto(produto);
        
        // Força o flush das alterações do produto
        entityManager.flush();
        
        // Salva a movimentação
        return movimentacaoRepository.save(movimentacao);
    }

    public List<Movimentacao> listarMovimentacoes() {
        return movimentacaoRepository.findAll();
    }

    public List<Movimentacao> listarMovimentacoesPorProduto(Long produtoId) {
        return movimentacaoRepository.findByProdutoId(produtoId);
    }
} 