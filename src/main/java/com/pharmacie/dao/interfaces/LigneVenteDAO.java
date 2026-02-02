package com.pharmacie.dao.interfaces;
import com.pharmacie.model.LigneVente;
import java.util.List;

public interface LigneVenteDAO extends GenericDAO<LigneVente, Long> {
	/**
     * Trouver les lignes d'une vente
     */
    List<LigneVente> findByVente(Long idVente);
    
    /**
     * Supprimer toutes les lignes d'une vente
     */
    boolean deleteByVente(Long idVente);
    
    /**
     * Créer plusieurs lignes en une fois
     */
    boolean createBatch(List<LigneVente> lignes);
    
    /**
     * Obtenir les produits les plus vendus
     */
    List<Object[]> getProduitsLesPlusVendus(Long idPharmacie, int limite);

}
