package com.pharmacie.dao.interfaces;
import com.pharmacie.model.Inventaire;
import java.time.LocalDateTime;
import java.util.List;

public interface InventaireDAO extends GenericDAO<com.pharmacie.model.Inventaire, Long> {
	// Méthodes spécifiques à l'entité Inventaire peuvent être ajoutées ici
	
	/**
     * Trouver les inventaires d'une pharmacie
     */
    List<Inventaire> findByPharmacie(Long idPharmacie);
    
    /**
     * Trouver les inventaires par période
     */
    List<Inventaire> findByPeriode(Long idPharmacie, LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Trouver les inventaires par statut
     */
    List<Inventaire> findByStatut(String statut);
    
    /**
     * Trouver les inventaires en cours
     */
    List<Inventaire> findEnCours();
    
    /**
     * Changer le statut d'un inventaire
     */
    boolean updateStatut(Long id, String statut);
    
    /**
     * Vérifier si un inventaire est en cours pour une pharmacie
     */
    boolean hasInventaireEnCours(Long idPharmacie);

}
