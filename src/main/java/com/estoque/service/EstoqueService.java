package com.estoque.service;

import com.estoque.dto.BalancoDTO;
import com.estoque.model.Categoria;
import com.estoque.model.Movimentacao;
import com.estoque.model.Produto;
import com.estoque.repository.CategoriaRepository;
import com.estoque.repository.MovimentacaoRepository;
import com.estoque.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstoqueService {
    @Autowired
    private ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final MovimentacaoRepository movimentacaoRepository;

    public EstoqueService(ProdutoRepository produtoRepository,
                         CategoriaRepository categoriaRepository,
                         MovimentacaoRepository movimentacaoRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
        this.movimentacaoRepository = movimentacaoRepository;
    }

    @Transactional
    public void realizarMovimentacao(Movimentacao movimentacao) {
        Produto produto = produtoRepository.findById(movimentacao.getProduto().getId())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        int novaQuantidade;
        if ("ENTRADA".equals(movimentacao.getTipo())) {
            novaQuantidade = produto.getQuantidadeAtual() + movimentacao.getQuantidade();
            if (novaQuantidade > produto.getQuantidadeMaxima()) {
                throw new RuntimeException("Quantidade excede o limite máximo permitido");
            }
        } else if ("SAIDA".equals(movimentacao.getTipo())) {
            novaQuantidade = produto.getQuantidadeAtual() - movimentacao.getQuantidade();
            if (novaQuantidade < produto.getQuantidadeMinima()) {
                throw new RuntimeException("Quantidade ficaria abaixo do mínimo permitido");
            }
        } else {
            throw new RuntimeException("Tipo de movimentação inválido");
        }

        produto.setQuantidadeAtual(novaQuantidade);
        produtoRepository.save(produto);
        movimentacaoRepository.save(movimentacao);
    }

    @Transactional
    public void reajustarPrecos(BigDecimal percentual) {
        if (percentual.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O percentual de reajuste deve ser maior que zero");
        }

        List<Produto> produtos = produtoRepository.findAll();
        for (Produto produto : produtos) {
            BigDecimal precoAtual = produto.getPrecoUnitario();
            BigDecimal aumento = precoAtual.multiply(percentual.divide(BigDecimal.valueOf(100)));
            produto.setPrecoUnitario(precoAtual.add(aumento));
        }
        produtoRepository.saveAll(produtos);
    }

    public List<Produto> listarProdutosOrdenadosPorNome() {
        return produtoRepository.findAllByOrderByNomeAsc();
    }

    public List<Produto> listarProdutosAbaixoDoMinimo() {
        return produtoRepository.findProdutosAbaixoDoMinimo();
    }

    public List<Produto> listarProdutosAcimaDoMaximo() {
        return produtoRepository.findProdutosAcimaDoMaximo();
    }

    @Transactional(readOnly = true)
    public List<BalancoDTO> gerarBalanco() {
        List<Produto> produtos = produtoRepository.findAllWithCategoriaOrderByNome();
        List<BalancoDTO> balanco = new ArrayList<>();

        for (Produto produto : produtos) {
            BigDecimal valorTotal = produto.getPrecoUnitario()
                .multiply(BigDecimal.valueOf(produto.getQuantidadeAtual()));

            balanco.add(new BalancoDTO(
                produto.getNome(),
                produto.getQuantidadeAtual(),
                produto.getPrecoUnitario(),
                valorTotal
            ));
        }

        return balanco;
    }
} 