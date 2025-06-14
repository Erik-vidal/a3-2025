package com.estoque.controller;

import com.estoque.service.RelatorioService;
import com.estoque.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioController {
    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/lista-precos")
    public List<ListaPrecoDTO> gerarListaPrecos() {
        return relatorioService.gerarListaPrecos();
    }

    @GetMapping("/balanco")
    public List<BalancoDTO> gerarBalanco() {
        return relatorioService.gerarBalanco();
    }

    @GetMapping("/produtos-abaixo-minimo")
    public List<ProdutoQuantidadeDTO> gerarRelatorioProdutosAbaixoMinimo() {
        return relatorioService.gerarRelatorioProdutosAbaixoMinimo();
    }

    @GetMapping("/produtos-acima-maximo")
    public List<ProdutoQuantidadeDTO> gerarRelatorioProdutosAcimaMaximo() {
        return relatorioService.gerarRelatorioProdutosAcimaMaximo();
    }

    @GetMapping("/quantidade-por-categoria")
    public List<CategoriaProdutosDTO> gerarRelatorioQuantidadePorCategoria() {
        return relatorioService.gerarRelatorioQuantidadePorCategoria();
    }
} 