-- Drop tables if they exist
DROP TABLE IF EXISTS movimentacoes;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS categorias;

-- Create categorias table
CREATE TABLE categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT
);

-- Create produtos table
CREATE TABLE produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    unidade_medida VARCHAR(20) NOT NULL,
    preco_unitario DECIMAL(10,2) NOT NULL,
    quantidade_atual INT NOT NULL,
    quantidade_minima INT NOT NULL,
    quantidade_maxima INT NOT NULL,
    categoria_id BIGINT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);

-- Create movimentacoes table
CREATE TABLE movimentacoes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    quantidade INT NOT NULL,
    data_hora DATETIME NOT NULL,
    FOREIGN KEY (produto_id) REFERENCES produtos(id),
    CHECK (tipo IN ('ENTRADA', 'SAIDA'))
);

-- Insert some sample categories
INSERT INTO categorias (nome, descricao) VALUES
    ('Limpeza', 'Produtos de limpeza em geral'),
    ('Enlatados', 'Alimentos enlatados e conservas'),
    ('Vegetais', 'Vegetais frescos'),
    ('Óleos', 'Óleos de cozinha e azeites');

-- Insert some sample products
INSERT INTO produtos (nome, unidade_medida, preco_unitario, quantidade_atual, quantidade_minima, quantidade_maxima, categoria_id) VALUES
    ('Detergente', 'UN', 2.50, 100, 20, 200, 1),
    ('Milho em Conserva', 'UN', 3.75, 50, 10, 100, 2),
    ('Tomate', 'KG', 5.99, 30, 10, 50, 3),
    ('Azeite Extra Virgem', 'UN', 25.90, 20, 5, 40, 4);

-- Create indexes for better performance
CREATE INDEX idx_produtos_nome ON produtos(nome);
CREATE INDEX idx_produtos_categoria ON produtos(categoria_id);
CREATE INDEX idx_movimentacoes_produto ON movimentacoes(produto_id);
CREATE INDEX idx_movimentacoes_data ON movimentacoes(data_hora); 