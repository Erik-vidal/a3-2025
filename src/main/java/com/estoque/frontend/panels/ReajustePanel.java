package com.estoque.frontend.panels;

import com.estoque.frontend.service.ApiService;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class ReajustePanel extends JPanel {
    private final ApiService apiService;
    private final JTextField percentualField;

    public ReajustePanel(ApiService apiService) {
        this.apiService = apiService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Painel central com formulário
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titleLabel = new JLabel("Reajuste de Preços");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        centerPanel.add(titleLabel, gbc);

        // Descrição
        JTextArea descricaoArea = new JTextArea(
            "Esta operação irá reajustar os preços de todos os produtos no estoque.\n" +
            "Digite o percentual de reajuste (positivo para aumento, negativo para redução).\n" +
            "Exemplo: 10.5 para aumento de 10,5% ou -5.0 para redução de 5%."
        );
        descricaoArea.setEditable(false);
        descricaoArea.setBackground(null);
        descricaoArea.setWrapStyleWord(true);
        descricaoArea.setLineWrap(true);
        gbc.gridy = 1; gbc.gridwidth = 2;
        centerPanel.add(descricaoArea, gbc);

        // Campo de percentual
        gbc.gridy = 2; gbc.gridwidth = 1;
        centerPanel.add(new JLabel("Percentual de Reajuste (%):"), gbc);
        gbc.gridx = 1;
        percentualField = new JTextField(10);
        centerPanel.add(percentualField, gbc);

        // Botão de reajuste
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JButton reajustarButton = new JButton("Aplicar Reajuste");
        reajustarButton.addActionListener(e -> aplicarReajuste());
        centerPanel.add(reajustarButton, gbc);

        // Adiciona o painel central
        add(centerPanel, BorderLayout.CENTER);
    }

    private void aplicarReajuste() {
        try {
            String percentualText = percentualField.getText().replace(",", ".");
            BigDecimal percentual = new BigDecimal(percentualText);

            int option = JOptionPane.showConfirmDialog(this,
                "Confirma o reajuste de " + percentual + "% em todos os produtos?",
                "Confirmação",
                JOptionPane.YES_NO_OPTION);

            if (option == JOptionPane.YES_OPTION) {
                apiService.reajustarPrecos(percentual);
                JOptionPane.showMessageDialog(this, "Reajuste aplicado com sucesso!");
                percentualField.setText("");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Percentual inválido. Use apenas números e ponto/vírgula decimal.",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao aplicar reajuste: " + e.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE);
        }
    }
} 