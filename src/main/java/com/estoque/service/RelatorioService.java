package com.estoque.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.estoque.repository.ProdutoRepository;
import com.estoque.repository.CategoriaRepository;
import com.estoque.dto.*;
import com.estoque.model.Produto;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelatorioService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<ListaPrecoDTO> gerarListaPrecos() {
        return produtoRepository.findAllWithCategoriaOrderByNome().stream()
            .map(p -> new ListaPrecoDTO(
                p.getNome(),
                p.getPrecoUnitario(),
                p.getUnidadeMedida(),
                p.getCategoria().getNome()))
            .collect(Collectors.toList());
    }

    public List<BalancoDTO> gerarBalanco() {
        List<BalancoDTO> balanco = produtoRepository.findAllWithCategoriaOrderByNome().stream()
            .map(p -> new BalancoDTO(
                p.getNome(),
                p.getQuantidadeAtual(),
                p.getPrecoUnitario(),
                p.getPrecoUnitario().multiply(BigDecimal.valueOf(p.getQuantidadeAtual()))))
            .collect(Collectors.toList());

        return balanco;
    }

    public BigDecimal calcularValorTotalEstoque() {
        return produtoRepository.findAllWithCategoriaOrderByNome().stream()
            .map(p -> p.getPrecoUnitario().multiply(BigDecimal.valueOf(p.getQuantidadeAtual())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<ProdutoQuantidadeDTO> gerarRelatorioProdutosAbaixoMinimo() {
        return produtoRepository.findProdutosAbaixoDoMinimo().stream()
            .map(p -> new ProdutoQuantidadeDTO(
                p.getNome(),
                p.getQuantidadeMinima(),
                p.getQuantidadeMaxima(),
                p.getQuantidadeAtual()))
            .collect(Collectors.toList());
    }

    public List<ProdutoQuantidadeDTO> gerarRelatorioProdutosAcimaMaximo() {
        return produtoRepository.findProdutosAcimaDoMaximo().stream()
            .map(p -> new ProdutoQuantidadeDTO(
                p.getNome(),
                p.getQuantidadeMinima(),
                p.getQuantidadeMaxima(),
                p.getQuantidadeAtual()))
            .collect(Collectors.toList());
    }

    public List<CategoriaProdutosDTO> gerarRelatorioQuantidadePorCategoria() {
        return categoriaRepository.countProdutosPorCategoria().stream()
            .map(result -> new CategoriaProdutosDTO(
                result.getCategoria(),
                result.getQuantidade()))
            .collect(Collectors.toList());
    }
} 