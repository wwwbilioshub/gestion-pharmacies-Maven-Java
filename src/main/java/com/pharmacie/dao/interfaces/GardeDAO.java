package com.pharmacie.dao.interfaces;
import com.pharmacie.model.Garde;
import java.time.LocalDate;
import java.util.List;

public interface GardeDAO extends GenericDAO<Garde, Long> {
	// Méthodes spécifiques à l'entité Garde peuvent être ajoutées ici
	
	/**
     * Trouver les gardes d'une pharmacie
     */
    List<Garde> findByPharmacie(Long idPharmacie);
    
    /**
     * Trouver les gardes par période
     */
    List<Garde> findByPeriode(LocalDate debut, LocalDate fin);
    
    /**
     * Trouver les gardes en cours
     */
    List<Garde> findEnCours();
    
    /**
     * Trouver les gardes à venir
     */
    List<Garde> findAVenir(Long idPharmacie);
    
    /**
     * Vérifier si une pharmacie est de garde à une date donnée
     */
    boolean isDeGarde(Long idPharmacie, LocalDate date);
    
    /**
     * Changer le statut d'une garde
     */
    boolean updateStatut(Long id, String statut);

}
