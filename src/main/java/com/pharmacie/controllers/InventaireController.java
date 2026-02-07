package com.pharmacie.controllers;

import com.pharmacie.model.Inventaire;
import com.pharmacie.model.LigneInventaire;
import com.pharmacie.services.InventaireService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InventaireController {
    
    private final InventaireService inventaireService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public InventaireController() {
        this.inventaireService = new InventaireService();
    }
    
    /**
     * Démarrer un nouvel inventaire
     */
    public boolean demarrerInventaire(Long idPharmacie, Long idEmploye) {
        try {
            inventaireService.demarrerInventaire(idPharmacie, idEmploye);
            JOptionPane.showMessageDialog(null, 
                "Inventaire démarré avec succès\nVous pouvez maintenant saisir les quantités réelles", 
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
                "Erreur lors du démarrage : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Terminer un inventaire
     */
    public boolean terminerInventaire(Long id) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Êtes-vous sûr d'avoir saisi toutes les quantités ?\nL'inventaire sera marqué comme terminé.", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = inventaireService.terminerInventaire(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, 
                        "Inventaire terminé avec succès", 
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
     * Valider un inventaire (mettre à jour les stocks)
     */
    public boolean validerInventaire(Long id) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "⚠️ ATTENTION ⚠️\n\n" +
            "Cette action va mettre à jour les stocks de la pharmacie.\n" +
            "Les quantités théoriques seront remplacées par les quantités réelles.\n\n" +
            "Voulez-vous continuer ?", 
            "Confirmation importante", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = inventaireService.validerInventaire(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, 
                        "Inventaire validé avec succès\nLes stocks ont été mis à jour", 
                        "Succès", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                return success;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Erreur lors de la validation : " + e.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }
    
    /**
     * Annuler un inventaire
     */
    public boolean annulerInventaire(Long id) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Voulez-vous vraiment annuler cet inventaire ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = inventaireService.annulerInventaire(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, 
                        "Inventaire annulé", 
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
     * Charger les inventaires d'une pharmacie
     */
    public void chargerInventaires(Long idPharmacie, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Inventaire> inventaires = inventaireService.getInventairesByPharmacie(idPharmacie);
        
        for (Inventaire inventaire : inventaires) {
            Object[] row = {
                inventaire.getIdInventaire(),
                inventaire.getDateInventaire().format(dateFormatter),
                inventaire.getNomEmploye(),
                inventaire.getTotalProduits(),
                inventaire.getTotalEcarts(),
                inventaire.getStatut()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Charger les lignes d'un inventaire
     */
    public void chargerLignesInventaire(Long idInventaire, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        Inventaire inventaire = inventaireService.getInventaire(idInventaire).orElse(null);
        
        if (inventaire != null) {
            for (LigneInventaire ligne : inventaire.getLignesInventaire()) {
                String statut = "";
                if (ligne.estConforme()) {
                    statut = "✓ OK";
                } else if (ligne.estExcedent()) {
                    statut = "↑ +" + ligne.getEcart();
                } else {
                    statut = "↓ " + ligne.getEcart();
                }
                
                Object[] row = {
                    ligne.getIdLigneInventaire(),
                    ligne.getNomProduit(),
                    ligne.getCategorie(),
                    ligne.getQuantiteTheorique(),
                    ligne.getQuantiteReelle(),
                    ligne.getEcart(),
                    statut
                };
                tableModel.addRow(row);
            }
        }
    }
    
    /**
     * Mettre à jour une ligne d'inventaire
     */
    public boolean updateLigneInventaire(LigneInventaire ligne) {
        try {
            inventaireService.updateLigneInventaire(ligne);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Afficher le résumé d'un inventaire
     */
    public void afficherResume(Long idInventaire) {
        Inventaire inventaire = inventaireService.getInventaire(idInventaire).orElse(null);
        
        if (inventaire != null) {
            long excedents = inventaireService.countExcedents(idInventaire);
            long manquants = inventaireService.countManquants(idInventaire);
            
            String message = String.format(
                "Inventaire du %s\n\n" +
                "Nombre de produits : %d\n" +
                "Produits conformes : %d\n" +
                "Excédents : %d\n" +
                "Manquants : %d\n" +
                "Taux de précision : %.2f%%\n\n" +
                "Statut : %s",
                inventaire.getDateInventaire().format(dateFormatter),
                inventaire.getTotalProduits(),
                inventaire.getTotalProduits() - inventaire.getTotalEcarts(),
                excedents,
                manquants,
                inventaire.getTauxPrecision(),
                inventaire.getStatut()
            );
            
            JOptionPane.showMessageDialog(null, 
                message, 
                "Résumé de l'inventaire", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}

