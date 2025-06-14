package com.estoque.dto;

public class CategoriaProdutosDTO {
    private String categoria;
    private Long quantidadeProdutos;

    public CategoriaProdutosDTO(String categoria, Long quantidadeProdutos) {
        this.categoria = categoria;
        this.quantidadeProdutos = quantidadeProdutos;
    }

    public String getCategoria() {
        return categoria;
    }

    public Long getQuantidadeProdutos() {
        return quantidadeProdutos;
    }
} 