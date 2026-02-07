package com.pharmacie.services;

import com.pharmacie.dao.interfaces.ProduitDAO;
import com.pharmacie.dao.impl.ProduitDAOImpl;
import com.pharmacie.model.Produit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockService {
    
    private final ProduitDAO produitDAO;
    
    public StockService() {
        this.produitDAO = new ProduitDAOImpl();
    }
    
    /**
     * Obtenir les alertes de stock
     */
    public Map<String, List<Produit>> getAlertesStock(Long idPharmacie) {
        Map<String, List<Produit>> alertes = new HashMap<>();
        
        // Produits en rupture
        alertes.put("rupture", produitDAO.findEnRuptureStock(idPharmacie));
        
        // Produits périmés
        alertes.put("perimes", getProduitsPerimes(idPharmacie));
        
        // Produits proches de la péremption (30 jours)
        alertes.put("peremption_proche", getProduitsPeremptionProche(idPharmacie, 30));
        
        return alertes;
    }
    
    /**
     * Obtenir les produits périmés
     */
    public List<Produit> getProduitsPerimes(Long idPharmacie) {
        List<Produit> produits = produitDAO.findByPharmacie(idPharmacie);
        List<Produit> perimes = new ArrayList<>();
        
        for (Produit produit : produits) {
            if (produit.estPerime()) {
                perimes.add(produit);
            }
        }
        
        return perimes;
    }
    
    /**
     * Obtenir les produits proches de la péremption
     */
    public List<Produit> getProduitsPeremptionProche(Long idPharmacie, int joursAvant) {
        return produitDAO.findPerimesOuProches(idPharmacie, joursAvant);
    }
    
    /**
     * Calculer la valeur totale du stock
     */
    public double getValeurStock(Long idPharmacie) {
        List<Produit> produits = produitDAO.findByPharmacie(idPharmacie);
        return produits.stream()
                .mapToDouble(p -> p.getPrixUnitaire().doubleValue() * p.getQuantiteStock())
                .sum();
    }
    
    /**
     * Obtenir le nombre total d'articles en stock
     */
    public int getTotalArticles(Long idPharmacie) {
        List<Produit> produits = produitDAO.findByPharmacie(idPharmacie);
        return produits.stream()
                .mapToInt(Produit::getQuantiteStock)
                .sum();
    }
    
    /**
     * Obtenir les statistiques de stock par catégorie
     */
    public Map<String, Integer> getStatistiquesParCategorie(Long idPharmacie) {
        List<Produit> produits = produitDAO.findByPharmacie(idPharmacie);
        Map<String, Integer> stats = new HashMap<>();
        
        for (Produit produit : produits) {
            String categorie = produit.getCategorie();
            stats.put(categorie, stats.getOrDefault(categorie, 0) + produit.getQuantiteStock());
        }
        
        return stats;
    }
    
    /**
     * Obtenir le taux de rotation du stock
     * (à implémenter avec les données de ventes)
     */
    public double getTauxRotation(Long idPharmacie, LocalDate debut, LocalDate fin) {
        // Calcul simplifié : nombre de ventes / stock moyen
        // À implémenter avec les données réelles
        return 0.0;
    }
    
    /**
     * Générer un rapport de stock
     */
    public Map<String, Object> genererRapportStock(Long idPharmacie) {
        Map<String, Object> rapport = new HashMap<>();
        
        rapport.put("valeur_totale", getValeurStock(idPharmacie));
        rapport.put("total_articles", getTotalArticles(idPharmacie));
        rapport.put("nombre_produits", produitDAO.findByPharmacie(idPharmacie).size());
        rapport.put("produits_rupture", produitDAO.findEnRuptureStock(idPharmacie).size());
        rapport.put("produits_perimes", getProduitsPerimes(idPharmacie).size());
        rapport.put("stats_categories", getStatistiquesParCategorie(idPharmacie));
        
        return rapport;
    }
}
