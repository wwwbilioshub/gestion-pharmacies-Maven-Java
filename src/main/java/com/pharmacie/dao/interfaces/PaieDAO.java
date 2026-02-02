package com.pharmacie.dao.interfaces;
import com.pharmacie.model.Paie;
import java.util.List;
import java.util.Optional;

public interface PaieDAO extends GenericDAO<com.pharmacie.model.Paie, Long> {
	// Méthodes spécifiques à l'entité Paie peuvent être ajoutées ici
	
    /**
     * Trouver les paies d'un employé
     */
    List<Paie> findByEmploye(Long idEmploye);
    
    /**
     * Trouver une paie par employé et période
     */
    Optional<Paie> findByEmployeAndPeriode(Long idEmploye, int mois, int annee);
    
    /**
     * Trouver les paies par période
     */
    List<Paie> findByPeriode(int mois, int annee);
    
    /**
     * Trouver les paies non payées
     */
    List<Paie> findNonPayees();
    
    /**
     * Marquer une paie comme payée
     */
    boolean marquerPayee(Long id);
    
    /**
     * Calculer le total des salaires par période
     */
    double getTotalSalaires(int mois, int annee);

}
