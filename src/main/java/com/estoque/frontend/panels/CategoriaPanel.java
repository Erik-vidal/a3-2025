package com.estoque.frontend.panels;

import com.estoque.frontend.service.ApiService;
import com.estoque.model.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoriaPanel extends JPanel {
    private final ApiService apiService;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField nomeField;
    private final JTextArea descricaoArea;

    public CategoriaPanel(ApiService apiService) {
        this.apiService = apiService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulário
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        nomeField = new JTextField(20);
        formPanel.add(nomeField, gbc);

        // Descrição
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        descricaoArea = new JTextArea(3, 20);
        descricaoArea.setLineWrap(true);
        formPanel.add(new JScrollPane(descricaoArea), gbc);

        // Botão Salvar
        gbc.gridx = 1; gbc.gridy = 2;
        JButton salvarButton = new JButton("Salvar");
        salvarButton.addActionListener(e -> salvarCategoria());
        formPanel.add(salvarButton, gbc);

        // Tabela
        String[] colunas = {"ID", "Nome", "Descrição"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // Layout
        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Botão Atualizar
        JButton atualizarButton = new JButton("Atualizar Lista");
        atualizarButton.addActionListener(e -> atualizarTabela());
        add(atualizarButton, BorderLayout.SOUTH);

        // Carrega dados iniciais
        atualizarTabela();
    }

    private void salvarCategoria() {
        try {
            Categoria categoria = new Categoria();
            categoria.setNome(nomeField.getText());
            categoria.setDescricao(descricaoArea.getText());

            apiService.criarCategoria(categoria);
            JOptionPane.showMessageDialog(this, "Categoria salva com sucesso!");
            
            // Limpa campos
            nomeField.setText("");
            descricaoArea.setText("");
            
            // Atualiza tabela
            atualizarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar categoria: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabela() {
        try {
            List<Categoria> categorias = apiService.listarCategorias();
            tableModel.setRowCount(0);
            for (Categoria categoria : categorias) {
                tableModel.addRow(new Object[]{
                    categoria.getId(),
                    categoria.getNome(),
                    categoria.getDescricao()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar categorias: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
} 