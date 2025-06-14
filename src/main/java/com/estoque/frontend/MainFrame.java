package com.estoque.frontend;

import com.estoque.frontend.service.ApiService;
import com.estoque.frontend.panels.*;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainFrame extends JFrame {
    private final ApiService apiService;

    public MainFrame(ApiService apiService) {
        this.apiService = apiService;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sistema de Controle de Estoque");
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Produtos", new ProdutoPanel(apiService));
        tabbedPane.addTab("Categorias", new CategoriaPanel(apiService));
        tabbedPane.addTab("Movimentações", new MovimentacaoPanel(apiService));
        tabbedPane.addTab("Relatórios", new RelatoriosPanel(apiService));

        add(tabbedPane);
    }
} 