package com.estoque.repository;

import com.estoque.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    @Query("SELECT c.nome as categoria, COUNT(p) as quantidade FROM Categoria c LEFT JOIN c.produtos p GROUP BY c.nome ORDER BY c.nome")
    List<CategoriaProdutosResult> countProdutosPorCategoria();

    interface CategoriaProdutosResult {
        String getCategoria();
        Long getQuantidade();
    }
} 