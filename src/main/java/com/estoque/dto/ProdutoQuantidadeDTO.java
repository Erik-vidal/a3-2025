package com.estoque.dto;

public class ProdutoQuantidadeDTO {
    private String nome;
    private Integer quantidadeMinima;
    private Integer quantidadeMaxima;
    private Integer quantidadeAtual;

    public ProdutoQuantidadeDTO(String nome, Integer quantidadeMinima, Integer quantidadeMaxima, Integer quantidadeAtual) {
        this.nome = nome;
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
        this.quantidadeAtual = quantidadeAtual;
    }

    public String getNome() {
        return nome;
    }

    public Integer getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public Integer getQuantidadeMaxima() {
        return quantidadeMaxima;
    }

    public Integer getQuantidadeAtual() {
        return quantidadeAtual;
    }
} 