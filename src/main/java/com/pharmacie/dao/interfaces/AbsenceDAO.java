package com.pharmacie.dao.interfaces;
import com.pharmacie.model.Absence;
import java.time.LocalDate;
import java.util.List;
public interface AbsenceDAO extends GenericDAO<Absence, Long> {
	// Méthodes spécifiques à l'entité Absence peuvent être ajoutées ici
	/**
     * Trouver les absences d'un employé
     */
    List<Absence> findByEmploye(Long idEmploye);
    
    /**
     * Trouver les absences par période
     */
    List<Absence> findByPeriode(LocalDate debut, LocalDate fin);
    
    /**
     * Trouver les absences en attente d'approbation
     */
    List<Absence> findEnAttenteApprobation();
    
    /**
     * Trouver les absences non justifiées
     */
    List<Absence> findNonJustifiees();
    
    /**
     * Approuver/Rejeter une absence
     */
    boolean updateApprobation(Long id, boolean approuvee);
    
    /**
     * Compter les jours d'absence d'un employé sur une période
     */
    long countJoursAbsence(Long idEmploye, LocalDate debut, LocalDate fin);
	

}
