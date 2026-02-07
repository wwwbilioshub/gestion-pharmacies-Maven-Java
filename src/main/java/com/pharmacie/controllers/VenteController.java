package com.pharmacie.controllers;

import com.pharmacie.model.Vente;
import com.pharmacie.model.LigneVente;
import com.pharmacie.services.VenteService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class VenteController {
	private final VenteService venteService;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    public VenteController() {
        this.venteService = new VenteService();
    }
    
    /**
     * Créer une nouvelle vente
     */
    public boolean creerVente(Vente vente) {
        try {
            venteService.creerVente(vente);
            JOptionPane.showMessageDialog(null, 
                "Vente enregistrée avec succès\nNuméro de ticket : " + vente.getNumeroTicket(), 
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
                "Erreur lors de l'enregistrement : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Annuler une vente
     */
    public boolean annulerVente(Long idVente) {
        int confirm = JOptionPane.showConfirmDialog(null, 
            "Voulez-vous vraiment annuler cette vente ?\nLe stock sera restauré.", 
            "Confirmation", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean success = venteService.annulerVente(idVente);
                if (success) {
                    JOptionPane.showMessageDialog(null, 
                        "Vente annulée avec succès", 
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
     * Charger les ventes d'une pharmacie
     */
    public void chargerVentes(Long idPharmacie, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Vente> ventes = venteService.getVentesByPharmacie(idPharmacie);
        
        for (Vente vente : ventes) {
            Object[] row = {
                vente.getIdVente(),
                vente.getNumeroTicket(),
                vente.getDateVente().format(dateFormatter),
                vente.getMontantTotal() + " FCFA",
                vente.getModePaiement(),
                vente.getStatut()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Charger les ventes du jour
     */
    public void chargerVentesDuJour(Long idPharmacie, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        List<Vente> ventes = venteService.getVentesDuJour(idPharmacie);
        
        for (Vente vente : ventes) {
            Object[] row = {
                vente.getIdVente(),
                vente.getNumeroTicket(),
                vente.getDateVente().format(dateFormatter),
                vente.getMontantTotal() + " FCFA",
                vente.getModePaiement()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Charger les ventes par période
     */
    public void chargerVentesPeriode(Long idPharmacie, LocalDate dateDebut, LocalDate dateFin, DefaultTableModel tableModel) {
        tableModel.setRowCount(0);
        LocalDateTime debut = dateDebut.atStartOfDay();
        LocalDateTime fin = dateFin.atTime(23, 59, 59);
        
        List<Vente> ventes = venteService.getVentesByPeriode(idPharmacie, debut, fin);
        
        for (Vente vente : ventes) {
            Object[] row = {
                vente.getIdVente(),
                vente.getNumeroTicket(),
                vente.getDateVente().format(dateFormatter),
                vente.getMontantTotal() + " FCFA",
                vente.getStatut()
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Obtenir une vente par ID
     */
    public Vente getVente(Long id) {
        return venteService.getVente(id).orElse(null);
    }
    
    /**
     * Calculer le chiffre d'affaires du jour
     */
    public double getChiffreAffairesDuJour(Long idPharmacie) {
        return venteService.getChiffreAffairesDuJour(idPharmacie);
    }
    
    /**
     * Afficher le chiffre d'affaires d'une période
     */
    public void afficherChiffreAffaires(Long idPharmacie, LocalDate dateDebut, LocalDate dateFin) {
        LocalDateTime debut = dateDebut.atStartOfDay();
        LocalDateTime fin = dateFin.atTime(23, 59, 59);
        
        double ca = venteService.getChiffreAffaires(idPharmacie, debut, fin);
        long nbVentes = venteService.countVentes(idPharmacie, debut, fin);
        double panierMoyen = nbVentes > 0 ? ca / nbVentes : 0;
        
        String message = String.format(
            "Période : %s au %s\n\n" +
            "Nombre de ventes : %d\n" +
            "Chiffre d'affaires : %.2f FCFA\n" +
            "Panier moyen : %.2f FCFA",
            dateDebut.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            dateFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            nbVentes,
            ca,
            panierMoyen
        );
        
        JOptionPane.showMessageDialog(null, 
            message, 
            "Statistiques de ventes", 
            JOptionPane.INFORMATION_MESSAGE);
    }

}
