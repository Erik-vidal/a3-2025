package com.estoque.frontend;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import com.estoque.model.*;
import com.estoque.service.ProdutoService;

public class MovimentacaoDialog extends JDialog {
    private JComboBox<Produto> produtoComboBox;
    private JTextField quantidadeField;
    private boolean confirmed = false;
    private Movimentacao movimentacao;
    private TipoMovimentacao tipo;
    private ProdutoService produtoService;

    public MovimentacaoDialog(JFrame parent, String title, TipoMovimentacao tipo, ProdutoService produtoService) {
        super(parent, title, true);
        this.tipo = tipo;
        this.produtoService = produtoService;
        this.movimentacao = new Movimentacao();
        
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Painel de campos
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        List<Produto> produtos = produtoService.listarProdutos();
        produtoComboBox = new JComboBox<>(produtos.toArray(new Produto[0]));
        quantidadeField = new JTextField(20);
        
        fieldsPanel.add(new JLabel("Produto:"));
        fieldsPanel.add(produtoComboBox);
        fieldsPanel.add(new JLabel("Quantidade:"));
        fieldsPanel.add(quantidadeField);
        
        // Painel de botões
        JPanel buttonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancelar");
        
        okButton.addActionListener(e -> {
            if (validateFields()) {
                updateMovimentacao();
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

    private boolean validateFields() {
        if (produtoComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            int quantidade = Integer.parseInt(quantidadeField.getText());
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero", "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (tipo == TipoMovimentacao.SAIDA) {
                Produto produto = (Produto) produtoComboBox.getSelectedItem();
                if (quantidade > produto.getQuantidade()) {
                    JOptionPane.showMessageDialog(this, "Quantidade insuficiente em estoque", "Erro", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void updateMovimentacao() {
        movimentacao.setProduto((Produto) produtoComboBox.getSelectedItem());
        movimentacao.setQuantidade(Integer.parseInt(quantidadeField.getText()));
        movimentacao.setTipo(tipo);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Movimentacao getMovimentacao() {
        return movimentacao;
    }
} 