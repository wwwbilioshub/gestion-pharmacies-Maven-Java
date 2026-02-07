package com.pharmacie.views;
import com.pharmacie.controllers.LoginController;
import com.pharmacie.views.components.CustomButton;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
	 private LoginController loginController;
	    
	    private JTextField loginField;
	    private JPasswordField passwordField;
	    private CustomButton loginButton;
	    private CustomButton quitButton;
	    
	    public LoginView() {
	        this.loginController = new LoginController();
	        initComponents();
	        setupLayout();
	        setupListeners();
	    }
	    
	    private void initComponents() {
	        setTitle("Gestion des Pharmacies - Connexion");
	        setSize(500, 500);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setResizable(false);
	        
	        // Look and Feel moderne
	        try {
	            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    private void setupLayout() {
	        JPanel mainPanel = new JPanel();
	        mainPanel.setLayout(new BorderLayout());
	        mainPanel.setBackground(Color.WHITE);
	        
	        // Panel du haut avec le logo/titre
	        JPanel headerPanel = new JPanel();
	        headerPanel.setBackground(new Color(52, 58, 64));
	        headerPanel.setPreferredSize(new Dimension(0, 100));
	        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
	        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	        
	        JLabel titleLabel = new JLabel("GESTION DES PHARMACIES");
	        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
	        titleLabel.setForeground(Color.WHITE);
	        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        
	        JLabel subtitleLabel = new JLabel("Connectez-vous pour continuer");
	        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        subtitleLabel.setForeground(new Color(200, 200, 200));
	        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	        
	        headerPanel.add(Box.createVerticalGlue());
	        headerPanel.add(titleLabel);
	        headerPanel.add(Box.createVerticalStrut(10));
	        headerPanel.add(subtitleLabel);
	        headerPanel.add(Box.createVerticalGlue());
	        
	        // Panel central avec le formulaire
	        JPanel formPanel = new JPanel();
	        formPanel.setLayout(new GridBagLayout());
	        formPanel.setBackground(Color.WHITE);
	        formPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
	        
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.fill = GridBagConstraints.HORIZONTAL;
	        gbc.insets = new Insets(10, 0, 10, 0);
	        
	        // Login
	        JLabel loginLabel = new JLabel("Identifiant");
	        loginLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        formPanel.add(loginLabel, gbc);
	        
	        loginField = new JTextField();
	        loginField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        loginField.setPreferredSize(new Dimension(300, 35));
	        loginField.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(new Color(206, 212, 218)),
	            BorderFactory.createEmptyBorder(5, 10, 5, 10)
	        ));
	        gbc.gridy = 1;
	        formPanel.add(loginField, gbc);
	        
	        // Mot de passe
	        JLabel passwordLabel = new JLabel("Mot de passe");
	        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
	        gbc.gridy = 2;
	        formPanel.add(passwordLabel, gbc);
	        
	        passwordField = new JPasswordField();
	        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        passwordField.setPreferredSize(new Dimension(300, 35));
	        passwordField.setBorder(BorderFactory.createCompoundBorder(
	            BorderFactory.createLineBorder(new Color(206, 212, 218)),
	            BorderFactory.createEmptyBorder(5, 10, 5, 10)
	        ));
	        gbc.gridy = 3;
	        formPanel.add(passwordField, gbc);
	        
	        // Boutons
	        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
	        buttonPanel.setBackground(Color.WHITE);
	        
	        loginButton = new CustomButton("Se connecter", CustomButton.ButtonType.PRIMARY);
	        loginButton.setPreferredSize(new Dimension(150, 40));
	        
	        quitButton = new CustomButton("Quitter", CustomButton.ButtonType.SECONDARY);
	        quitButton.setPreferredSize(new Dimension(100, 40));
	        
	        buttonPanel.add(loginButton);
	        buttonPanel.add(quitButton);
	        
	        gbc.gridy = 4;
	        gbc.insets = new Insets(20, 0, 0, 0);
	        formPanel.add(buttonPanel, gbc);
	        
	        mainPanel.add(headerPanel, BorderLayout.NORTH);
	        mainPanel.add(formPanel, BorderLayout.CENTER);
	        
	        add(mainPanel);
	    }
	    
	    private void setupListeners() {
	        // Connexion au clic
	        loginButton.addActionListener(e -> handleLogin());
	        
	        // Connexion avec Enter
	        passwordField.addActionListener(e -> handleLogin());
	        
	        // Quitter
	        quitButton.addActionListener(e -> {
	            int confirm = JOptionPane.showConfirmDialog(
	                this,
	                "Voulez-vous vraiment quitter l'application ?",
	                "Confirmation",
	                JOptionPane.YES_NO_OPTION
	            );
	            if (confirm == JOptionPane.YES_OPTION) {
	                System.exit(0);
	            }
	        });
	    }
	    
	    private void handleLogin() {
	        String login = loginField.getText();
	        String password = new String(passwordField.getPassword());
	        
	        if (loginController.login(login, password)) {
	            // Ouvrir la fenêtre principale
	            new MainView(loginController.getUtilisateurConnecte()).setVisible(true);
	            dispose();
	        } else {
	            passwordField.setText("");
	            loginField.requestFocus();
	        }
	    }
	    
	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> {
	            new LoginView().setVisible(true);
	        });
	    }

}
