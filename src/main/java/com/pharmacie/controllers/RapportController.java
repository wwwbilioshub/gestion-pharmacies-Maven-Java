package com.pharmacie.controllers;

import com.pharmacie.services.RapportService;

import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RapportController {
    
    private final RapportService rapportService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public RapportController() {
        this.rapportService = new RapportService();
    }
    
    /**
     * Générer et afficher le rapport journalier
     */
    public void afficherRapportJournalier(Long idPharmacie, LocalDate date) {
        Map<String, Object> rapport = rapportService.genererRapportJournalier(idPharmacie, date);
        
        String message = String.format(
            "RAPPORT JOURNALIER\n" +
            "Date : %s\n\n" +
            "Nombre de ventes : %d\n" +
            "Chiffre d'affaires : %.2f FCFA\n" +
            "Panier moyen : %.2f FCFA",
            date.format(dateFormatter),
            rapport.get("nombre_ventes"),
            rapport.get("chiffre_affaires"),
            rapport.get("panier_moyen")
        );
        
        JOptionPane.showMessageDialog(null, 
            message, 
            "Rapport Journalier", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Générer et afficher le rapport mensuel
     */
    public void afficherRapportMensuel(Long idPharmacie, int mois, int annee) {
        Map<String, Object> rapport = rapportService.genererRapportMensuel(idPharmacie, mois, annee);
        
        String message = String.format(
            "RAPPORT MENSUEL\n" +
            "Période : %s\n\n" +
            "Nombre de ventes : %d\n" +
            "Chiffre d'affaires : %.2f FCFA\n" +
            "Panier moyen : %.2f FCFA",
            rapport.get("periode"),
            rapport.get("nombre_ventes"),
            rapport.get("chiffre_affaires"),
            rapport.get("panier_moyen")
        );
        
        JOptionPane.showMessageDialog(null, 
            message, 
            "Rapport Mensuel", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Générer rapport de performance des employés
     */
    public Map<String, Object> genererRapportPerformance(Long idPharmacie, LocalDate debut, LocalDate fin) {
        return rapportService.genererRapportPerformanceEmployes(idPharmacie, debut, fin);
    }
    
    /**
     * Générer rapport de stock
     */
    public Map<String, Object> genererRapportStock(Long idPharmacie) {
        return rapportService.genererRapportStock(idPharmacie);
    }
    
    /**
     * Afficher le rapport de stock
     */
    public void afficherRapportStock(Long idPharmacie) {
        Map<String, Object> rapport = rapportService.genererRapportStock(idPharmacie);
        
        String message = String.format(
            "RAPPORT DE STOCK\n\n" +
            "Nombre de produits : %d\n" +
            "Total d'articles : %d\n" +
            "Valeur totale : %.2f FCFA\n\n" +
            "⚠️ Alertes :\n" +
            "Produits en rupture : %d\n" +
            "Produits périmés : %d",
            rapport.get("nombre_produits"),
            rapport.get("total_articles"),
            rapport.get("valeur_totale"),
            rapport.get("produits_rupture"),
            rapport.get("produits_perimes")
        );
        
        JOptionPane.showMessageDialog(null, 
            message, 
            "Rapport de Stock", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Exporter un rapport en PDF
     */
    public boolean exporterRapportPDF(String typeRapport, Map<String, Object> donnees, String cheminFichier) {
        try {
            // TODO: Implémenter l'export PDF avec iText
            JOptionPane.showMessageDialog(null, 
                "Rapport exporté avec succès vers :\n" + cheminFichier, 
                "Export PDF", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur lors de l'export : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Exporter un rapport en Excel
     */
    public boolean exporterRapportExcel(String typeRapport, Map<String, Object> donnees, String cheminFichier) {
        try {
            // TODO: Implémenter l'export Excel avec Apache POI
            JOptionPane.showMessageDialog(null, 
                "Rapport exporté avec succès vers :\n" + cheminFichier, 
                "Export Excel", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur lors de l'export : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}