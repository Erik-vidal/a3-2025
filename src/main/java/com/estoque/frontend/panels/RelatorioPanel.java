package com.estoque.frontend.panels;

import com.estoque.frontend.service.ApiService;
import com.estoque.dto.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RelatorioPanel extends JPanel {
    private final ApiService apiService;
    private final JTable table;
    private final JLabel totalLabel;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public RelatorioPanel(ApiService apiService) {
        this.apiService = apiService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colunas = {"Produto", "Quantidade", "Preço Unitário", "Valor Total"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        totalLabel = new JLabel("Valor Total do Estoque: R$ 0,00");
        add(totalLabel, BorderLayout.SOUTH);

        JButton atualizarButton = new JButton("Atualizar");
        atualizarButton.addActionListener(e -> atualizarBalanco());
        add(atualizarButton, BorderLayout.NORTH);

        atualizarBalanco();
    }

    private void atualizarBalanco() {
        try {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);

            List<BalancoDTO> balanco = apiService.gerarBalanco();
            BigDecimal valorTotalEstoque = BigDecimal.ZERO;

            for (BalancoDTO item : balanco) {
                model.addRow(new Object[]{
                    item.getNome(),
                    item.getQuantidadeDisponivel(),
                    currencyFormat.format(item.getPrecoUnitario()),
                    currencyFormat.format(item.getValorTotal())
                });
                valorTotalEstoque = valorTotalEstoque.add(item.getValorTotal());
            }

            totalLabel.setText("Valor Total do Estoque: " + currencyFormat.format(valorTotalEstoque));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar balanço: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
} 