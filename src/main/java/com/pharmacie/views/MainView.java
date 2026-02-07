package com.pharmacie.views;

import com.pharmacie.model.Utilisateur;
import com.pharmacie.views.components.CustomButton;

import javax.swing.*;
import java.awt.*;


public class MainView extends JFrame{
	 private Utilisateur utilisateurConnecte;
	    
	    private JPanel sidebarPanel;
	    private JPanel contentPanel;
	    private JLabel userLabel;
	    
	    public MainView(Utilisateur utilisateur) {
	        this.utilisateurConnecte = utilisateur;
	        initComponents();
	        setupLayout();
	        setupMenus();
	        
	        // Afficher le dashboard par défaut
	        showDashboard();
	    }
	    
	    private void initComponents() {
	        setTitle("Gestion des Pharmacies - " + utilisateurConnecte.getLogin());
	        setSize(1400, 800);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        
	        // Look and Feel
	        try {
	            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    private void setupLayout() {
	        setLayout(new BorderLayout());
	        
	        // Barre de menu supérieure
	        JPanel topBar = createTopBar();
	        add(topBar, BorderLayout.NORTH);
	        
	        // Sidebar (menu latéral)
	        sidebarPanel = createSidebar();
	        add(sidebarPanel, BorderLayout.WEST);
	        
	        // Panel de contenu principal
	        contentPanel = new JPanel();
	        contentPanel.setLayout(new BorderLayout());
	        contentPanel.setBackground(new Color(248, 249, 250));
	        add(contentPanel, BorderLayout.CENTER);
	    }
	    
	    private JPanel createTopBar() {
	        JPanel topBar = new JPanel(new BorderLayout());
	        topBar.setBackground(new Color(52, 58, 64));
	        topBar.setPreferredSize(new Dimension(0, 60));
	        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
	        
	        // Logo et titre
	        JLabel titleLabel = new JLabel("⚕ GESTION DES PHARMACIES");
	        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
	        titleLabel.setForeground(Color.WHITE);
	        
	        // Infos utilisateur
	        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
	        userPanel.setBackground(new Color(52, 58, 64));
	        
	        userLabel = new JLabel("👤 " + utilisateurConnecte.getLogin() + " (" + utilisateurConnecte.getRole().getLibelle() + ")");
	        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
	        userLabel.setForeground(Color.WHITE);
	        
	        CustomButton logoutButton = new CustomButton("Déconnexion", CustomButton.ButtonType.DANGER);
	        logoutButton.setPreferredSize(new Dimension(120, 35));
	        logoutButton.addActionListener(e -> handleLogout());
	        
	        userPanel.add(userLabel);
	        userPanel.add(logoutButton);
	        
	        topBar.add(titleLabel, BorderLayout.WEST);
	        topBar.add(userPanel, BorderLayout.EAST);
	        
	        return topBar;
	    }
	    
	    private JPanel createSidebar() {
	        JPanel sidebar = new JPanel();
	        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
	        sidebar.setBackground(new Color(33, 37, 41));
	        sidebar.setPreferredSize(new Dimension(250, 0));
	        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
	        
	        return sidebar;
	    }
	    
	    private void setupMenus() {
	        sidebarPanel.removeAll();
	        
	        // Tableau de bord
	        addMenuButton("📊 Tableau de bord", e -> showDashboard());
	        
	        // Gestion des pharmacies (Admin uniquement)
	        if (utilisateurConnecte.getRole().peutGererPharmacies()) {
	            addMenuButton("🏥 Pharmacies", e -> showPharmacies());
	        }
	        
	        // Gestion des produits
	        if (utilisateurConnecte.getRole().peutGererProduits()) {
	            addMenuButton("💊 Produits", e -> showProduits());
	        }
	        
	        // Ventes
	        if (utilisateurConnecte.getRole().peutEffectuerVentes()) {
	            addMenuButton("🛒 Ventes", e -> showVentes());
	        }
	        
	        // Employés (Admin uniquement)
	        if (utilisateurConnecte.getRole().peutGererEmployes()) {
	            addMenuButton("👥 Employés", e -> showEmployes());
	        }
	        
	        // Gardes
	        if (utilisateurConnecte.getRole().peutGererGardes()) {
	            addMenuButton("🌙 Gardes", e -> showGardes());
	        }
	        
	        // Horaires
	        if (utilisateurConnecte.getRole().peutGererHoraires()) {
	            addMenuButton("⏰ Horaires", e -> showHoraires());
	        }
	        
	        // Paies (Admin uniquement)
	        if (utilisateurConnecte.getRole().peutGererPaies()) {
	            addMenuButton("💰 Paies", e -> showPaies());
	        }
	        
	        // Inventaires
	        if (utilisateurConnecte.getRole().peutGererProduits()) {
	            addMenuButton("📦 Inventaires", e -> showInventaires());
	        }
	        
	        // Rapports
	        if (utilisateurConnecte.getRole().peutVoirRapports()) {
	            addMenuButton("📈 Rapports", e -> showRapports());
	        }
	        
	        sidebarPanel.revalidate();
	        sidebarPanel.repaint();
	    }
	    
	    private void addMenuButton(String text, java.awt.event.ActionListener action) {
	        JButton menuButton = new JButton(text);
	        menuButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
	        menuButton.setForeground(Color.WHITE);
	        menuButton.setBackground(new Color(33, 37, 41));
	        menuButton.setFocusPainted(false);
	        menuButton.setBorderPainted(false);
	        menuButton.setHorizontalAlignment(SwingConstants.LEFT);
	        menuButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
	        menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
	        menuButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
	        
	        menuButton.addMouseListener(new java.awt.event.MouseAdapter() {
	            public void mouseEntered(java.awt.event.MouseEvent evt) {
	                menuButton.setBackground(new Color(52, 58, 64));
	            }
	            public void mouseExited(java.awt.event.MouseEvent evt) {
	                menuButton.setBackground(new Color(33, 37, 41));
	            }
	        });
	        
	        menuButton.addActionListener(action);
	        
	        sidebarPanel.add(menuButton);
	        sidebarPanel.add(Box.createVerticalStrut(5));
	    }
	    
	    private void showDashboard() {
	        contentPanel.removeAll();
	        DashboardView dashboard = new DashboardView(this.utilisateurConnecte);
	        contentPanel.add(dashboard, BorderLayout.CENTER);
	        contentPanel.revalidate();
	        contentPanel.repaint();
	    }
	    
	    private void showPharmacies() {
	        contentPanel.removeAll();
	        JLabel label = new JLabel("Module Pharmacies - En construction");
	        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
	        label.setHorizontalAlignment(SwingConstants.CENTER);
	        contentPanel.add(label, BorderLayout.CENTER);
	        contentPanel.revalidate();
	        contentPanel.repaint();
	    }
	    
	    private void showProduits() {
	        contentPanel.removeAll();
	        JLabel label = new JLabel("Module Produits - En construction");
	        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
	        label.setHorizontalAlignment(SwingConstants.CENTER);
	        contentPanel.add(label, BorderLayout.CENTER);
	        contentPanel.revalidate();
	        contentPanel.repaint();
	    }
	    
	    private void showVentes() {
	        contentPanel.removeAll();
	        JLabel label = new JLabel("Module Ventes - En construction");
	        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
	        label.setHorizontalAlignment(SwingConstants.CENTER);
	        contentPanel.add(label, BorderLayout.CENTER);
	        contentPanel.revalidate();
	        contentPanel.repaint();
	    }
	    
	    private void showEmployes() {
	        contentPanel.removeAll();
	        JLabel label = new JLabel("Module Employés - En construction");
	        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
	        label.setHorizontalAlignment(SwingConstants.CENTER);
	        contentPanel.add(label, BorderLayout.CENTER);
	        contentPanel.revalidate();
	        contentPanel.repaint();
	    }
	    
	    private void showGardes() {
	        contentPanel.removeAll();
	        JLabel label = new JLabel("Module Gardes - En construction");
	        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
	        label.setHorizontalAlignment(SwingConstants.CENTER);
	        contentPanel.add(label, BorderLayout.CENTER);
	        contentPanel.revalidate();
	        contentPanel.repaint();
	    }
	    
	    private void showHoraires() {
	        contentPanel.removeAll();
	        JLabel label = new JLabel("Module Horaires - En construction");
	        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
	        label.setHorizontalAlignment(SwingConstants.CENTER);
	        contentPanel.add(label, BorderLayout.CENTER);
	        contentPanel.revalidate();
	        contentPanel.repaint();
	    }
	    
	    private void showPaies() {
	        contentPanel.removeAll();
	        JLabel label = new JLabel("Module Paies - En construction");
	        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
	        label.setHorizontalAlignment(SwingConstants.CENTER);
	        contentPanel.add(label, BorderLayout.CENTER);
	        contentPanel.revalidate();
	        contentPanel.repaint();
	    }
	    
	    private void showInventaires() {
	        contentPanel.removeAll();
	        JLabel label = new JLabel("Module Inventaires - En construction");
	        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
	        label.setHorizontalAlignment(SwingConstants.CENTER);
	        contentPanel.add(label, BorderLayout.CENTER);
	        contentPanel.revalidate();
	        contentPanel.repaint();
	    }
	    
	    private void showRapports() {
	        contentPanel.removeAll();
	        JLabel label = new JLabel("Module Rapports - En construction");
	        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
	        label.setHorizontalAlignment(SwingConstants.CENTER);
	        contentPanel.add(label, BorderLayout.CENTER);
	        contentPanel.revalidate();
	        contentPanel.repaint();
	    }
	    
	    private void handleLogout() {
	        int confirm = JOptionPane.showConfirmDialog(
	            this,
	            "Voulez-vous vraiment vous déconnecter ?",
	            "Confirmation",
	            JOptionPane.YES_NO_OPTION
	        );
	        
	        if (confirm == JOptionPane.YES_OPTION) {
	            dispose();
	            new LoginView().setVisible(true);
	        }
	    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub


	}

}
