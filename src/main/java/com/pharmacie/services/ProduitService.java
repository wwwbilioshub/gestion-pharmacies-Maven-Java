package com.pharmacie.services;

import com.pharmacie.dao.interfaces.ProduitDAO;
import com.pharmacie.dao.impl.ProduitDAOImpl;
import com.pharmacie.model.Produit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ProduitService {
    
    private final ProduitDAO produitDAO;
    
    public ProduitService() {
        this.produitDAO = new ProduitDAOImpl();
    }
    
    /**
     * Créer un nouveau produit
     */
    public Produit creerProduit(Produit produit) {
        // Validation
        if (produit.getNom() == null || produit.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du produit est obligatoire");
        }
        if (produit.getCategorie() == null || produit.getCategorie().trim().isEmpty()) {
            throw new IllegalArgumentException("La catégorie est obligatoire");
        }
        if (produit.getPrixUnitaire() == null || produit.getPrixUnitaire().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le prix doit être supérieur à zéro");
        }
        if (produit.getIdPharmacie() == null) {
            throw new IllegalArgumentException("La pharmacie est obligatoire");
        }
        
        return produitDAO.create(produit);
    }
    
    /**
     * Modifier un produit existant
     */
    public Produit modifierProduit(Produit produit) {
        if (produit.getIdProduit() == null) {
            throw new IllegalArgumentException("L'ID du produit est obligatoire pour la modification");
        }
        
        if (!produitDAO.exists(produit.getIdProduit())) {
            throw new IllegalArgumentException("Produit introuvable");
        }
        
        return produitDAO.update(produit);
    }
    
    /**
     * Supprimer un produit (suppression logique)
     */
    public boolean supprimerProduit(Long id) {
        return produitDAO.delete(id);
    }
    
    /**
     * Récupérer un produit par ID
     */
    public Optional<Produit> getProduit(Long id) {
        return produitDAO.findById(id);
    }
    
    /**
     * Récupérer tous les produits
     */
    public List<Produit> getAllProduits() {
        return produitDAO.findAll();
    }
    
    /**
     * Récupérer les produits d'une pharmacie
     */
    public List<Produit> getProduitsByPharmacie(Long idPharmacie) {
        return produitDAO.findByPharmacie(idPharmacie);
    }
    
    /**
     * Récupérer les produits par catégorie
     */
    public List<Produit> getProduitsByCategorie(String categorie) {
        return produitDAO.findByCategorie(categorie);
    }
    
    /**
     * Récupérer les produits en rupture de stock
     */
    public List<Produit> getProduitsEnRupture(Long idPharmacie) {
        return produitDAO.findEnRuptureStock(idPharmacie);
    }
    
    /**
     * Récupérer les produits périmés ou proches de la péremption
     */
    public List<Produit> getProduitsPerimes(Long idPharmacie, int joursAvant) {
        return produitDAO.findPerimesOuProches(idPharmacie, joursAvant);
    }
    
    /**
     * Rechercher des produits par nom
     */
    public List<Produit> rechercherProduits(String nom, Long idPharmacie) {
        if (nom == null || nom.trim().isEmpty()) {
            return getProduitsByPharmacie(idPharmacie);
        }
        return produitDAO.searchByNom(nom, idPharmacie);
    }
    
    /**
     * Trouver un produit par code-barres
     */
    public Produit getProduitByCodeBarre(String codeBarre) {
        return produitDAO.findByCodeBarre(codeBarre);
    }
    
    /**
     * Trouver un produit par référence
     */
    public Produit getProduitByReference(String reference) {
        return produitDAO.findByReference(reference);
    }
    
    /**
     * Mettre à jour le stock d'un produit
     */
    public boolean updateStock(Long idProduit, int nouvelleQuantite) {
        if (nouvelleQuantite < 0) {
            throw new IllegalArgumentException("La quantité ne peut pas être négative");
        }
        return produitDAO.updateStock(idProduit, nouvelleQuantite);
    }
    
    /**
     * Ajouter du stock
     */
    public boolean ajouterStock(Long idProduit, int quantite) {
        Optional<Produit> produitOpt = produitDAO.findById(idProduit);
        if (produitOpt.isPresent()) {
            Produit produit = produitOpt.get();
            int nouvelleQuantite = produit.getQuantiteStock() + quantite;
            return updateStock(idProduit, nouvelleQuantite);
        }
        return false;
    }
    
    /**
     * Retirer du stock
     */
    public boolean retirerStock(Long idProduit, int quantite) {
        Optional<Produit> produitOpt = produitDAO.findById(idProduit);
        if (produitOpt.isPresent()) {
            Produit produit = produitOpt.get();
            int nouvelleQuantite = produit.getQuantiteStock() - quantite;
            if (nouvelleQuantite < 0) {
                throw new IllegalArgumentException("Stock insuffisant");
            }
            return updateStock(idProduit, nouvelleQuantite);
        }
        return false;
    }
    
    /**
     * Obtenir toutes les catégories
     */
    public List<String> getAllCategories() {
        return produitDAO.findAllCategories();
    }
    
    /**
     * Vérifier si un produit est périmé
     */
    public boolean isProduitPerime(Long idProduit) {
        Optional<Produit> produitOpt = produitDAO.findById(idProduit);
        return produitOpt.isPresent() && produitOpt.get().estPerime();
    }
    
    /**
     * Vérifier si un produit est en rupture
     */
    public boolean isProduitEnRupture(Long idProduit) {
        Optional<Produit> produitOpt = produitDAO.findById(idProduit);
        return produitOpt.isPresent() && produitOpt.get().estEnRuptureStock();
    }
}