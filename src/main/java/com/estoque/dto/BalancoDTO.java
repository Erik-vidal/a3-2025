package com.estoque.dto;

import java.math.BigDecimal;

public class BalancoDTO {
    private String nome;
    private Integer quantidadeDisponivel;
    private BigDecimal precoUnitario;
    private BigDecimal valorTotal;

    public BalancoDTO(String nome, Integer quantidadeDisponivel, BigDecimal precoUnitario, BigDecimal valorTotal) {
        this.nome = nome;
        this.quantidadeDisponivel = quantidadeDisponivel;
        this.precoUnitario = precoUnitario;
        this.valorTotal = valorTotal;
    }

    public String getNome() {
        return nome;
    }

    public Integer getQuantidadeDisponivel() {
        return quantidadeDisponivel;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }
} 