package com.pharmacie.services;

import com.pharmacie.dao.interfaces.EmployeDAO;
import com.pharmacie.dao.impl.EmployeDAOImpl;
import com.pharmacie.model.Employe;
import com.pharmacie.enums.PosteEmploye;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EmployeService {
    
    private final EmployeDAO employeDAO;
    
    public EmployeService() {
        this.employeDAO = new EmployeDAOImpl();
    }
    
    /**
     * Recruter un nouvel employé
     */
    public Employe recruterEmploye(Employe employe) {
        // Validation
        if (employe.getNom() == null || employe.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (employe.getPrenom() == null || employe.getPrenom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }
        if (employe.getTelephone() == null || employe.getTelephone().trim().isEmpty()) {
            throw new IllegalArgumentException("Le téléphone est obligatoire");
        }
        if (employe.getPoste() == null) {
            throw new IllegalArgumentException("Le poste est obligatoire");
        }
        if (employe.getSalaireBase() == null || employe.getSalaireBase().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le salaire doit être supérieur à zéro");
        }
        if (employe.getIdPharmacie() == null) {
            throw new IllegalArgumentException("La pharmacie est obligatoire");
        }
        
        // Définir la date d'embauche si non définie
        if (employe.getDateEmbauche() == null) {
            employe.setDateEmbauche(LocalDate.now());
        }
        
        return employeDAO.create(employe);
    }
    
    /**
     * Modifier un employé
     */
    public Employe modifierEmploye(Employe employe) {
        if (employe.getIdEmploye() == null) {
            throw new IllegalArgumentException("L'ID de l'employé est obligatoire pour la modification");
        }
        
        if (!employeDAO.exists(employe.getIdEmploye())) {
            throw new IllegalArgumentException("Employé introuvable");
        }
        
        return employeDAO.update(employe);
    }
    
    /**
     * Désactiver un employé
     */
    public boolean desactiverEmploye(Long id) {
        return employeDAO.delete(id);
    }
    
    /**
     * Récupérer un employé par ID
     */
    public Optional<Employe> getEmploye(Long id) {
        return employeDAO.findById(id);
    }
    
    /**
     * Récupérer tous les employés
     */
    public List<Employe> getAllEmployes() {
        return employeDAO.findAll();
    }
    
    /**
     * Récupérer les employés d'une pharmacie
     */
    public List<Employe> getEmployesByPharmacie(Long idPharmacie) {
        return employeDAO.findByPharmacie(idPharmacie);
    }
    
    /**
     * Récupérer les employés actifs d'une pharmacie
     */
    public List<Employe> getEmployesActifs(Long idPharmacie) {
        return employeDAO.findActifs(idPharmacie);
    }
    
    /**
     * Récupérer les employés par poste
     */
    public List<Employe> getEmployesByPoste(Long idPharmacie, PosteEmploye poste) {
        return employeDAO.findByPoste(idPharmacie, poste);
    }
    
    /**
     * Rechercher des employés
     */
    public List<Employe> rechercherEmployes(String recherche, Long idPharmacie) {
        if (recherche == null || recherche.trim().isEmpty()) {
            return getEmployesByPharmacie(idPharmacie);
        }
        return employeDAO.searchByNom(recherche, idPharmacie);
    }
    
    /**
     * Activer/Désactiver un employé
     */
    public boolean toggleActif(Long id) {
        return employeDAO.toggleActif(id);
    }
    
    /**
     * Compter les employés d'une pharmacie
     */
    public long getNombreEmployes(Long idPharmacie) {
        return employeDAO.countByPharmacie(idPharmacie);
    }
    
    /**
     * Calculer l'ancienneté d'un employé
     */
    public int getAnciennete(Long idEmploye) {
        Optional<Employe> employeOpt = employeDAO.findById(idEmploye);
        if (employeOpt.isPresent()) {
            return employeOpt.get().getAnciennete();
        }
        return 0;
    }
    
    /**
     * Vérifier si un employé peut effectuer des ventes
     */
    public boolean peutVendre(Long idEmploye) {
        Optional<Employe> employeOpt = employeDAO.findById(idEmploye);
        return employeOpt.isPresent() && employeOpt.get().getPoste().peutVendre();
    }
    
    /**
     * Vérifier si un employé peut gérer le stock
     */
    public boolean peutGererStock(Long idEmploye) {
        Optional<Employe> employeOpt = employeDAO.findById(idEmploye);
        return employeOpt.isPresent() && employeOpt.get().getPoste().peutGererStock();
    }
}
