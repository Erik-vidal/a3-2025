package com.estoque.frontend;

import javax.swing.*;
import java.awt.*;
import com.estoque.model.*;
import com.estoque.service.*;
import com.estoque.repository.*;
import com.estoque.frontend.panels.*;
import com.estoque.frontend.service.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.estoque")
@EntityScan("com.estoque.model")
@EnableJpaRepositories("com.estoque.repository")
public class EstoqueDesktopApp extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private ApiService apiService;

    public EstoqueDesktopApp(ApiService apiService) {
        this.apiService = apiService;
        
        setTitle("Sistema de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // Painel de Produtos
        ProdutoPanel produtosPanel = new ProdutoPanel(apiService);
        tabbedPane.addTab("Produtos", produtosPanel);

        // Painel de Movimentações
        MovimentacaoPanel movimentacoesPanel = new MovimentacaoPanel(apiService);
        tabbedPane.addTab("Movimentações", movimentacoesPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        add(mainPanel);
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(EstoqueDesktopApp.class)
            .headless(false)
            .run(args);

        SwingUtilities.invokeLater(() -> {
            ApiService apiService = context.getBean(SpringApiService.class);
            EstoqueDesktopApp app = new EstoqueDesktopApp(apiService);
            app.setVisible(true);
        });
    }
} 