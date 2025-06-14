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

public class RelatoriosPanel extends JPanel {
    private final ApiService apiService;
    private final JTabbedPane tabbedPane;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    public RelatoriosPanel(ApiService apiService) {
        this.apiService = apiService;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        // Adiciona as abas
        tabbedPane.addTab("Lista de Preços", criarPainelListaPrecos());
        tabbedPane.addTab("Balanço Físico/Financeiro", criarPainelBalanco());
        tabbedPane.addTab("Produtos Abaixo do Mínimo", criarPainelProdutosAbaixoMinimo());
        tabbedPane.addTab("Produtos Acima do Máximo", criarPainelProdutosAcimaMaximo());
        tabbedPane.addTab("Produtos por Categoria", criarPainelProdutosPorCategoria());

        // Botão de atualizar
        JButton atualizarButton = new JButton("Atualizar Relatórios");
        atualizarButton.addActionListener(e -> atualizarTodosRelatorios());
        add(atualizarButton, BorderLayout.SOUTH);

        // Carrega os dados iniciais
        atualizarTodosRelatorios();
    }

    private JPanel criarPainelListaPrecos() {
        String[] colunas = {"Produto", "Preço Unitário", "Unidade", "Categoria"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        return new JPanel(new BorderLayout()) {{
            add(new JScrollPane(table), BorderLayout.CENTER);
            putClientProperty("tableModel", model);
        }};
    }

    private JPanel criarPainelBalanco() {
        String[] colunas = {"Produto", "Quantidade", "Preço Unitário", "Valor Total"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        JLabel totalLabel = new JLabel("Valor Total do Estoque: R$ 0,00");
        
        return new JPanel(new BorderLayout()) {{
            add(new JScrollPane(table), BorderLayout.CENTER);
            add(totalLabel, BorderLayout.SOUTH);
            putClientProperty("tableModel", model);
            putClientProperty("totalLabel", totalLabel);
        }};
    }

    private JPanel criarPainelProdutosAbaixoMinimo() {
        String[] colunas = {"Produto", "Quantidade Mínima", "Quantidade Atual"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        return new JPanel(new BorderLayout()) {{
            add(new JScrollPane(table), BorderLayout.CENTER);
            putClientProperty("tableModel", model);
        }};
    }

    private JPanel criarPainelProdutosAcimaMaximo() {
        String[] colunas = {"Produto", "Quantidade Máxima", "Quantidade Atual"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        return new JPanel(new BorderLayout()) {{
            add(new JScrollPane(table), BorderLayout.CENTER);
            putClientProperty("tableModel", model);
        }};
    }

    private JPanel criarPainelProdutosPorCategoria() {
        String[] colunas = {"Categoria", "Quantidade de Produtos"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);
        return new JPanel(new BorderLayout()) {{
            add(new JScrollPane(table), BorderLayout.CENTER);
            putClientProperty("tableModel", model);
        }};
    }

    private void atualizarTodosRelatorios() {
        try {
            atualizarListaPrecos();
            atualizarBalanco();
            atualizarProdutosAbaixoMinimo();
            atualizarProdutosAcimaMaximo();
            atualizarProdutosPorCategoria();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar relatórios: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarListaPrecos() {
        JPanel panel = (JPanel) tabbedPane.getComponentAt(0);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        model.setRowCount(0);

        List<ListaPrecoDTO> lista = apiService.gerarListaPrecos();
        for (ListaPrecoDTO item : lista) {
            model.addRow(new Object[]{
                item.getNome(),
                currencyFormat.format(item.getPrecoUnitario()),
                item.getUnidadeMedida(),
                item.getCategoria()
            });
        }
    }

    private void atualizarBalanco() {
        JPanel panel = (JPanel) tabbedPane.getComponentAt(1);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        JLabel totalLabel = (JLabel) panel.getClientProperty("totalLabel");
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
    }

    private void atualizarProdutosAbaixoMinimo() {
        JPanel panel = (JPanel) tabbedPane.getComponentAt(2);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        model.setRowCount(0);

        List<ProdutoQuantidadeDTO> lista = apiService.gerarRelatorioProdutosAbaixoMinimo();
        for (ProdutoQuantidadeDTO item : lista) {
            model.addRow(new Object[]{
                item.getNome(),
                item.getQuantidadeMinima(),
                item.getQuantidadeAtual()
            });
        }
    }

    private void atualizarProdutosAcimaMaximo() {
        JPanel panel = (JPanel) tabbedPane.getComponentAt(3);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        model.setRowCount(0);

        List<ProdutoQuantidadeDTO> lista = apiService.gerarRelatorioProdutosAcimaMaximo();
        for (ProdutoQuantidadeDTO item : lista) {
            model.addRow(new Object[]{
                item.getNome(),
                item.getQuantidadeMaxima(),
                item.getQuantidadeAtual()
            });
        }
    }

    private void atualizarProdutosPorCategoria() {
        JPanel panel = (JPanel) tabbedPane.getComponentAt(4);
        DefaultTableModel model = (DefaultTableModel) panel.getClientProperty("tableModel");
        model.setRowCount(0);

        List<CategoriaProdutosDTO> lista = apiService.gerarRelatorioQuantidadePorCategoria();
        for (CategoriaProdutosDTO item : lista) {
            model.addRow(new Object[]{
                item.getCategoria(),
                item.getQuantidadeProdutos()
            });
        }
    }
} 