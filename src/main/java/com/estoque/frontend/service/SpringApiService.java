package com.estoque.frontend.service;

import com.estoque.dto.*;
import com.estoque.model.Categoria;
import com.estoque.model.Movimentacao;
import com.estoque.model.Produto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.List;

public class SpringApiService implements ApiService {
    private static final Logger logger = LoggerFactory.getLogger(SpringApiService.class);
    private final String baseUrl;
    private final RestTemplate restTemplate;

    public SpringApiService(String baseUrl, RestTemplate restTemplate) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
        logger.info("SpringApiService inicializado com URL base: {}", baseUrl);
    }

    @Override
    public List<Produto> listarProdutos() {
        String url = baseUrl + "/produtos";
        logger.info("Fazendo requisição GET para: {}", url);
        try {
            ResponseEntity<List<Produto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Produto>>() {}
            );
            logger.info("Resposta recebida com sucesso. Status: {}", response.getStatusCode());
            return response.getBody();
        } catch (Exception e) {
            logger.error("Erro ao listar produtos: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Produto buscarProduto(Long id) {
        return restTemplate.getForObject(baseUrl + "/produtos/" + id, Produto.class);
    }

    @Override
    public Produto salvarProduto(Produto produto) {
        return restTemplate.postForObject(baseUrl + "/produtos", produto, Produto.class);
    }

    @Override
    public Produto atualizarProduto(Produto produto) {
        restTemplate.put(baseUrl + "/produtos/" + produto.getId(), produto);
        return produto;
    }

    @Override
    public void excluirProduto(Long id) {
        restTemplate.delete(baseUrl + "/produtos/" + id);
    }

    @Override
    public List<Movimentacao> listarMovimentacoes() {
        ResponseEntity<List<Movimentacao>> response = restTemplate.exchange(
            baseUrl + "/movimentacoes",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Movimentacao>>() {}
        );
        return response.getBody();
    }

    @Override
    public Movimentacao realizarMovimentacao(Movimentacao movimentacao) {
        return restTemplate.postForObject(baseUrl + "/movimentacoes", movimentacao, Movimentacao.class);
    }

    @Override
    public List<Categoria> listarCategorias() {
        ResponseEntity<List<Categoria>> response = restTemplate.exchange(
            baseUrl + "/categorias",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Categoria>>() {}
        );
        return response.getBody();
    }

    @Override
    public Categoria criarCategoria(Categoria categoria) {
        return restTemplate.postForObject(baseUrl + "/categorias", categoria, Categoria.class);
    }

    @Override
    public void reajustarPrecos(BigDecimal percentual) {
        restTemplate.postForObject(baseUrl + "/produtos/reajuste?percentual=" + percentual, null, Void.class);
    }

    @Override
    public List<ListaPrecoDTO> gerarListaPrecos() {
        ResponseEntity<List<ListaPrecoDTO>> response = restTemplate.exchange(
            baseUrl + "/relatorios/lista-precos",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ListaPrecoDTO>>() {}
        );
        return response.getBody();
    }

    @Override
    public List<BalancoDTO> gerarBalanco() {
        ResponseEntity<List<BalancoDTO>> response = restTemplate.exchange(
            baseUrl + "/relatorios/balanco",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<BalancoDTO>>() {}
        );
        return response.getBody();
    }

    @Override
    public List<ProdutoQuantidadeDTO> gerarRelatorioProdutosAbaixoMinimo() {
        ResponseEntity<List<ProdutoQuantidadeDTO>> response = restTemplate.exchange(
            baseUrl + "/relatorios/produtos-abaixo-minimo",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ProdutoQuantidadeDTO>>() {}
        );
        return response.getBody();
    }

    @Override
    public List<ProdutoQuantidadeDTO> gerarRelatorioProdutosAcimaMaximo() {
        ResponseEntity<List<ProdutoQuantidadeDTO>> response = restTemplate.exchange(
            baseUrl + "/relatorios/produtos-acima-maximo",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ProdutoQuantidadeDTO>>() {}
        );
        return response.getBody();
    }

    @Override
    public List<CategoriaProdutosDTO> gerarRelatorioQuantidadePorCategoria() {
        ResponseEntity<List<CategoriaProdutosDTO>> response = restTemplate.exchange(
            baseUrl + "/relatorios/quantidade-por-categoria",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<CategoriaProdutosDTO>>() {}
        );
        return response.getBody();
    }
} 