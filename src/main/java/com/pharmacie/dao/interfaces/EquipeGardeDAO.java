package com.pharmacie.dao.interfaces;
import com.pharmacie.model.EquipeGarde;
import java.util.List;

public interface EquipeGardeDAO extends GenericDAO<EquipeGarde, Long> {
	// Méthodes spécifiques à l'entité EquipeGarde peuvent être ajoutées ici
	 /**
     * Trouver les membres d'une garde
     */
    List<EquipeGarde> findByGarde(Long idGarde);
    
    /**
     * Trouver les gardes d'un employé
     */
    List<EquipeGarde> findByEmploye(Long idEmploye);
    
    /**
     * Vérifier si un employé est assigné à une garde
     */
    boolean isAssigne(Long idGarde, Long idEmploye);
    
    /**
     * Supprimer tous les membres d'une garde
     */
    boolean deleteByGarde(Long idGarde);
    
    /**
     * Enregistrer la présence
     */
    boolean enregistrerPresence(Long id, boolean present);

}
