# Sistema de Controle de Estoque

Sistema completo para gerenciamento de estoque, incluindo backend em Spring Boot e frontend em Java Swing.

## Requisitos

- Docker e Docker Compose
- Java 17 JDK
- Maven (opcional, pode usar através do container)

## Executando o Sistema

### 1. Backend (Spring Boot + MySQL)

Para iniciar o backend e o banco de dados:

```bash
docker-compose up
```

O backend estará disponível em `http://localhost:8080`

### 2. Frontend (Java Swing)

O frontend é uma aplicação desktop Java Swing que se comunica com o backend.

#### Pré-requisitos do Frontend

1. Certifique-se de ter o Java 17 instalado:
   ```bash
   java --version
   ```

   Se não estiver instalado:
   - **Manjaro/Arch**: `sudo pacman -S jdk17-openjdk`
   - **Ubuntu/Debian**: `sudo apt install openjdk-17-jdk`
   - **Fedora**: `sudo dnf install java-17-openjdk`

2. Certifique-se de que o backend está rodando antes de iniciar o frontend.

#### Executando o Frontend

1. Execute o script de inicialização:
   ```bash
   ./run-frontend.sh
   ```

   Este script irá:
   - Copiar o JAR compilado do container Maven
   - Iniciar a interface gráfica do sistema

#### Funcionalidades do Frontend

A interface gráfica possui as seguintes seções:

1. **Cadastros**
   - Gerenciamento de Categorias
   - Gerenciamento de Produtos

2. **Movimentações**
   - Registro de Entradas
   - Registro de Saídas
   - Controle de Quantidades

3. **Ferramentas**
   - Reajuste de Preços

4. **Relatórios**
   - Balanço de Estoque
   - Valor Total do Estoque

#### Regras de Negócio no Frontend

- Produtos não podem ficar abaixo da quantidade mínima
- Entradas não podem ultrapassar quantidade máxima
- Validação automática de limites de estoque
- Cálculo automático de valores totais
- Formatação de valores monetários em Real (R$)

#### Solução de Problemas

1. **Erro "java: command not found"**
   - Verifique se o Java está instalado corretamente
   - Confirme se a variável JAVA_HOME está configurada

2. **Erro de conexão com o backend**
   - Verifique se o backend está rodando (`docker-compose ps`)
   - Confirme se a porta 8080 está acessível

3. **Erro ao copiar o JAR**
   - Verifique se o container `estoque_maven` está rodando
   - Tente reconstruir o projeto com `docker-compose up --build`

## API Documentation

A documentação da API está disponível em:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI: http://localhost:8080/v3/api-docs

## Endpoints da API

### Categorias

- `POST /api/categorias` - Criar uma nova categoria
  ```json
  // Request
  {
    "nome": "Limpeza",
    "descricao": "Produtos de limpeza em geral"
  }

  // Response
  {
    "id": 1,
    "nome": "Limpeza",
    "descricao": "Produtos de limpeza em geral"
  }
  ```
- `GET /api/categorias` - Listar todas as categorias

### Produtos

- `POST /api/produtos` - Criar um novo produto
  ```json
  // Request
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

  // Response
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
  ```
- `GET /api/produtos` - Listar todos os produtos

### Movimentações de Estoque

- `POST /api/estoque/movimentacao` - Realizar movimentação de entrada/saída
  ```json
  // Request
  {
    "produto": {"id": 1},
    "tipo": "ENTRADA",
    "quantidade": 10
  }

  // Response
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
  ```

### Reajuste de Preços

- `POST /api/estoque/reajuste` - Reajusta os preços dos produtos
  ```json
  {
    "percentual": 10.5
  }
  ```

### Relatórios

- `GET /api/relatorios/balanco` - Gerar balanço físico/financeiro do estoque
  ```json
  {
    "produtos": [
      {
        "nome": "Detergente",
        "categoria": "Limpeza",
        "unidadeMedida": "UN",
        "precoUnitario": 2.50,
        "quantidadeDisponivel": 100,
        "valorTotal": 250.00
      }
    ],
    "valorTotalEstoque": 250.00
  }
  ```

## Tipos de Movimentação

- **ENTRADA**: Adiciona quantidade ao estoque
  - Exemplos: compras, devoluções
  - Validação: não pode ultrapassar quantidade máxima

- **SAIDA**: Remove quantidade do estoque
  - Exemplos: vendas, perdas
  - Validações: 
    - Deve haver quantidade suficiente
    - Não pode ficar abaixo do mínimo

## Estrutura do Projeto

- `model/` - Classes de domínio (Produto, Categoria, Movimentacao)
- `repository/` - Interfaces de repositório JPA
- `service/` - Lógica de negócio
- `controller/` - Endpoints da API REST
- `config/` - Classes de configuração
- `dto/` - Classes de transferência de dados para relatórios

## Observações

- Os IDs são gerados automaticamente pelo sistema
- Campos nulos são omitidos nas respostas JSON
- A data e hora das movimentações são geradas automaticamente pelo sistema
- Para referências a outras entidades (ex: categoria em produto), apenas o ID é necessário
