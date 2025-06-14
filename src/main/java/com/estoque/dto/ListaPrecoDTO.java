package com.estoque.dto;

import java.math.BigDecimal;

public class ListaPrecoDTO {
    private String nome;
    private BigDecimal precoUnitario;
    private String unidadeMedida;
    private String categoria;

    public ListaPrecoDTO(String nome, BigDecimal precoUnitario, String unidadeMedida, String categoria) {
        this.nome = nome;
        this.precoUnitario = precoUnitario;
        this.unidadeMedida = unidadeMedida;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public String getCategoria() {
        return categoria;
    }
} 