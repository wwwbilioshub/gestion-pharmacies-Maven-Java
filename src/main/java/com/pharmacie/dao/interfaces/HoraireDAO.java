package com.pharmacie.dao.interfaces;
import com.pharmacie.model.Horaire;
import com.pharmacie.enums.TypeHoraire;
import java.time.LocalDate;
import java.util.List;

public interface HoraireDAO extends GenericDAO<Horaire, Long> {
	// Méthodes spécifiques à l'entité Horaire peuvent être ajoutées ici
	
	/**
     * Trouver les horaires d'un employé
     */
    List<Horaire> findByEmploye(Long idEmploye);
    
    /**
     * Trouver les horaires par période
     */
    List<Horaire> findByPeriode(Long idEmploye, LocalDate debut, LocalDate fin);
    
    /**
     * Trouver l'horaire d'un employé à une date donnée
     */
    Horaire findByEmployeAndDate(Long idEmploye, LocalDate date, TypeHoraire type);
    
    /**
     * Calculer le total d'heures travaillées sur une période
     */
    double getTotalHeuresTravaillees(Long idEmploye, LocalDate debut, LocalDate fin);
    
    /**
     * Calculer le total d'heures supplémentaires
     */
    double getTotalHeuresSupplementaires(Long idEmploye, LocalDate debut, LocalDate fin);
    
    /**
     * Compter les absences sur une période
     */
    long countAbsences(Long idEmploye, LocalDate debut, LocalDate fin);

}
