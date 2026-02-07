package com.pharmacie.controllers;

import com.pharmacie.services.RapportService;
import com.pharmacie.services.StockService;

import java.util.Map;

public class DashboardController {
    
    private final RapportService rapportService;
    private final StockService stockService;
    
    public DashboardController() {
        this.rapportService = new RapportService();
        this.stockService = new StockService();
    }
    
    /**
     * Obtenir les données du tableau de bord
     */
    public Map<String, Object> getTableauDeBord(Long idPharmacie) {
        return rapportService.genererTableauDeBord(idPharmacie);
    }
    
    /**
     * Obtenir les alertes de stock
     */
    public Map<String, java.util.List<com.pharmacie.model.Produit>> getAlertesStock(Long idPharmacie) {
        return stockService.getAlertesStock(idPharmacie);
    }
    
    /**
     * Obtenir l'évolution du chiffre d'affaires
     */
    public Map<String, Double> getEvolutionCA(Long idPharmacie, int nombreJours) {
        return rapportService.getEvolutionCA(idPharmacie, nombreJours);
    }
    
    /**
     * Obtenir les produits les plus vendus
     */
    public java.util.List<Object[]> getProduitsLesPlusVendus(Long idPharmacie, int limite) {
        return rapportService.getProduitsLesPlusVendus(idPharmacie, limite);
    }
    
    /**
     * Obtenir les statistiques de stock par catégorie
     */
    public Map<String, Integer> getStatistiquesStockParCategorie(Long idPharmacie) {
        return stockService.getStatistiquesParCategorie(idPharmacie);
    }
    
    /**
     * Obtenir la valeur totale du stock
     */
    public double getValeurStock(Long idPharmacie) {
        return stockService.getValeurStock(idPharmacie);
    }
    
    /**
     * Obtenir le nombre total d'articles
     */
    public int getTotalArticles(Long idPharmacie) {
        return stockService.getTotalArticles(idPharmacie);
    }
}