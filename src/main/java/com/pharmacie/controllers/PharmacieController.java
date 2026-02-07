package com.pharmacie.controllers;

import com.pharmacie.model.Pharmacie;
import com.pharmacie.services.PharmacieService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Optional;

public class PharmacieController {
    
    private final PharmacieService pharmacieService;
    
    public PharmacieController() {
        this.pharmacieService = new PharmacieService();
    }
    
    /**
     * Créer une nouvelle pharmacie
     */
    public boolean creerPharmacie(Pharmacie pharmacie) {
        try {
            pharmacieService.creerPharmacie(pharmacie);
            JOptionPane.showMessageDialog(null, 
                "Pharmacie créée avec succès", 
                "Succès", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, 
                e.getMessage(), 
                "Erreur de validation", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur lors de la création : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Modifier une pharmacie
     */
    public boolean modifierPharmacie(Pharmacie pharmacie) {
        try {
            pharmacieService.modifierPharmacie(pharmacie);
            JOptionPane.showMessageDialog(null, 
                "Pharmacie modifiée avec succès", 
                "Succès", 
                JOptionPane.INFORMATION_MESSAGE);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur lors de la modification : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Désactiver une pharmacie
     */
    public boolean desactiverPharmacie(Long id) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Voulez-vous vraiment désactiver cette pharmacie ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = pharmacieService.desactiverPharmacie(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, 
                        "Pharmacie désactivée avec succès", 
                        "Succès", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                return success;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Erreur lors de la désactivation : " + e.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }
    
    /**
     * Charger toutes les pharmacies dans un tableau
     */
    public void chargerPharmacies(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Pharmacie> pharmacies = pharmacieService.getAllPharmacies();
        
        for (Pharmacie pharmacie : pharmacies) {
            Object[] row = {
                pharmacie.getIdPharmacie(),
                pharmacie.getNom(),
                pharmacie.getAdresse(),
                pharmacie.getTelephone(),
                pharmacie.getEmail(),
                pharmacie.isEstDeGarde() ? "Oui" : "Non",
                pharmacie.isActive() ? "Active" : "Inactive"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Charger les pharmacies actives
     */
    public void chargerPharmaciesActives(DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Pharmacie> pharmacies = pharmacieService.getPharmaciesActives();
        
        for (Pharmacie pharmacie : pharmacies) {
            Object[] row = {
                pharmacie.getIdPharmacie(),
                pharmacie.getNom(),
                pharmacie.getAdresse(),
                pharmacie.getTelephone()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Rechercher des pharmacies
     */
    public void rechercherPharmacies(String nom, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Pharmacie> pharmacies = pharmacieService.rechercherParNom(nom);
        
        for (Pharmacie pharmacie : pharmacies) {
            Object[] row = {
                pharmacie.getIdPharmacie(),
                pharmacie.getNom(),
                pharmacie.getAdresse(),
                pharmacie.getTelephone(),
                pharmacie.isEstDeGarde() ? "Oui" : "Non",
                pharmacie.isActive() ? "Active" : "Inactive"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Obtenir une pharmacie par ID
     */
    public Pharmacie getPharmacie(Long id) {
        Optional<Pharmacie> pharmacieOpt = pharmacieService.getPharmacie(id);
        return pharmacieOpt.orElse(null);
    }
    
    /**
     * Changer le statut de garde
     */
    public boolean toggleGarde(Long id) {
        try {
            boolean success = pharmacieService.toggleGarde(id);
            if (success) {
                JOptionPane.showMessageDialog(null, 
                    "Statut de garde modifié", 
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
    
    /**
     * Obtenir toutes les pharmacies pour ComboBox
     */
    public List<Pharmacie> getAllPharmacies() {
        return pharmacieService.getAllPharmacies();
    }
    
    /**
     * Obtenir les pharmacies actives pour ComboBox
     */
    public List<Pharmacie> getPharmaciesActives() {
        return pharmacieService.getPharmaciesActives();
    }
}
