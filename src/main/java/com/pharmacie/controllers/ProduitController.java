package com.pharmacie.controllers;

import com.pharmacie.model.Produit;
import com.pharmacie.services.ProduitService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProduitController {
    
    private final ProduitService produitService;
    
    public ProduitController() {
        this.produitService = new ProduitService();
    }
    
    /**
     * Créer un nouveau produit
     */
    public boolean creerProduit(Produit produit) {
        try {
            produitService.creerProduit(produit);
            JOptionPane.showMessageDialog(null, 
                "Produit créé avec succès", 
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
     * Modifier un produit
     */
    public boolean modifierProduit(Produit produit) {
        try {
            produitService.modifierProduit(produit);
            JOptionPane.showMessageDialog(null, 
                "Produit modifié avec succès", 
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
     * Supprimer un produit
     */
    public boolean supprimerProduit(Long id) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Voulez-vous vraiment supprimer ce produit ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = produitService.supprimerProduit(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, 
                        "Produit supprimé avec succès", 
                        "Succès", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                return success;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Erreur lors de la suppression : " + e.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }
    
    /**
     * Charger les produits d'une pharmacie
     */
    public void chargerProduits(Long idPharmacie, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Produit> produits = produitService.getProduitsByPharmacie(idPharmacie);
        
        for (Produit produit : produits) {
            Object[] row = {
                produit.getIdProduit(),
                produit.getNom(),
                produit.getCategorie(),
                produit.getPrixUnitaire(),
                produit.getQuantiteStock(),
                produit.getSeuilMinimal(),
                produit.getDateExpiration(),
                produit.estEnRuptureStock() ? "⚠ Rupture" : "OK"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Rechercher des produits
     */
    public void rechercherProduits(String nom, Long idPharmacie, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Produit> produits = produitService.rechercherProduits(nom, idPharmacie);
        
        for (Produit produit : produits) {
            Object[] row = {
                produit.getIdProduit(),
                produit.getNom(),
                produit.getCategorie(),
                produit.getPrixUnitaire(),
                produit.getQuantiteStock()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Charger les produits en rupture
     */
    public void chargerProduitsEnRupture(Long idPharmacie, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Produit> produits = produitService.getProduitsEnRupture(idPharmacie);
        
        for (Produit produit : produits) {
            Object[] row = {
                produit.getIdProduit(),
                produit.getNom(),
                produit.getQuantiteStock(),
                produit.getSeuilMinimal(),
                "URGENT"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Charger les produits périmés
     */
    public void chargerProduitsPerimes(Long idPharmacie, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Produit> produits = produitService.getProduitsPerimes(idPharmacie, 30);
        
        for (Produit produit : produits) {
            Object[] row = {
                produit.getIdProduit(),
                produit.getNom(),
                produit.getDateExpiration(),
                produit.getQuantiteStock(),
                produit.estPerime() ? "PÉRIMÉ" : "À surveiller"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Mettre à jour le stock
     */
    public boolean updateStock(Long idProduit, int nouvelleQuantite) {
        try {
            boolean success = produitService.updateStock(idProduit, nouvelleQuantite);
            if (success) {
                JOptionPane.showMessageDialog(null, 
                    "Stock mis à jour avec succès", 
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
     * Obtenir un produit par ID
     */
    public Produit getProduit(Long id) {
        return produitService.getProduit(id).orElse(null);
    }
    
    /**
     * Obtenir toutes les catégories
     */
    public List<String> getAllCategories() {
        return produitService.getAllCategories();
    }
}
