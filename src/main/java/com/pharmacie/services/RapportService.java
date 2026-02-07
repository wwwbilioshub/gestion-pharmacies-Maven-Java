package com.pharmacie.services;

import com.pharmacie.dao.interfaces.VenteDAO;
import com.pharmacie.dao.interfaces.EmployeDAO;
import com.pharmacie.dao.interfaces.ProduitDAO;
import com.pharmacie.dao.impl.VenteDAOImpl;
import com.pharmacie.dao.impl.EmployeDAOImpl;
import com.pharmacie.dao.impl.ProduitDAOImpl;
import com.pharmacie.model.Employe;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RapportService {
    
    private final VenteDAO venteDAO;
    private final EmployeDAO employeDAO;
    private final ProduitDAO produitDAO;
    private final StockService stockService;
    
    public RapportService() {
        this.venteDAO = new VenteDAOImpl();
        this.employeDAO = new EmployeDAOImpl();
        this.produitDAO = new ProduitDAOImpl();
        this.stockService = new StockService();
    }
    
    /**
     * Générer le rapport journalier
     */
    public Map<String, Object> genererRapportJournalier(Long idPharmacie, LocalDate date) {
        Map<String, Object> rapport = new HashMap<>();
        
        LocalDateTime debut = date.atStartOfDay();
        LocalDateTime fin = date.atTime(23, 59, 59);
        
        // Ventes du jour
        long nombreVentes = venteDAO.countByPeriode(idPharmacie, debut, fin);
        double chiffreAffaires = venteDAO.getChiffreAffaires(idPharmacie, debut, fin);
        
        rapport.put("date", date);
        rapport.put("nombre_ventes", nombreVentes);
        rapport.put("chiffre_affaires", chiffreAffaires);
        rapport.put("panier_moyen", nombreVentes > 0 ? chiffreAffaires / nombreVentes : 0);
        
        return rapport;
    }
    
    /**
     * Générer le rapport mensuel
     */
    public Map<String, Object> genererRapportMensuel(Long idPharmacie, int mois, int annee) {
        Map<String, Object> rapport = new HashMap<>();
        
        LocalDate debut = LocalDate.of(annee, mois, 1);
        LocalDate fin = debut.withDayOfMonth(debut.lengthOfMonth());
        
        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);
        
        // Statistiques de ventes
        long nombreVentes = venteDAO.countByPeriode(idPharmacie, debutDateTime, finDateTime);
        double chiffreAffaires = venteDAO.getChiffreAffaires(idPharmacie, debutDateTime, finDateTime);
        
        rapport.put("periode", mois + "/" + annee);
        rapport.put("nombre_ventes", nombreVentes);
        rapport.put("chiffre_affaires", chiffreAffaires);
        rapport.put("panier_moyen", nombreVentes > 0 ? chiffreAffaires / nombreVentes : 0);
        
        // Statistiques employés
        List<Object[]> statsEmployes = venteDAO.getStatistiquesParEmploye(idPharmacie, debut, fin);
        rapport.put("stats_employes", statsEmployes);
        
        return rapport;
    }
    
    /**
     * Générer le rapport de performance des employés
     */
    public Map<String, Object> genererRapportPerformanceEmployes(Long idPharmacie, LocalDate debut, LocalDate fin) {
        Map<String, Object> rapport = new HashMap<>();
        
        List<Object[]> stats = venteDAO.getStatistiquesParEmploye(idPharmacie, debut, fin);
        
        rapport.put("periode_debut", debut);
        rapport.put("periode_fin", fin);
        rapport.put("statistiques", stats);
        rapport.put("nombre_employes", employeDAO.countByPharmacie(idPharmacie));
        
        return rapport;
    }
    
    /**
     * Générer le rapport de stock
     */
    public Map<String, Object> genererRapportStock(Long idPharmacie) {
        return stockService.genererRapportStock(idPharmacie);
    }
    
    /**
     * Générer le tableau de bord
     */
    public Map<String, Object> genererTableauDeBord(Long idPharmacie) {
        Map<String, Object> dashboard = new HashMap<>();
        
        LocalDate aujourdhui = LocalDate.now();
        LocalDateTime debut = aujourdhui.atStartOfDay();
        LocalDateTime fin = aujourdhui.atTime(23, 59, 59);
        
        // Ventes du jour
        dashboard.put("ventes_jour", venteDAO.countByPeriode(idPharmacie, debut, fin));
        dashboard.put("ca_jour", venteDAO.getChiffreAffaires(idPharmacie, debut, fin));
        
        // Stock
        dashboard.put("produits_rupture", produitDAO.findEnRuptureStock(idPharmacie).size());
        dashboard.put("produits_perimes", stockService.getProduitsPerimes(idPharmacie).size());
        dashboard.put("valeur_stock", stockService.getValeurStock(idPharmacie));
        
        // Employés
        dashboard.put("employes_actifs", employeDAO.countByPharmacie(idPharmacie));
        
        return dashboard;
    }
    
    /**
     * Obtenir les produits les plus vendus
     */
    public List<Object[]> getProduitsLesPlusVendus(Long idPharmacie, int limite) {
        VenteService venteService = new VenteService();
        return venteService.getProduitsLesPlusVendus(idPharmacie, limite);
    }
    
    /**
     * Calculer l'évolution du chiffre d'affaires
     */
    public Map<String, Double> getEvolutionCA(Long idPharmacie, int nombreJours) {
        Map<String, Double> evolution = new HashMap<>();
        LocalDate aujourdhui = LocalDate.now();
        
        for (int i = nombreJours - 1; i >= 0; i--) {
            LocalDate date = aujourdhui.minusDays(i);
            LocalDateTime debut = date.atStartOfDay();
            LocalDateTime fin = date.atTime(23, 59, 59);
            
            double ca = venteDAO.getChiffreAffaires(idPharmacie, debut, fin);
            evolution.put(date.toString(), ca);
        }
        
        return evolution;
    }
}
