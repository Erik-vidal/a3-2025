package com.estoque.frontend;

import javax.swing.*;
import java.awt.*;
import com.estoque.model.Produto;
import com.estoque.model.Categoria;
import com.estoque.frontend.service.ApiService;
import java.util.List;

public class ProdutoDialog extends JDialog {
    private JTextField nomeField;
    private JTextField quantidadeField;
    private JTextField precoField;
    private JComboBox<Categoria> categoriaCombo;
    private boolean confirmed = false;
    private Produto produto;
    private ApiService apiService;

    public ProdutoDialog(JFrame parent, String title, Produto produto, ApiService apiService) {
        super(parent, title, true);
        this.produto = produto != null ? produto : new Produto();
        this.apiService = apiService;
        
        initComponents();
        if (produto != null) {
            populateFields();
        }
        
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Painel de campos
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        nomeField = new JTextField(20);
        quantidadeField = new JTextField(20);
        precoField = new JTextField(20);
        categoriaCombo = new JComboBox<>();
        
        // Carrega as categorias
        try {
            List<Categoria> categorias = apiService.listarCategorias();
            for (Categoria categoria : categorias) {
                categoriaCombo.addItem(categoria);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar categorias: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        fieldsPanel.add(new JLabel("Nome:"));
        fieldsPanel.add(nomeField);
        fieldsPanel.add(new JLabel("Quantidade:"));
        fieldsPanel.add(quantidadeField);
        fieldsPanel.add(new JLabel("Preço:"));
        fieldsPanel.add(precoField);
        fieldsPanel.add(new JLabel("Categoria:"));
        fieldsPanel.add(categoriaCombo);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancelar");
        
        okButton.addActionListener(e -> {
            if (validateFields()) {
                updateProduto();
                confirmed = true;
                dispose();
            }
        });
        
        cancelButton.addActionListener(e -> dispose());
        
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        
        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void populateFields() {
        nomeField.setText(produto.getNome());
        quantidadeField.setText(String.valueOf(produto.getQuantidade()));
        precoField.setText(String.valueOf(produto.getPreco()));
        if (produto.getCategoria() != null) {
            for (int i = 0; i < categoriaCombo.getItemCount(); i++) {
                Categoria cat = categoriaCombo.getItemAt(i);
                if (cat.getId().equals(produto.getCategoria().getId())) {
                    categoriaCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private boolean validateFields() {
        if (nomeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome é obrigatório", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            Integer.parseInt(quantidadeField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            Double.parseDouble(precoField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço inválido", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (categoriaCombo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma categoria", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void updateProduto() {
        produto.setNome(nomeField.getText());
        produto.setQuantidade(Integer.parseInt(quantidadeField.getText()));
        produto.setPreco(Double.parseDouble(precoField.getText()));
        produto.setCategoria((Categoria) categoriaCombo.getSelectedItem());
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Produto getProduto() {
        return produto;
    }
} 