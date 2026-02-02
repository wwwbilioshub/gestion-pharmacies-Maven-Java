package com.pharmacie.dao.interfaces;
import com.pharmacie.model.Vente;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VenteDAO extends GenericDAO<Vente, Long> {
	// Méthodes spécifiques à l'entité Vente peuvent être ajoutées ici
	/**
     * Trouver les ventes d'une pharmacie
     */
    List<Vente> findByPharmacie(Long idPharmacie);
    
    /**
     * Trouver les ventes par employé
     */
    List<Vente> findByEmploye(Long idEmploye);
    
    /**
     * Trouver les ventes par période
     */
    List<Vente> findByPeriode(Long idPharmacie, LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Trouver les ventes du jour
     */
    List<Vente> findVentesDuJour(Long idPharmacie);
    
    /**
     * Trouver une vente par numéro de ticket
     */
    Vente findByNumeroTicket(String numeroTicket);
    
    /**
     * Calculer le chiffre d'affaires par période
     */
    double getChiffreAffaires(Long idPharmacie, LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Compter les ventes par période
     */
    long countByPeriode(Long idPharmacie, LocalDateTime debut, LocalDateTime fin);
    
    /**
     * Obtenir les statistiques de vente par employé
     */
    List<Object[]> getStatistiquesParEmploye(Long idPharmacie, LocalDate debut, LocalDate fin);

}
