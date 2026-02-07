// ============================================
// DashboardView.java
// ============================================
package com.pharmacie.views;

import javax.swing.*;
import javax.swing.border.*;

import com.pharmacie.model.Utilisateur;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardView extends JPanel {
	private Utilisateur utilisateurConnecte;
    
    // Composants principaux
    private JLabel lblBienvenue;
    private JLabel lblDateHeure;
    private Timer timerDateHeure;
    
    // Panneaux de statistiques
    private JPanel panelVentesJour;
    private JPanel panelProduitsStock;
    private JPanel panelEmployesActifs;
    private JPanel panelVentesMois;
    
    // Labels pour les statistiques
    private JLabel lblVentesJourValeur;
    private JLabel lblProduitsStockValeur;
    private JLabel lblEmployesActifsValeur;
    private JLabel lblVentesMoisValeur;
    
    // Panneaux d'alertes
    private JPanel panelAlertes;
    private JTextArea txtAlertes;
    
    // Panneau d'actions rapides
    private JPanel panelActionsRapides;
    
    // Couleurs du thème
    private static final Color COULEUR_PRIMAIRE = new Color(41, 128, 185);
    private static final Color COULEUR_SUCCESS = new Color(39, 174, 96);
    private static final Color COULEUR_WARNING = new Color(243, 156, 18);
    private static final Color COULEUR_DANGER = new Color(231, 76, 60);
    private static final Color COULEUR_INFO = new Color(52, 152, 219);
    private static final Color COULEUR_BACKGROUND = new Color(236, 240, 241);
    
    /**
     * Constructeur
     */
    public DashboardView(Utilisateur utilisateur) {
    	this.utilisateurConnecte = utilisateur;
        initComponents();
        demarrerHorloge();
    }
    


	/**
     * Initialisation des composants
     */
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COULEUR_BACKGROUND);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // En-tête
        add(creerPanelEnTete(), BorderLayout.NORTH);
        
        // Contenu principal
        add(creerPanelContenu(), BorderLayout.CENTER);
        
        // Pied de page
        add(creerPanelPiedDePage(), BorderLayout.SOUTH);
    }
    
    /**
     * Création du panel d'en-tête
     */
    private JPanel creerPanelEnTete() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // Titre et message de bienvenue
        JPanel panelGauche = new JPanel(new GridLayout(2, 1, 0, 5));
        panelGauche.setBackground(Color.WHITE);
        
        JLabel lblTitre = new JLabel("Tableau de Bord");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitre.setForeground(COULEUR_PRIMAIRE);
        
        lblBienvenue = new JLabel("Bienvenue dans votre espace de gestion");
        lblBienvenue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBienvenue.setForeground(new Color(127, 140, 141));
        
        panelGauche.add(lblTitre);
        panelGauche.add(lblBienvenue);
        
        // Date et heure
        JPanel panelDroit = new JPanel(new BorderLayout());
        panelDroit.setBackground(Color.WHITE);
        
        lblDateHeure = new JLabel();
        lblDateHeure.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblDateHeure.setForeground(COULEUR_PRIMAIRE);
        lblDateHeure.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panelDroit.add(lblDateHeure, BorderLayout.NORTH);
        
        panel.add(panelGauche, BorderLayout.WEST);
        panel.add(panelDroit, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Création du panel de contenu principal
     */
    private JPanel creerPanelContenu() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(COULEUR_BACKGROUND);
        
        // Panel supérieur - Statistiques
        panel.add(creerPanelStatistiques(), BorderLayout.NORTH);
        
        // Panel central - Division en deux colonnes
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(600);
        splitPane.setResizeWeight(0.6);
        splitPane.setBorder(null);
        
        // Colonne gauche - Alertes et notifications
        splitPane.setLeftComponent(creerPanelAlertes());
        
        // Colonne droite - Actions rapides
        splitPane.setRightComponent(creerPanelActionsRapides(this.utilisateurConnecte));
        
        panel.add(splitPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Création du panel de statistiques avec les KPIs
     */
    private JPanel creerPanelStatistiques() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(COULEUR_BACKGROUND);
        
        // KPI 1 - Ventes du jour
        panelVentesJour = creerCarteStatistique(
            "Ventes du jour",
            "0 FCFA",
            "💰",
            COULEUR_SUCCESS
        );
        lblVentesJourValeur = (JLabel) ((JPanel) panelVentesJour.getComponent(1)).getComponent(0);
        
        // KPI 2 - Produits en stock
        panelProduitsStock = creerCarteStatistique(
            "Produits en stock",
            "0",
            "📦",
            COULEUR_INFO
        );
        lblProduitsStockValeur = (JLabel) ((JPanel) panelProduitsStock.getComponent(1)).getComponent(0);
        
        // KPI 3 - Employés actifs
        panelEmployesActifs = creerCarteStatistique(
            "Employés actifs",
            "0",
            "👥",
            COULEUR_PRIMAIRE
        );
        lblEmployesActifsValeur = (JLabel) ((JPanel) panelEmployesActifs.getComponent(1)).getComponent(0);
        
        // KPI 4 - Ventes du mois
        panelVentesMois = creerCarteStatistique(
            "Ventes du mois",
            "0 FCFA",
            "📈",
            COULEUR_WARNING
        );
        lblVentesMoisValeur = (JLabel) ((JPanel) panelVentesMois.getComponent(1)).getComponent(0);
        
        panel.add(panelVentesJour);
        panel.add(panelProduitsStock);
        panel.add(panelEmployesActifs);
        panel.add(panelVentesMois);
        
        return panel;
    }
    
    /**
     * Création d'une carte de statistique
     */
    private JPanel creerCarteStatistique(String titre, String valeur, String icone, Color couleur) {
        JPanel carte = new JPanel(new BorderLayout(10, 10));
        carte.setBackground(Color.WHITE);
        carte.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Icône
        JLabel lblIcone = new JLabel(icone);
        lblIcone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcone.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Contenu
        JPanel panelContenu = new JPanel(new GridLayout(2, 1, 0, 5));
        panelContenu.setBackground(Color.WHITE);
        
        JLabel lblValeur = new JLabel(valeur);
        lblValeur.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValeur.setForeground(couleur);
        lblValeur.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitre.setForeground(new Color(127, 140, 141));
        lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
        
        panelContenu.add(lblValeur);
        panelContenu.add(lblTitre);
        
        carte.add(lblIcone, BorderLayout.WEST);
        carte.add(panelContenu, BorderLayout.CENTER);
        
        return carte;
    }
    
    /**
     * Création du panel d'alertes et notifications
     */
    private JPanel creerPanelAlertes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // En-tête
        JLabel lblTitre = new JLabel("🔔 Alertes et Notifications");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitre.setForeground(COULEUR_PRIMAIRE);
        
        // Zone de texte pour les alertes
        txtAlertes = new JTextArea();
        txtAlertes.setEditable(false);
        txtAlertes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtAlertes.setLineWrap(true);
        txtAlertes.setWrapStyleWord(true);
        txtAlertes.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Ajout d'alertes par défaut
        ajouterAlerte("⚠️ 5 produits en rupture de stock");
        ajouterAlerte("📅 Garde planifiée pour ce week-end");
        ajouterAlerte("⏰ 3 produits arrivent à expiration dans 30 jours");
        
        JScrollPane scrollPane = new JScrollPane(txtAlertes);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollPane.setPreferredSize(new Dimension(0, 300));
        
        panel.add(lblTitre, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Création du panel d'actions rapides
     */
    private JPanel creerPanelActionsRapides(Utilisateur utilisateur) {
    	if(utilisateur.getRole().equals("Pharmacien")) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        // En-tête
        JLabel lblTitre = new JLabel("⚡ Actions Rapides");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitre.setForeground(COULEUR_PRIMAIRE);
        
        // Panel des boutons
        JPanel panelBoutons = new JPanel(new GridLayout(6, 1, 0, 10));
        panelBoutons.setBackground(Color.WHITE);
        
        JButton btnNouvelleVente = creerBoutonAction("🛒 Nouvelle Vente", COULEUR_SUCCESS);
        JButton btnGestionStock = creerBoutonAction("📦 Gestion du Stock", COULEUR_INFO);
        JButton btnGestionEmployes = creerBoutonAction("👥 Gestion Employés", COULEUR_PRIMAIRE);
        JButton btnPlanifierGarde = creerBoutonAction("🕐 Planifier une Garde", COULEUR_WARNING);
        JButton btnRapports = creerBoutonAction("📊 Voir les Rapports", COULEUR_DANGER);
        JButton btnParametres = creerBoutonAction("⚙️ Paramètres", new Color(127, 140, 141));
        
        panelBoutons.add(btnNouvelleVente);
        panelBoutons.add(btnGestionStock);
        panelBoutons.add(btnGestionEmployes);
        panelBoutons.add(btnPlanifierGarde);
        panelBoutons.add(btnRapports);
        panelBoutons.add(btnParametres);
        
        panel.add(lblTitre, BorderLayout.NORTH);
        panel.add(panelBoutons, BorderLayout.CENTER);
        
        return panel;
    	}
    	return null;
    }
    
    /**
     * Création d'un bouton d'action
     */
    private JButton creerBoutonAction(String texte, Color couleur) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        bouton.setForeground(Color.WHITE);
        bouton.setBackground(couleur);
        bouton.setFocusPainted(false);
        bouton.setBorderPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bouton.setPreferredSize(new Dimension(0, 45));
        
        // Effet hover
        bouton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bouton.setBackground(couleur.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                bouton.setBackground(couleur);
            }
        });
        
        return bouton;
    }
    
    /**
     * Création du panel de pied de page
     */
    private JPanel creerPanelPiedDePage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(1, 0, 0, 0, new Color(189, 195, 199)),
            new EmptyBorder(10, 20, 10, 20)
        ));
        
        JLabel lblCopyright = new JLabel("© 2025 Système de Gestion de Pharmacies - Tous droits réservés");
        lblCopyright.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCopyright.setForeground(new Color(127, 140, 141));
        
        JLabel lblVersion = new JLabel("Version 1.0.0");
        lblVersion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblVersion.setForeground(new Color(127, 140, 141));
        
        panel.add(lblCopyright, BorderLayout.WEST);
        panel.add(lblVersion, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Démarrage de l'horloge
     */
    private void demarrerHorloge() {
        timerDateHeure = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mettreAJourDateHeure();
            }
        });
        timerDateHeure.start();
        mettreAJourDateHeure();
    }
    
    /**
     * Mise à jour de la date et heure
     */
    private void mettreAJourDateHeure() {
        LocalDateTime maintenant = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy - HH:mm:ss");
        lblDateHeure.setText(maintenant.format(formatter));
    }
    
    /**
     * Ajout d'une alerte
     */
    public void ajouterAlerte(String message) {
        String texteActuel = txtAlertes.getText();
        if (!texteActuel.isEmpty()) {
            texteActuel += "\n\n";
        }
        
        LocalDateTime maintenant = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        
        txtAlertes.setText(texteActuel + "[" + maintenant.format(formatter) + "] " + message);
        txtAlertes.setCaretPosition(txtAlertes.getDocument().getLength());
    }
    
    /**
     * Mise à jour des statistiques
     */
    public void mettreAJourStatistiques(String ventesJour, String produitsStock, 
                                        String employesActifs, String ventesMois) {
        lblVentesJourValeur.setText(ventesJour);
        lblProduitsStockValeur.setText(produitsStock);
        lblEmployesActifsValeur.setText(employesActifs);
        lblVentesMoisValeur.setText(ventesMois);
    }
    
    /**
     * Définir le message de bienvenue personnalisé
     */
    public void setBienvenue(String nomUtilisateur) {
        lblBienvenue.setText("Bienvenue, " + nomUtilisateur + " !");
    }
    
    /**
     * Ajout d'écouteurs pour les boutons d'action
     */
    public void ajouterEcouteurNouvelleVente(ActionListener listener) {
        Component[] components = ((JPanel)panelActionsRapides.getComponent(1)).getComponents();
        ((JButton)components[0]).addActionListener(listener);
    }
    
    public void ajouterEcouteurGestionStock(ActionListener listener) {
        Component[] components = ((JPanel)panelActionsRapides.getComponent(1)).getComponents();
        ((JButton)components[1]).addActionListener(listener);
    }
    
    public void ajouterEcouteurGestionEmployes(ActionListener listener) {
        Component[] components = ((JPanel)panelActionsRapides.getComponent(1)).getComponents();
        ((JButton)components[2]).addActionListener(listener);
    }
    
    public void ajouterEcouteurPlanifierGarde(ActionListener listener) {
        Component[] components = ((JPanel)panelActionsRapides.getComponent(1)).getComponents();
        ((JButton)components[3]).addActionListener(listener);
    }
    
    public void ajouterEcouteurRapports(ActionListener listener) {
        Component[] components = ((JPanel)panelActionsRapides.getComponent(1)).getComponents();
        ((JButton)components[4]).addActionListener(listener);
    }
    
    public void ajouterEcouteurParametres(ActionListener listener) {
        Component[] components = ((JPanel)panelActionsRapides.getComponent(1)).getComponents();
        ((JButton)components[5]).addActionListener(listener);
    }
    
    /**
     * Nettoyage des ressources
     */
    public void nettoyer() {
        if (timerDateHeure != null) {
            timerDateHeure.stop();
        }
    }
    
    /**
     * Méthode main pour tester la vue
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Dashboard - Gestion Pharmacie");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            
//            DashboardView dashboard = new DashboardView(this.utilisateurConnecte);
//            dashboard.setBienvenue("Dr. Martin");
//            dashboard.mettreAJourStatistiques("125,000 FCFA", "245", "8", "3,500,000 FCFA");
//            
//            frame.add(dashboard);
//            frame.setSize(1200, 800);
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
        });
    }
}
