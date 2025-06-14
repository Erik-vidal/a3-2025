package com.estoque.frontend.panels;

import com.estoque.frontend.ProdutoDialog;
import com.estoque.frontend.service.ApiService;
import com.estoque.model.Produto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProdutoPanel extends JPanel {
    private final ApiService apiService;
    private final JTable table;
    private final DefaultTableModel tableModel;

    public ProdutoPanel(ApiService apiService) {
        this.apiService = apiService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botões
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Adicionar");
        JButton editButton = new JButton("Editar");
        JButton deleteButton = new JButton("Excluir");
        JButton refreshButton = new JButton("Atualizar");

        addButton.addActionListener(e -> adicionarProduto());
        editButton.addActionListener(e -> editarProduto());
        deleteButton.addActionListener(e -> excluirProduto());
        refreshButton.addActionListener(e -> atualizarTabela());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Tabela
        String[] colunas = {"ID", "Nome", "Quantidade", "Preço", "Categoria"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Carrega dados iniciais
        atualizarTabela();
    }

    private void adicionarProduto() {
        ProdutoDialog dialog = new ProdutoDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Novo Produto", null, apiService);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            try {
                Produto produto = dialog.getProduto();
                apiService.salvarProduto(produto);
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar produto: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editarProduto() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para editar");
            return;
        }

        Long id = (Long) tableModel.getValueAt(selectedRow, 0);
        try {
            Produto produto = apiService.buscarProduto(id);
            ProdutoDialog dialog = new ProdutoDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                    "Editar Produto", produto, apiService);
            dialog.setVisible(true);

            if (dialog.isConfirmed()) {
                produto = dialog.getProduto();
                apiService.atualizarProduto(produto);
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao editar produto: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirProduto() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Selecione um produto para excluir");
            return;
        }

        int option = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir este produto?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.YES_OPTION) {
            Long id = (Long) tableModel.getValueAt(selectedRow, 0);
            try {
                apiService.excluirProduto(id);
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void atualizarTabela() {
        try {
            List<Produto> produtos = apiService.listarProdutos();
            tableModel.setRowCount(0);
            for (Produto produto : produtos) {
                Object[] row = {
                    produto.getId(),
                    produto.getNome(),
                    produto.getQuantidade(),
                    produto.getPreco(),
                    produto.getCategoria() != null ? produto.getCategoria().getNome() : ""
                };
                tableModel.addRow(row);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
} 