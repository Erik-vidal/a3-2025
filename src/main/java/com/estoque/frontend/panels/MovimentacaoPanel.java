package com.estoque.frontend.panels;

import com.estoque.frontend.service.ApiService;
import com.estoque.model.Movimentacao;
import com.estoque.model.Produto;
import com.estoque.model.TipoMovimentacao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MovimentacaoPanel extends JPanel {
    private final ApiService apiService;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JComboBox<ProdutoItem> produtoCombo;
    private final JComboBox<TipoMovimentacao> tipoCombo;
    private final JTextField quantidadeField;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public MovimentacaoPanel(ApiService apiService) {
        this.apiService = apiService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Produto
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1;
        produtoCombo = new JComboBox<>();
        formPanel.add(produtoCombo, gbc);

        // Tipo de Movimentação
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1;
        tipoCombo = new JComboBox<>(TipoMovimentacao.values());
        formPanel.add(tipoCombo, gbc);

        // Quantidade
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        quantidadeField = new JTextField(20);
        formPanel.add(quantidadeField, gbc);

        // Botão Registrar
        gbc.gridx = 1; gbc.gridy = 3;
        JButton registrarButton = new JButton("Registrar Movimentação");
        registrarButton.addActionListener(e -> registrarMovimentacao());
        formPanel.add(registrarButton, gbc);

        // Tabela
        String[] colunas = {"ID", "Produto", "Tipo", "Quantidade", "Data/Hora"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        
        JButton atualizarButton = new JButton("Atualizar Lista");
        atualizarButton.addActionListener(e -> {
            atualizarProdutos();
            atualizarTabela();
        });
        topPanel.add(atualizarButton, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Carrega dados iniciais
        atualizarProdutos();
        atualizarTabela();
    }

    private void atualizarProdutos() {
        try {
            List<Produto> produtos = apiService.listarProdutos();
            produtoCombo.removeAllItems();
            for (Produto produto : produtos) {
                produtoCombo.addItem(new ProdutoItem(produto));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarMovimentacao() {
        try {
            ProdutoItem produtoItem = (ProdutoItem) produtoCombo.getSelectedItem();
            if (produtoItem == null) {
                JOptionPane.showMessageDialog(this, "Selecione um produto!");
                return;
            }

            // Cria um novo produto apenas com o ID
            Produto produto = new Produto();
            produto.setId(produtoItem.getProduto().getId());

            // Cria a movimentação com o produto
            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setProduto(produto);
            movimentacao.setTipo((TipoMovimentacao) tipoCombo.getSelectedItem());
            
            try {
                movimentacao.setQuantidade(Integer.parseInt(quantidadeField.getText()));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Quantidade inválida!");
                return;
            }

            apiService.realizarMovimentacao(movimentacao);
            JOptionPane.showMessageDialog(this, "Movimentação registrada com sucesso!");
            
            // Limpa campos
            produtoCombo.setSelectedIndex(-1);
            tipoCombo.setSelectedIndex(0);
            quantidadeField.setText("");
            
            // Atualiza tabela
            atualizarTabela();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao registrar movimentação: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabela() {
        try {
            List<Movimentacao> movimentacoes = apiService.listarMovimentacoes();
            tableModel.setRowCount(0);
            for (Movimentacao movimentacao : movimentacoes) {
                Object[] row = {
                    movimentacao.getId(),
                    movimentacao.getProduto().getNome(),
                    movimentacao.getTipo(),
                    movimentacao.getQuantidade(),
                    movimentacao.getDataHora().format(dateFormatter)
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar movimentações: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class ProdutoItem {
        private final Produto produto;

        public ProdutoItem(Produto produto) {
            this.produto = produto;
        }

        public Produto getProduto() {
            return produto;
        }

        @Override
        public String toString() {
            return produto.getNome();
        }
    }
} 