package com.pharmacie.dao.interfaces;
import com.pharmacie.model.Produit;
import java.util.List;

public interface ProduitDAO extends GenericDAO<Produit, Long> {
	 
    /**
     * Trouver les produits d'une pharmacie
     */
    List<Produit> findByPharmacie(Long idPharmacie);
    
    /**
     * Trouver les produits par catégorie
     */
    List<Produit> findByCategorie(String categorie);
    
    /**
     * Trouver les produits en rupture de stock
     */
    List<Produit> findEnRuptureStock(Long idPharmacie);
    
    /**
     * Trouver les produits périmés ou proches de la péremption
     */
    List<Produit> findPerimesOuProches(Long idPharmacie, int joursAvant);
    
    /**
     * Rechercher des produits par nom
     */
    List<Produit> searchByNom(String nom, Long idPharmacie);
    
    /**
     * Trouver un produit par code-barres
     */
    Produit findByCodeBarre(String codeBarre);
    
    /**
     * Trouver un produit par référence
     */
    Produit findByReference(String reference);
    
    /**
     * Mettre à jour le stock d'un produit
     */
    boolean updateStock(Long idProduit, int nouvelleQuantite);
    
    /**
     * Obtenir toutes les catégories distinctes
     */
    List<String> findAllCategories();

}
