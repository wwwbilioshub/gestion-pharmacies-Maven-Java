package com.pharmacie.dao.interfaces;
import com.pharmacie.model.LigneInventaire;
import java.util.List;

public interface LigneInventaireDAO extends GenericDAO<LigneInventaire, Long> {
	// Méthodes spécifiques à l'entité LigneInventaire peuvent être ajoutées ici
	
	/**
     * Trouver les lignes d'un inventaire
     */
    List<LigneInventaire> findByInventaire(Long idInventaire);
    
    /**
     * Trouver les lignes avec écart
     */
    List<LigneInventaire> findAvecEcart(Long idInventaire);
    
    /**
     * Supprimer toutes les lignes d'un inventaire
     */
    boolean deleteByInventaire(Long idInventaire);
    
    /**
     * Créer plusieurs lignes en une fois
     */
    boolean createBatch(List<LigneInventaire> lignes);
    
    /**
     * Compter les écarts par type
     */
    long countExcedents(Long idInventaire);
    long countManquants(Long idInventaire);

}
