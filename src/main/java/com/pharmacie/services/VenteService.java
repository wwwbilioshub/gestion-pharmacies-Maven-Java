package com.pharmacie.services;

import com.pharmacie.dao.interfaces.VenteDAO;
import com.pharmacie.dao.interfaces.LigneVenteDAO;
import com.pharmacie.dao.interfaces.ProduitDAO;
import com.pharmacie.dao.impl.VenteDAOImpl;
import com.pharmacie.dao.impl.LigneVenteDAOImpl;
import com.pharmacie.dao.impl.ProduitDAOImpl;
import com.pharmacie.model.Vente;
import com.pharmacie.model.LigneVente;
import com.pharmacie.model.Produit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class VenteService {
    
    private final VenteDAO venteDAO;
    private final LigneVenteDAO ligneVenteDAO;
    private final ProduitDAO produitDAO;
    
    public VenteService() {
        this.venteDAO = new VenteDAOImpl();
        this.ligneVenteDAO = new LigneVenteDAOImpl();
        this.produitDAO = new ProduitDAOImpl();
    }
    
    /**
     * Créer une nouvelle vente
     */
    public Vente creerVente(Vente vente) {
        // Validation
        if (vente.getIdEmploye() == null) {
            throw new IllegalArgumentException("L'employé est obligatoire");
        }
        if (vente.getIdPharmacie() == null) {
            throw new IllegalArgumentException("La pharmacie est obligatoire");
        }
        if (vente.getLignesVente() == null || vente.getLignesVente().isEmpty()) {
            throw new IllegalArgumentException("La vente doit contenir au moins un produit");
        }
        
        // Vérifier le stock pour chaque produit
        for (LigneVente ligne : vente.getLignesVente()) {
            Optional<Produit> produitOpt = produitDAO.findById(ligne.getIdProduit());
            if (produitOpt.isEmpty()) {
                throw new IllegalArgumentException("Produit introuvable: " + ligne.getIdProduit());
            }
            
            Produit produit = produitOpt.get();
            if (produit.getQuantiteStock() < ligne.getQuantite()) {
                throw new IllegalArgumentException("Stock insuffisant pour: " + produit.getNom());
            }
        }
        
        // Générer le numéro de ticket
        vente.setNumeroTicket(vente.genererNumeroTicket());
        
        // Calculer le montant total
        vente.calculerMontantTotal();
        
        // Valider la vente
        vente.valider();
        
        // Créer la vente
        Vente venteCreee = venteDAO.create(vente);
        
        // Créer les lignes de vente
        for (LigneVente ligne : vente.getLignesVente()) {
            ligne.setIdVente(venteCreee.getIdVente());
            ligneVenteDAO.create(ligne);
        }
        
        return venteCreee;
    }
    
    /**
     * Annuler une vente
     */
    public boolean annulerVente(Long idVente) {
        Optional<Vente> venteOpt = venteDAO.findById(idVente);
        if (venteOpt.isEmpty()) {
            throw new IllegalArgumentException("Vente introuvable");
        }
        
        Vente vente = venteOpt.get();
        if ("ANNULEE".equals(vente.getStatut())) {
            throw new IllegalArgumentException("Cette vente est déjà annulée");
        }
        
        // Restaurer le stock
        List<LigneVente> lignes = ligneVenteDAO.findByVente(idVente);
        for (LigneVente ligne : lignes) {
            Optional<Produit> produitOpt = produitDAO.findById(ligne.getIdProduit());
            if (produitOpt.isPresent()) {
                Produit produit = produitOpt.get();
                int nouvelleQuantite = produit.getQuantiteStock() + ligne.getQuantite();
                produitDAO.updateStock(produit.getIdProduit(), nouvelleQuantite);
            }
        }
        
        return venteDAO.delete(idVente);
    }
    
    /**
     * Récupérer une vente par ID
     */
    public Optional<Vente> getVente(Long id) {
        Optional<Vente> venteOpt = venteDAO.findById(id);
        if (venteOpt.isPresent()) {
            Vente vente = venteOpt.get();
            vente.setLignesVente(ligneVenteDAO.findByVente(id));
        }
        return venteOpt;
    }
    
    /**
     * Récupérer toutes les ventes
     */
    public List<Vente> getAllVentes() {
        return venteDAO.findAll();
    }
    
    /**
     * Récupérer les ventes d'une pharmacie
     */
    public List<Vente> getVentesByPharmacie(Long idPharmacie) {
        return venteDAO.findByPharmacie(idPharmacie);
    }
    
    /**
     * Récupérer les ventes d'un employé
     */
    public List<Vente> getVentesByEmploye(Long idEmploye) {
        return venteDAO.findByEmploye(idEmploye);
    }
    
    /**
     * Récupérer les ventes par période
     */
    public List<Vente> getVentesByPeriode(Long idPharmacie, LocalDateTime debut, LocalDateTime fin) {
        return venteDAO.findByPeriode(idPharmacie, debut, fin);
    }
    
    /**
     * Récupérer les ventes du jour
     */
    public List<Vente> getVentesDuJour(Long idPharmacie) {
        return venteDAO.findVentesDuJour(idPharmacie);
    }
    
    /**
     * Trouver une vente par numéro de ticket
     */
    public Vente getVenteByNumeroTicket(String numeroTicket) {
        return venteDAO.findByNumeroTicket(numeroTicket);
    }
    
    /**
     * Calculer le chiffre d'affaires
     */
    public double getChiffreAffaires(Long idPharmacie, LocalDateTime debut, LocalDateTime fin) {
        return venteDAO.getChiffreAffaires(idPharmacie, debut, fin);
    }
    
    /**
     * Calculer le chiffre d'affaires du jour
     */
    public double getChiffreAffairesDuJour(Long idPharmacie) {
        LocalDateTime debut = LocalDate.now().atStartOfDay();
        LocalDateTime fin = LocalDate.now().atTime(23, 59, 59);
        return getChiffreAffaires(idPharmacie, debut, fin);
    }
    
    /**
     * Compter les ventes par période
     */
    public long countVentes(Long idPharmacie, LocalDateTime debut, LocalDateTime fin) {
        return venteDAO.countByPeriode(idPharmacie, debut, fin);
    }
    
    /**
     * Obtenir les statistiques par employé
     */
    public List<Object[]> getStatistiquesParEmploye(Long idPharmacie, LocalDate debut, LocalDate fin) {
        return venteDAO.getStatistiquesParEmploye(idPharmacie, debut, fin);
    }
    
    /**
     * Obtenir les produits les plus vendus
     */
    public List<Object[]> getProduitsLesPlusVendus(Long idPharmacie, int limite) {
        return ligneVenteDAO.getProduitsLesPlusVendus(idPharmacie, limite);
    }
}
