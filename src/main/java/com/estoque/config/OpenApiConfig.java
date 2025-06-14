package com.estoque.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Controle de Estoque API")
                        .version("1.0")
                        .description("""
                            API para gerenciamento completo de estoque, incluindo:
                            
                            * Cadastro e consulta de produtos e categorias
                            * Controle de movimentações (entradas e saídas)
                            * Reajuste de preços
                            * Relatórios gerenciais
                            
                            ### Funcionalidades principais:
                            
                            1. **Gestão de Produtos**
                               - Cadastro com controle de quantidades mínimas e máximas
                               - Organização por categorias
                               - Controle de unidades de medida
                            
                            2. **Movimentações**
                               - Entrada de produtos (compras, devoluções)
                               - Saída de produtos (vendas, perdas)
                               - Validações automáticas de limites
                            
                            3. **Relatórios**
                               - Balanço físico e financeiro
                               - Listagem de produtos por categoria
                               - Alertas de estoque mínimo/máximo
                            
                            ### Regras de Negócio:
                            
                            * Produtos não podem ficar abaixo da quantidade mínima
                            * Entradas não podem ultrapassar quantidade máxima
                            * Saídas exigem quantidade suficiente em estoque
                            """)
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento")
                                .email("contato@estoque.com")))
                .tags(Arrays.asList(
                        new Tag()
                                .name("Produtos")
                                .description("Operações de gerenciamento de produtos"),
                        new Tag()
                                .name("Categorias")
                                .description("Operações de gerenciamento de categorias"),
                        new Tag()
                                .name("Movimentações")
                                .description("Controle de entradas e saídas do estoque"),
                        new Tag()
                                .name("Relatórios")
                                .description("Relatórios gerenciais e balanços do estoque")))
                .components(new Components()
                        .addExamples("categoria-request", new Example()
                                .value("{\n  \"nome\": \"Limpeza\",\n  \"descricao\": \"Produtos de limpeza em geral\"\n}"))
                        .addExamples("produto-request", new Example()
                                .value("{\n  \"nome\": \"Detergente\",\n  \"unidadeMedida\": \"UN\",\n  \"precoUnitario\": 2.50,\n  \"quantidadeAtual\": 100,\n  \"quantidadeMinima\": 20,\n  \"quantidadeMaxima\": 200,\n  \"categoria\": {\n    \"id\": 1\n  }\n}"))
                        .addExamples("movimentacao-request", new Example()
                                .value("{\n  \"produto\": {\"id\": 1},\n  \"tipo\": \"ENTRADA\",\n  \"quantidade\": 10\n}"))
                        .addExamples("reajuste-request", new Example()
                                .value("{\n  \"percentual\": 10.5\n}"))
                        .addSchemas("TipoMovimentacao", new Schema<String>()
                                .type("string")
                                .description("Tipo de movimentação do estoque")
                                ._enum(Arrays.asList("ENTRADA", "SAIDA"))));
    }
} 