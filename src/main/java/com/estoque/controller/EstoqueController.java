package com.estoque.controller;

import com.estoque.model.Movimentacao;
import com.estoque.model.Produto;
import com.estoque.service.EstoqueService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estoque")
@RequiredArgsConstructor
@Tag(name = "Movimentações", description = "APIs para controle de movimentações de estoque")
public class EstoqueController {
    private final EstoqueService estoqueService;

    @Operation(summary = "Realizar movimentação de estoque",
            description = """
                    Registra uma movimentação de entrada ou saída no estoque.
                    
                    Tipos de movimentação:
                    - ENTRADA: Adiciona quantidade ao estoque (ex: compras, devoluções)
                    - SAIDA: Remove quantidade do estoque (ex: vendas, perdas)
                    
                    Regras:
                    - Para ENTRADA: Verifica se não ultrapassa quantidade máxima
                    - Para SAIDA: Verifica se há quantidade suficiente e se não fica abaixo do mínimo
                    
                    O ID da movimentação e a data/hora são gerados automaticamente pelo sistema.
                    """)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movimentação realizada com sucesso",
                content = @Content(mediaType = "application/json",
                        schema = @Schema(implementation = Movimentacao.class),
                        examples = @ExampleObject(value = 
                            """
                            {
                              "id": 1,
                              "produto": {
                                "id": 1,
                                "nome": "Detergente"
                              },
                              "tipo": "ENTRADA",
                              "quantidade": 10,
                              "dataHora": "2024-03-15T10:30:00"
                            }
                            """))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou violação de regras de negócio (quantidade máxima/mínima)"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Dados da movimentação",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Movimentacao.class),
            examples = @ExampleObject(value = 
                """
                {
                  "produto": {"id": 1},
                  "tipo": "ENTRADA",
                  "quantidade": 10
                }
                """)
        )
    )
    @PostMapping("/movimentacao")
    public ResponseEntity<Void> realizarMovimentacao(@RequestBody Movimentacao movimentacao) {
        estoqueService.realizarMovimentacao(movimentacao);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reajustar preços",
            description = "Aplica um percentual de reajuste aos preços de todos os produtos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reajuste aplicado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Percentual inválido")
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Percentual de reajuste",
        required = true,
        content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(value = 
                """
                {
                  "percentual": 10.5
                }
                """)
        )
    )
    @PostMapping("/reajuste")
    public ResponseEntity<Void> reajustarPrecos(@RequestBody Map<String, BigDecimal> request) {
        estoqueService.reajustarPrecos(request.get("percentual"));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Listar produtos",
            description = "Retorna a lista de todos os produtos ordenados por nome")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Produto.class)))
    @GetMapping("/produtos")
    public ResponseEntity<List<Produto>> listarProdutos() {
        return ResponseEntity.ok(estoqueService.listarProdutosOrdenadosPorNome());
    }

    @Operation(summary = "Listar produtos abaixo do mínimo",
            description = "Retorna a lista de produtos com quantidade em estoque abaixo do mínimo")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    @GetMapping("/produtos/abaixo-minimo")
    public ResponseEntity<List<Produto>> listarProdutosAbaixoDoMinimo() {
        return ResponseEntity.ok(estoqueService.listarProdutosAbaixoDoMinimo());
    }

    @Operation(summary = "Listar produtos acima do máximo",
            description = "Retorna a lista de produtos com quantidade em estoque acima do máximo")
    @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso")
    @GetMapping("/produtos/acima-maximo")
    public ResponseEntity<List<Produto>> listarProdutosAcimaDoMaximo() {
        return ResponseEntity.ok(estoqueService.listarProdutosAcimaDoMaximo());
    }
} 