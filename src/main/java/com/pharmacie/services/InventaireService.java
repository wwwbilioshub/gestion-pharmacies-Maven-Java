package com.pharmacie.services;

import com.pharmacie.dao.interfaces.InventaireDAO;
import com.pharmacie.dao.interfaces.LigneInventaireDAO;
import com.pharmacie.dao.interfaces.ProduitDAO;
import com.pharmacie.dao.impl.InventaireDAOImpl;
import com.pharmacie.dao.impl.LigneInventaireDAOImpl;
import com.pharmacie.dao.impl.ProduitDAOImpl;
import com.pharmacie.model.Inventaire;
import com.pharmacie.model.LigneInventaire;
import com.pharmacie.model.Produit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class InventaireService {
    
    private final InventaireDAO inventaireDAO;
    private final LigneInventaireDAO ligneInventaireDAO;
    private final ProduitDAO produitDAO;
    
    public InventaireService() {
        this.inventaireDAO = new InventaireDAOImpl();
        this.ligneInventaireDAO = new LigneInventaireDAOImpl();
        this.produitDAO = new ProduitDAOImpl();
    }
    
    /**
     * Démarrer un nouvel inventaire
     */
    public Inventaire demarrerInventaire(Long idPharmacie, Long idEmploye) {
        // Vérifier qu'il n'y a pas d'inventaire en cours
        if (inventaireDAO.hasInventaireEnCours(idPharmacie)) {
            throw new IllegalArgumentException("Un inventaire est déjà en cours pour cette pharmacie");
        }
        
        Inventaire inventaire = new Inventaire(idPharmacie, idEmploye);
        Inventaire inventaireCreé = inventaireDAO.create(inventaire);
        
        // Créer les lignes d'inventaire pour tous les produits
        List<Produit> produits = produitDAO.findByPharmacie(idPharmacie);
        for (Produit produit : produits) {
            LigneInventaire ligne = new LigneInventaire();
            ligne.setIdInventaire(inventaireCreé.getIdInventaire());
            ligne.setIdProduit(produit.getIdProduit());
            ligne.setQuantiteTheorique(produit.getQuantiteStock());
            ligne.setQuantiteReelle(0); // À saisir manuellement
            ligneInventaireDAO.create(ligne);
        }
        
        return inventaireCreé;
    }
    
    /**
     * Modifier un inventaire
     */
    public Inventaire modifierInventaire(Inventaire inventaire) {
        if (inventaire.getIdInventaire() == null) {
            throw new IllegalArgumentException("L'ID de l'inventaire est obligatoire pour la modification");
        }
        
        if (!inventaireDAO.exists(inventaire.getIdInventaire())) {
            throw new IllegalArgumentException("Inventaire introuvable");
        }
        
        return inventaireDAO.update(inventaire);
    }
    
    /**
     * Annuler un inventaire
     */
    public boolean annulerInventaire(Long id) {
        return inventaireDAO.delete(id);
    }
    
    /**
     * Récupérer un inventaire par ID
     */
    public Optional<Inventaire> getInventaire(Long id) {
        Optional<Inventaire> invOpt = inventaireDAO.findById(id);
        if (invOpt.isPresent()) {
            Inventaire inventaire = invOpt.get();
            inventaire.setLignesInventaire(ligneInventaireDAO.findByInventaire(id));
        }
        return invOpt;
    }
    
    /**
     * Récupérer tous les inventaires
     */
    public List<Inventaire> getAllInventaires() {
        return inventaireDAO.findAll();
    }
    
    /**
     * Récupérer les inventaires d'une pharmacie
     */
    public List<Inventaire> getInventairesByPharmacie(Long idPharmacie) {
        return inventaireDAO.findByPharmacie(idPharmacie);
    }
    
    /**
     * Récupérer les inventaires par période
     */
    public List<Inventaire> getInventairesByPeriode(Long idPharmacie, LocalDateTime debut, LocalDateTime fin) {
        return inventaireDAO.findByPeriode(idPharmacie, debut, fin);
    }
    
    /**
     * Récupérer les inventaires par statut
     */
    public List<Inventaire> getInventairesByStatut(String statut) {
        return inventaireDAO.findByStatut(statut);
    }
    
    /**
     * Récupérer les inventaires en cours
     */
    public List<Inventaire> getInventairesEnCours() {
        return inventaireDAO.findEnCours();
    }
    
    /**
     * Terminer un inventaire
     */
    public boolean terminerInventaire(Long id) {
        return inventaireDAO.updateStatut(id, "TERMINE");
    }
    
    /**
     * Valider un inventaire et mettre à jour les stocks
     */
    public boolean validerInventaire(Long id) {
        Optional<Inventaire> invOpt = inventaireDAO.findById(id);
        if (invOpt.isEmpty()) {
            throw new IllegalArgumentException("Inventaire introuvable");
        }
        
        Inventaire inventaire = invOpt.get();
        if (!"TERMINE".equals(inventaire.getStatut())) {
            throw new IllegalArgumentException("L'inventaire doit être terminé avant d'être validé");
        }
        
        // Mettre à jour les stocks
        List<LigneInventaire> lignes = ligneInventaireDAO.findByInventaire(id);
        for (LigneInventaire ligne : lignes) {
            produitDAO.updateStock(ligne.getIdProduit(), ligne.getQuantiteReelle());
        }
        
        return inventaireDAO.updateStatut(id, "VALIDE");
    }
    
    /**
     * Mettre à jour une ligne d'inventaire
     */
    public LigneInventaire updateLigneInventaire(LigneInventaire ligne) {
        if (!ligneInventaireDAO.exists(ligne.getIdLigneInventaire())) {
            throw new IllegalArgumentException("Ligne d'inventaire introuvable");
        }
        
        ligne.calculerEcart();
        return ligneInventaireDAO.update(ligne);
    }
    
    /**
     * Récupérer les lignes avec écart
     */
    public List<LigneInventaire> getLignesAvecEcart(Long idInventaire) {
        return ligneInventaireDAO.findAvecEcart(idInventaire);
    }
    
    /**
     * Compter les excédents
     */
    public long countExcedents(Long idInventaire) {
        return ligneInventaireDAO.countExcedents(idInventaire);
    }
    
    /**
     * Compter les manquants
     */
    public long countManquants(Long idInventaire) {
        return ligneInventaireDAO.countManquants(idInventaire);
    }
}
