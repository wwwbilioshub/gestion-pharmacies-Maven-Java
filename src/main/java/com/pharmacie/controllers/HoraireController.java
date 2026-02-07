package com.pharmacie.controllers;


import com.pharmacie.model.Horaire;
import com.pharmacie.services.HoraireService;
import com.pharmacie.enums.TypeHoraire;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HoraireController {
    
    private final HoraireService horaireService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    public HoraireController() {
        this.horaireService = new HoraireService();
    }
    
    /**
     * Créer un nouvel horaire
     */
    public boolean creerHoraire(Horaire horaire) {
        try {
            horaireService.creerHoraire(horaire);
            JOptionPane.showMessageDialog(null, 
                "Horaire créé avec succès", 
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
     * Modifier un horaire
     */
    public boolean modifierHoraire(Horaire horaire) {
        try {
            horaireService.modifierHoraire(horaire);
            JOptionPane.showMessageDialog(null, 
                "Horaire modifié avec succès", 
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
     * Charger les horaires d'un employé
     */
    public void chargerHoraires(Long idEmploye, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Horaire> horaires = horaireService.getHorairesByEmploye(idEmploye);
        
        for (Horaire horaire : horaires) {
            Object[] row = {
                horaire.getIdHoraire(),
                horaire.getDate().format(dateFormatter),
                horaire.getTypeHoraire().getLibelle(),
                horaire.getHeureDebut() + " - " + horaire.getHeureFin(),
                String.format("%.2f h", horaire.getHeuresTravaillees()),
                String.format("%.2f h", horaire.getHeuresSupplementaires()),
                horaire.isPresent() ? "Présent" : "Absent"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Charger les horaires par période
     */
    public void chargerHorairesPeriode(Long idEmploye, LocalDate debut, LocalDate fin, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Horaire> horaires = horaireService.getHorairesByPeriode(idEmploye, debut, fin);
        
        for (Horaire horaire : horaires) {
            Object[] row = {
                horaire.getDate().format(dateFormatter),
                horaire.getTypeHoraire().getLibelle(),
                String.format("%.2f h", horaire.getHeuresTravaillees()),
                horaire.isPresent() ? "✓" : "✗"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Afficher les statistiques d'un employé
     */
    public void afficherStatistiques(Long idEmploye, LocalDate debut, LocalDate fin) {
        double totalHeures = horaireService.getTotalHeuresTravaillees(idEmploye, debut, fin);
        double totalHS = horaireService.getTotalHeuresSupplementaires(idEmploye, debut, fin);
        long absences = horaireService.countAbsences(idEmploye, debut, fin);
        
        String message = String.format(
            "Période : %s au %s\n\n" +
            "Total heures travaillées : %.2f h\n" +
            "Heures supplémentaires : %.2f h\n" +
            "Nombre d'absences : %d",
            debut.format(dateFormatter),
            fin.format(dateFormatter),
            totalHeures,
            totalHS,
            absences
        );
        
        JOptionPane.showMessageDialog(null, 
            message, 
            "Statistiques de l'employé", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Marquer un employé absent
     */
    public boolean marquerAbsent(Long idHoraire) {
        try {
            boolean success = horaireService.marquerAbsent(idHoraire);
            if (success) {
                JOptionPane.showMessageDialog(null, 
                    "Absence enregistrée", 
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
     * Marquer un employé présent
     */
    public boolean marquerPresent(Long idHoraire) {
        try {
            boolean success = horaireService.marquerPresent(idHoraire);
            if (success) {
                JOptionPane.showMessageDialog(null, 
                    "Présence enregistrée", 
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