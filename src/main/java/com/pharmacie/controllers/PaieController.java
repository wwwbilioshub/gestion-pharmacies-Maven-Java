package com.pharmacie.controllers;

import com.pharmacie.model.Paie;
import com.pharmacie.services.PaieService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PaieController {
    
    private final PaieService paieService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public PaieController() {
        this.paieService = new PaieService();
    }
    
    /**
     * Générer la paie d'un employé
     */
    public boolean genererPaie(Long idEmploye, int mois, int annee) {
        try {
            paieService.genererPaie(idEmploye, mois, annee);
            JOptionPane.showMessageDialog(null, 
                "Paie générée avec succès", 
                "Succès", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, 
                e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur lors de la génération : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Générer les paies pour toute une pharmacie
     */
    public void genererPaiesPharmacie(Long idPharmacie, int mois, int annee) {
        try {
            List<Paie> paies = paieService.genererPaiesPourPharmacie(idPharmacie, mois, annee);
            JOptionPane.showMessageDialog(null, 
                paies.size() + " paies générées avec succès", 
                "Succès", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Charger les paies d'un employé
     */
    public void chargerPaies(Long idEmploye, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Paie> paies = paieService.getPaiesByEmploye(idEmploye);
        
        for (Paie paie : paies) {
            Object[] row = {
                paie.getIdPaie(),
                paie.getPeriodeLongue(),
                paie.getMontantBase() + " FCFA",
                paie.getHeuresSupplementaires() + " FCFA",
                paie.getPrimeGarde() + " FCFA",
                paie.getMontantTotal() + " FCFA",
                paie.isPayee() ? "Payée" : "Non payée"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Charger les paies d'une période
     */
    public void chargerPaiesPeriode(int mois, int annee, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Paie> paies = paieService.getPaiesByPeriode(mois, annee);
        
        for (Paie paie : paies) {
            Object[] row = {
                paie.getIdPaie(),
                paie.getNomEmploye(),
                paie.getMontantTotal() + " FCFA",
                paie.isPayee() ? "✓" : "✗"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Marquer une paie comme payée
     */
    public boolean marquerPayee(Long id) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Confirmer le paiement de cette paie ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = paieService.marquerPayee(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, 
                        "Paie marquée comme payée", 
                        "Succès", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                return success;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Erreur : " + e.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }
    
    /**
     * Afficher les statistiques de paie
     */
    public void afficherStatistiquesPaie(int mois, int annee) {
        double totalSalaires = paieService.getTotalSalaires(mois, annee);
        
        String message = String.format(
            "Période : %s %d\n\n" +
            "Total des salaires : %.2f FCFA",
            getMonthName(mois),
            annee,
            totalSalaires
        );
        
        JOptionPane.showMessageDialog(null, 
            message, 
            "Statistiques de paie", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private String getMonthName(int mois) {
        String[] moisNoms = {
            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        };
        return moisNoms[mois - 1];
    }
}
