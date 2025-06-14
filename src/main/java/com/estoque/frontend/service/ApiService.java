package com.estoque.frontend.service;

import com.estoque.dto.*;
import com.estoque.model.Categoria;
import com.estoque.model.Movimentacao;
import com.estoque.model.Produto;
import java.math.BigDecimal;
import java.util.List;

public interface ApiService {
    // Produtos
    List<Produto> listarProdutos();
    Produto buscarProduto(Long id);
    Produto salvarProduto(Produto produto);
    Produto atualizarProduto(Produto produto);
    void excluirProduto(Long id);

    // Movimentações
    List<Movimentacao> listarMovimentacoes();
    Movimentacao realizarMovimentacao(Movimentacao movimentacao);

    // Categorias
    List<Categoria> listarCategorias();
    Categoria criarCategoria(Categoria categoria);

    // Reajuste
    void reajustarPrecos(BigDecimal percentual);

    // Relatórios
    List<ListaPrecoDTO> gerarListaPrecos();
    List<BalancoDTO> gerarBalanco();
    List<ProdutoQuantidadeDTO> gerarRelatorioProdutosAbaixoMinimo();
    List<ProdutoQuantidadeDTO> gerarRelatorioProdutosAcimaMaximo();
    List<CategoriaProdutosDTO> gerarRelatorioQuantidadePorCategoria();
} 