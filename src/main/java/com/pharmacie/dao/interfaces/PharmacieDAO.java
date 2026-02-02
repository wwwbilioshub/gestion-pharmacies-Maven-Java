package com.pharmacie.dao.interfaces;
import com.pharmacie.model.Pharmacie;
import java.util.List;

public interface PharmacieDAO extends GenericDAO<Pharmacie, Long> {
	
	 /**
     * Trouver les pharmacies actives
     */
    List<Pharmacie> findActives();
    
    /**
     * Trouver les pharmacies de garde
     */
    List<Pharmacie> findDeGarde();
    
    /**
     * Rechercher des pharmacies par nom
     */
    List<Pharmacie> findByNom(String nom);
    
    /**
     * Activer/Désactiver une pharmacie
     */
    boolean toggleActive(Long id);
    
    /**
     * Activer/Désactiver le statut de garde
     */
    boolean toggleGarde(Long id);

}
