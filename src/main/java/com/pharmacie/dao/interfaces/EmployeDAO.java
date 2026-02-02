package com.pharmacie.dao.interfaces;
import com.pharmacie.model.Employe;
import com.pharmacie.enums.PosteEmploye;
import java.util.List;

public interface EmployeDAO extends GenericDAO<Employe, Long> {
	// Méthodes spécifiques à l'entité Employe peuvent être ajoutées ici
	/**
	 * Trouver les employés par poste
	 */
	List<Employe> findByPoste(Long idPharmacie, PosteEmploye poste);
	
	/**
     * Trouver les employés d'une pharmacie
     */
    List<Employe> findByPharmacie(Long idPharmacie);
    
    /**
     * Trouver les employés actifs
     */
    List<Employe> findActifs(Long idPharmacie);
    
    /**
     * Rechercher des employés par nom ou prénom
     */
    List<Employe> searchByNom(String recherche, Long idPharmacie);
    
    /**
     * Activer/Désactiver un employé
     */
    boolean toggleActif(Long id);
    
    /**
     * Compter les employés par pharmacie
     */
    long countByPharmacie(Long idPharmacie);
}
