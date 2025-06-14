package com.estoque.repository;

import com.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findAllByOrderByNomeAsc();
    
    @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.categoria ORDER BY p.nome")
    List<Produto> findAllWithCategoriaOrderByNome();
    
    @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.categoria WHERE p.quantidadeAtual < p.quantidadeMinima ORDER BY p.nome")
    List<Produto> findProdutosAbaixoDoMinimo();
    
    @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.categoria WHERE p.quantidadeAtual > p.quantidadeMaxima ORDER BY p.nome")
    List<Produto> findProdutosAcimaDoMaximo();

    @Query("SELECT p FROM Produto p LEFT JOIN FETCH p.categoria WHERE p.id = :id")
    Optional<Produto> findByIdWithCategoria(Long id);
} 