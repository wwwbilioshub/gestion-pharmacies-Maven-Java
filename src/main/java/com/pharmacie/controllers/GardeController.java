package com.pharmacie.controllers;

import com.pharmacie.model.Garde;
import com.pharmacie.model.EquipeGarde;
import com.pharmacie.services.GardeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GardeController {
    
    private final GardeService gardeService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public GardeController() {
        this.gardeService = new GardeService();
    }
    
    /**
     * Planifier une nouvelle garde
     */
    public boolean planifierGarde(Garde garde) {
        try {
            gardeService.planifierGarde(garde);
            JOptionPane.showMessageDialog(null, 
                "Garde planifiée avec succès", 
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
                "Erreur lors de la planification : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Modifier une garde
     */
    public boolean modifierGarde(Garde garde) {
        try {
            gardeService.modifierGarde(garde);
            JOptionPane.showMessageDialog(null, 
                "Garde modifiée avec succès", 
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
     * Annuler une garde
     */
    public boolean annulerGarde(Long id) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Voulez-vous vraiment annuler cette garde ?", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = gardeService.annulerGarde(id);
                if (success) {
                    JOptionPane.showMessageDialog(null, 
                        "Garde annulée avec succès", 
                        "Succès", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
                return success;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Erreur lors de l'annulation : " + e.getMessage(), 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }
    
    /**
     * Charger les gardes d'une pharmacie
     */
    public void chargerGardes(Long idPharmacie, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Garde> gardes = gardeService.getGardesByPharmacie(idPharmacie);
        
        for (Garde garde : gardes) {
            Object[] row = {
                garde.getIdGarde(),
                garde.getDateDebut().format(dateFormatter),
                garde.getDateFin().format(dateFormatter),
                garde.getHeureDebut() + " - " + garde.getHeureFin(),
                garde.getStatut()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Charger les gardes par période
     */
    public void chargerGardesPeriode(LocalDate debut, LocalDate fin, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Garde> gardes = gardeService.getGardesByPeriode(debut, fin);
        
        for (Garde garde : gardes) {
            Object[] row = {
                garde.getIdGarde(),
                garde.getDateDebut().format(dateFormatter),
                garde.getDateFin().format(dateFormatter),
                garde.getStatut()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Obtenir une garde par ID
     */
    public Garde getGarde(Long id) {
        return gardeService.getGarde(id).orElse(null);
    }
    
    /**
     * Assigner un employé à une garde
     */
    public boolean assignerEmploye(Long idGarde, Long idEmploye) {
        try {
            gardeService.assignerEmploye(idGarde, idEmploye);
            JOptionPane.showMessageDialog(null, 
                "Employé assigné avec succès", 
                "Succès", 
                JOptionPane.INFORMATION_MESSAGE);
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
     * Charger l'équipe de garde
     */
    public void chargerEquipeGarde(Long idGarde, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<EquipeGarde> equipe = gardeService.getEquipeGarde(idGarde);
        
        for (EquipeGarde membre : equipe) {
            Object[] row = {
                membre.getIdEquipeGarde(),
                membre.getHeureArrivee(),
                membre.getHeureDepart(),
                membre.isPresent() ? "Présent" : "Absent"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Démarrer une garde
     */
    public boolean demarrerGarde(Long id) {
        try {
            boolean success = gardeService.demarrerGarde(id);
            if (success) {
                JOptionPane.showMessageDialog(null, 
                    "Garde démarrée", 
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
     * Terminer une garde
     */
    public boolean terminerGarde(Long id) {
        try {
            boolean success = gardeService.terminerGarde(id);
            if (success) {
                JOptionPane.showMessageDialog(null, 
                    "Garde terminée", 
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
}
