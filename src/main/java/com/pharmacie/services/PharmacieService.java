package com.pharmacie.services;

import com.pharmacie.dao.interfaces.PharmacieDAO;
import com.pharmacie.dao.impl.PharmacieDAOImpl;
import com.pharmacie.model.Pharmacie;

import java.util.List;
import java.util.Optional;

public class PharmacieService {
    
    private final PharmacieDAO pharmacieDAO;
    
    public PharmacieService() {
        this.pharmacieDAO = new PharmacieDAOImpl();
    }
    
    /**
     * Créer une nouvelle pharmacie
     */
    public Pharmacie creerPharmacie(Pharmacie pharmacie) {
        // Validation
        if (pharmacie.getNom() == null || pharmacie.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la pharmacie est obligatoire");
        }
        if (pharmacie.getAdresse() == null || pharmacie.getAdresse().trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse est obligatoire");
        }
        if (pharmacie.getTelephone() == null || pharmacie.getTelephone().trim().isEmpty()) {
            throw new IllegalArgumentException("Le téléphone est obligatoire");
        }
        
        return pharmacieDAO.create(pharmacie);
    }
    
    /**
     * Modifier une pharmacie existante
     */
    public Pharmacie modifierPharmacie(Pharmacie pharmacie) {
        if (pharmacie.getIdPharmacie() == null) {
            throw new IllegalArgumentException("L'ID de la pharmacie est obligatoire pour la modification");
        }
        
        if (!pharmacieDAO.exists(pharmacie.getIdPharmacie())) {
            throw new IllegalArgumentException("Pharmacie introuvable");
        }
        
        return pharmacieDAO.update(pharmacie);
    }
    
    /**
     * Désactiver une pharmacie (suppression logique)
     */
    public boolean desactiverPharmacie(Long id) {
        return pharmacieDAO.delete(id);
    }
    
    /**
     * Récupérer une pharmacie par ID
     */
    public Optional<Pharmacie> getPharmacie(Long id) {
        return pharmacieDAO.findById(id);
    }
    
    /**
     * Récupérer toutes les pharmacies
     */
    public List<Pharmacie> getAllPharmacies() {
        return pharmacieDAO.findAll();
    }
    
    /**
     * Récupérer les pharmacies actives
     */
    public List<Pharmacie> getPharmaciesActives() {
        return pharmacieDAO.findActives();
    }
    
    /**
     * Récupérer les pharmacies de garde
     */
    public List<Pharmacie> getPharmaciesDeGarde() {
        return pharmacieDAO.findDeGarde();
    }
    
    /**
     * Rechercher des pharmacies par nom
     */
    public List<Pharmacie> rechercherParNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return getAllPharmacies();
        }
        return pharmacieDAO.findByNom(nom);
    }
    
    /**
     * Activer/Désactiver le statut actif
     */
    public boolean toggleActif(Long id) {
        return pharmacieDAO.toggleActive(id);
    }
    
    /**
     * Activer/Désactiver le statut de garde
     */
    public boolean toggleGarde(Long id) {
        return pharmacieDAO.toggleGarde(id);
    }
    
    /**
     * Compter le nombre de pharmacies
     */
    public long getNombrePharmacies() {
        return pharmacieDAO.count();
    }
}