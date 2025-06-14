package com.estoque.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.estoque.model.Movimentacao;
import java.util.List;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    @Query("SELECT m FROM Movimentacao m LEFT JOIN FETCH m.produto p LEFT JOIN FETCH p.categoria ORDER BY m.dataHora DESC")
    List<Movimentacao> findAll();

    @Query("SELECT m FROM Movimentacao m LEFT JOIN FETCH m.produto p LEFT JOIN FETCH p.categoria WHERE p.id = :produtoId ORDER BY m.dataHora DESC")
    List<Movimentacao> findByProdutoId(Long produtoId);
    
    @Override
    @Query("SELECT m FROM Movimentacao m LEFT JOIN FETCH m.produto p LEFT JOIN FETCH p.categoria WHERE m.id = :id")
    java.util.Optional<Movimentacao> findById(Long id);
} 