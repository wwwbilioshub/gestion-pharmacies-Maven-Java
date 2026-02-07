package com.pharmacie.controllers;

import com.pharmacie.model.Employe;
import com.pharmacie.services.EmployeService;
import com.pharmacie.enums.PosteEmploye;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class EmployeController {
    
    private final EmployeService employeService;
    
    public EmployeController() {
        this.employeService = new EmployeService();
    }
    
    /**
     * Recruter un nouvel employé
     */
    public boolean recruterEmploye(Employe employe) {
        try {
            employeService.recruterEmploye(employe);
            JOptionPane.showMessageDialog(null, 
                "Employé recruté avec succès", 
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
                "Erreur lors du recrutement : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Modifier un employé
     */
    public boolean modifierEmploye(Employe employe) {
        try {
            employeService.modifierEmploye(employe);
            JOptionPane.showMessageDialog(null, 
                "Employé modifié avec succès", 
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
     * Désactiver un employé
     */
    public boolean desactiverEmploye(Long id) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Voulez-vous vraiment désactiver cet employé ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = employeService.desactiverEmploye(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, 
                        "Employé désactivé avec succès", 
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
     * Charger les employés d'une pharmacie
     */
    public void chargerEmployes(Long idPharmacie, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Employe> employes = employeService.getEmployesByPharmacie(idPharmacie);
        
        for (Employe employe : employes) {
            Object[] row = {
                employe.getIdEmploye(),
                employe.getNomComplet(),
                employe.getPoste().getLibelle(),
                employe.getTelephone(),
                employe.getEmail(),
                employe.getSalaireBase(),
                employe.isActif() ? "Actif" : "Inactif"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Rechercher des employés
     */
    public void rechercherEmployes(String recherche, Long idPharmacie, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Employe> employes = employeService.rechercherEmployes(recherche, idPharmacie);
        
        for (Employe employe : employes) {
            Object[] row = {
                employe.getIdEmploye(),
                employe.getNomComplet(),
                employe.getPoste().getLibelle(),
                employe.getTelephone()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Obtenir un employé par ID
     */
    public Employe getEmploye(Long id) {
        return employeService.getEmploye(id).orElse(null);
    }
    
    /**
     * Obtenir les employés actifs pour ComboBox
     */
    public List<Employe> getEmployesActifs(Long idPharmacie) {
        return employeService.getEmployesActifs(idPharmacie);
    }
    
    /**
     * Obtenir les employés par poste
     */
    public List<Employe> getEmployesByPoste(Long idPharmacie, PosteEmploye poste) {
        return employeService.getEmployesByPoste(idPharmacie, poste);
    }
}