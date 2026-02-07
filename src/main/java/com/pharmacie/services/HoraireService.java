package com.pharmacie.services;

import com.pharmacie.dao.interfaces.HoraireDAO;
import com.pharmacie.dao.impl.HoraireDAOImpl;
import com.pharmacie.model.Horaire;
import com.pharmacie.enums.TypeHoraire;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class HoraireService {
    
    private final HoraireDAO horaireDAO;
    
    public HoraireService() {
        this.horaireDAO = new HoraireDAOImpl();
    }
    
    /**
     * Créer un nouvel horaire
     */
    public Horaire creerHoraire(Horaire horaire) {
        // Validation
        if (horaire.getIdEmploye() == null) {
            throw new IllegalArgumentException("L'employé est obligatoire");
        }
        if (horaire.getDate() == null) {
            throw new IllegalArgumentException("La date est obligatoire");
        }
        if (horaire.getTypeHoraire() == null) {
            throw new IllegalArgumentException("Le type d'horaire est obligatoire");
        }
        
        // Définir les heures par défaut selon le type
        if (horaire.getHeureDebut() == null || horaire.getHeureFin() == null) {
            horaire.setHeureDebut(horaire.getTypeHoraire().getHeureDebut());
            horaire.setHeureFin(horaire.getTypeHoraire().getHeureFin());
        }
        
        // Calculer les heures travaillées
        horaire.calculerHeures();
        
        return horaireDAO.create(horaire);
    }
    
    /**
     * Modifier un horaire
     */
    public Horaire modifierHoraire(Horaire horaire) {
        if (horaire.getIdHoraire() == null) {
            throw new IllegalArgumentException("L'ID de l'horaire est obligatoire pour la modification");
        }
        
        if (!horaireDAO.exists(horaire.getIdHoraire())) {
            throw new IllegalArgumentException("Horaire introuvable");
        }
        
        // Recalculer les heures
        horaire.calculerHeures();
        
        return horaireDAO.update(horaire);
    }
    
    /**
     * Supprimer un horaire
     */
    public boolean supprimerHoraire(Long id) {
        return horaireDAO.delete(id);
    }
    
    /**
     * Récupérer un horaire par ID
     */
    public Optional<Horaire> getHoraire(Long id) {
        return horaireDAO.findById(id);
    }
    
    /**
     * Récupérer tous les horaires
     */
    public List<Horaire> getAllHoraires() {
        return horaireDAO.findAll();
    }
    
    /**
     * Récupérer les horaires d'un employé
     */
    public List<Horaire> getHorairesByEmploye(Long idEmploye) {
        return horaireDAO.findByEmploye(idEmploye);
    }
    
    /**
     * Récupérer les horaires par période
     */
    public List<Horaire> getHorairesByPeriode(Long idEmploye, LocalDate debut, LocalDate fin) {
        return horaireDAO.findByPeriode(idEmploye, debut, fin);
    }
    
    /**
     * Récupérer l'horaire d'un employé à une date donnée
     */
    public Horaire getHoraireByDate(Long idEmploye, LocalDate date, TypeHoraire type) {
        return horaireDAO.findByEmployeAndDate(idEmploye, date, type);
    }
    
    /**
     * Calculer le total d'heures travaillées
     */
    public double getTotalHeuresTravaillees(Long idEmploye, LocalDate debut, LocalDate fin) {
        return horaireDAO.getTotalHeuresTravaillees(idEmploye, debut, fin);
    }
    
    /**
     * Calculer le total d'heures supplémentaires
     */
    public double getTotalHeuresSupplementaires(Long idEmploye, LocalDate debut, LocalDate fin) {
        return horaireDAO.getTotalHeuresSupplementaires(idEmploye, debut, fin);
    }
    
    /**
     * Compter les absences
     */
    public long countAbsences(Long idEmploye, LocalDate debut, LocalDate fin) {
        return horaireDAO.countAbsences(idEmploye, debut, fin);
    }
    
    /**
     * Marquer un employé absent
     */
    public boolean marquerAbsent(Long idHoraire) {
        Optional<Horaire> horaireOpt = horaireDAO.findById(idHoraire);
        if (horaireOpt.isPresent()) {
            Horaire horaire = horaireOpt.get();
            horaire.marquerAbsent();
            horaireDAO.update(horaire);
            return true;
        }
        return false;
    }
    
    /**
     * Marquer un employé présent
     */
    public boolean marquerPresent(Long idHoraire) {
        Optional<Horaire> horaireOpt = horaireDAO.findById(idHoraire);
        if (horaireOpt.isPresent()) {
            Horaire horaire = horaireOpt.get();
            horaire.marquerPresent();
            horaireDAO.update(horaire);
            return true;
        }
        return false;
    }
}
