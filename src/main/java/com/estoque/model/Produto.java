package com.estoque.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nome;
    
    @Column(nullable = false)
    private String unidadeMedida = "UN";
    
    @Column(nullable = false)
    private BigDecimal precoUnitario = BigDecimal.ZERO;
    
    @Column(nullable = false)
    private Integer quantidadeAtual = 0;
    
    @Column(nullable = false)
    private Integer quantidadeMinima = 0;
    
    @Column(nullable = false)
    private Integer quantidadeMaxima = 1000;
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getUnidadeMedida() {
        return unidadeMedida;
    }
    
    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
    
    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }
    
    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
    
    public Integer getQuantidadeAtual() {
        return quantidadeAtual;
    }
    
    public void setQuantidadeAtual(Integer quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }
    
    public Integer getQuantidadeMinima() {
        return quantidadeMinima;
    }
    
    public void setQuantidadeMinima(Integer quantidadeMinima) {
        this.quantidadeMinima = quantidadeMinima;
    }
    
    public Integer getQuantidadeMaxima() {
        return quantidadeMaxima;
    }
    
    public void setQuantidadeMaxima(Integer quantidadeMaxima) {
        this.quantidadeMaxima = quantidadeMaxima;
    }
    
    public Categoria getCategoria() {
        return categoria;
    }
    
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
    public Integer getQuantidade() {
        return getQuantidadeAtual();
    }
    
    public void setQuantidade(Integer quantidade) {
        setQuantidadeAtual(quantidade);
    }
    
    public Double getPreco() {
        return precoUnitario != null ? precoUnitario.doubleValue() : null;
    }
    
    public void setPreco(Double preco) {
        this.precoUnitario = preco != null ? BigDecimal.valueOf(preco) : null;
    }
    
    @Override
    public String toString() {
        return nome;
    }
} 