package com.estoque.controller;

import com.estoque.model.Produto;
import com.estoque.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos", description = "APIs para gerenciamento de produtos")
public class ProdutoController {
    private final ProdutoService produtoService;

    @Operation(summary = "Criar produto",
            description = "Cria um novo produto no sistema. O ID será gerado automaticamente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto criado com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Produto.class),
                        examples = @ExampleObject(value = 
                            """
                            {
                              "id": 1,
                              "nome": "Detergente",
                              "unidadeMedida": "UN",
                              "precoUnitario": 2.50,
                              "quantidadeAtual": 100,
                              "quantidadeMinima": 20,
                              "quantidadeMaxima": 200,
                              "categoria": {
                                "id": 1,
                                "nome": "Limpeza",
                                "descricao": "Produtos de limpeza em geral"
                              }
                            }
                            """))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Dados do novo produto",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Produto.class),
            examples = @ExampleObject(value = 
                """
                {
                  "nome": "Detergente",
                  "unidadeMedida": "UN",
                  "precoUnitario": 2.50,
                  "quantidadeAtual": 100,
                  "quantidadeMinima": 20,
                  "quantidadeMaxima": 200,
                  "categoria": {
                    "id": 1
                  }
                }
                """)
        )
    )
    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        return ResponseEntity.ok(produtoService.salvar(produto));
    }

    @Operation(summary = "Listar produtos",
            description = "Retorna a lista de todos os produtos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Produto.class),
                    examples = @ExampleObject(value = 
                        """
                        [
                          {
                            "id": 1,
                            "nome": "Detergente",
                            "unidadeMedida": "UN",
                            "precoUnitario": 2.50,
                            "quantidadeAtual": 100,
                            "quantidadeMinima": 20,
                            "quantidadeMaxima": 200,
                            "categoria": {
                              "id": 1,
                              "nome": "Limpeza"
                            }
                          }
                        ]
                        """)))
    @GetMapping
    public ResponseEntity<List<Produto>> listarProdutos() {
        return ResponseEntity.ok(produtoService.listarTodos());
    }
} 