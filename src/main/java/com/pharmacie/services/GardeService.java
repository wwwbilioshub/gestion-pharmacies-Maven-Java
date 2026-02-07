package com.pharmacie.services;

import com.pharmacie.dao.interfaces.GardeDAO;
import com.pharmacie.dao.interfaces.EquipeGardeDAO;
import com.pharmacie.dao.impl.GardeDAOImpl;
import com.pharmacie.dao.impl.EquipeGardeDAOImpl;
import com.pharmacie.model.Garde;
import com.pharmacie.model.EquipeGarde;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class GardeService {
    
    private final GardeDAO gardeDAO;
    private final EquipeGardeDAO equipeGardeDAO;
    
    public GardeService() {
        this.gardeDAO = new GardeDAOImpl();
        this.equipeGardeDAO = new EquipeGardeDAOImpl();
    }
    
    /**
     * Planifier une nouvelle garde
     */
    public Garde planifierGarde(Garde garde) {
        // Validation
        if (garde.getDateDebut() == null) {
            throw new IllegalArgumentException("La date de début est obligatoire");
        }
        if (garde.getDateFin() == null) {
            throw new IllegalArgumentException("La date de fin est obligatoire");
        }
        if (garde.getDateDebut().isAfter(garde.getDateFin())) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin");
        }
        if (garde.getIdPharmacie() == null) {
            throw new IllegalArgumentException("La pharmacie est obligatoire");
        }
        
        // Définir les heures par défaut si non définies
        if (garde.getHeureDebut() == null) {
            garde.setHeureDebut(LocalTime.of(21, 0));
        }
        if (garde.getHeureFin() == null) {
            garde.setHeureFin(LocalTime.of(7, 0));
        }
        
        return gardeDAO.create(garde);
    }
    
    /**
     * Modifier une garde
     */
    public Garde modifierGarde(Garde garde) {
        if (garde.getIdGarde() == null) {
            throw new IllegalArgumentException("L'ID de la garde est obligatoire pour la modification");
        }
        
        if (!gardeDAO.exists(garde.getIdGarde())) {
            throw new IllegalArgumentException("Garde introuvable");
        }
        
        return gardeDAO.update(garde);
    }
    
    /**
     * Annuler une garde
     */
    public boolean annulerGarde(Long id) {
        return gardeDAO.delete(id);
    }
    
    /**
     * Récupérer une garde par ID
     */
    public Optional<Garde> getGarde(Long id) {
        Optional<Garde> gardeOpt = gardeDAO.findById(id);
        if (gardeOpt.isPresent()) {
            Garde garde = gardeOpt.get();
            garde.setEquipeGarde(equipeGardeDAO.findByGarde(id));
        }
        return gardeOpt;
    }
    
    /**
     * Récupérer toutes les gardes
     */
    public List<Garde> getAllGardes() {
        return gardeDAO.findAll();
    }
    
    /**
     * Récupérer les gardes d'une pharmacie
     */
    public List<Garde> getGardesByPharmacie(Long idPharmacie) {
        return gardeDAO.findByPharmacie(idPharmacie);
    }
    
    /**
     * Récupérer les gardes par période
     */
    public List<Garde> getGardesByPeriode(LocalDate debut, LocalDate fin) {
        return gardeDAO.findByPeriode(debut, fin);
    }
    
    /**
     * Récupérer les gardes en cours
     */
    public List<Garde> getGardesEnCours() {
        return gardeDAO.findEnCours();
    }
    
    /**
     * Récupérer les gardes à venir
     */
    public List<Garde> getGardesAVenir(Long idPharmacie) {
        return gardeDAO.findAVenir(idPharmacie);
    }
    
    /**
     * Vérifier si une pharmacie est de garde
     */
    public boolean isDeGarde(Long idPharmacie, LocalDate date) {
        return gardeDAO.isDeGarde(idPharmacie, date);
    }
    
    /**
     * Démarrer une garde
     */
    public boolean demarrerGarde(Long id) {
        return gardeDAO.updateStatut(id, "EN_COURS");
    }
    
    /**
     * Terminer une garde
     */
    public boolean terminerGarde(Long id) {
        return gardeDAO.updateStatut(id, "TERMINEE");
    }
    
    /**
     * Assigner un employé à une garde
     */
    public EquipeGarde assignerEmploye(Long idGarde, Long idEmploye) {
        // Vérifier si déjà assigné
        if (equipeGardeDAO.isAssigne(idGarde, idEmploye)) {
            throw new IllegalArgumentException("Cet employé est déjà assigné à cette garde");
        }
        
        EquipeGarde equipe = new EquipeGarde(idGarde, idEmploye);
        return equipeGardeDAO.create(equipe);
    }
    
    /**
     * Retirer un employé d'une garde
     */
    public boolean retirerEmploye(Long idEquipeGarde) {
        return equipeGardeDAO.delete(idEquipeGarde);
    }
    
    /**
     * Enregistrer la présence d'un employé
     */
    public boolean enregistrerPresence(Long idEquipeGarde, boolean present) {
        return equipeGardeDAO.enregistrerPresence(idEquipeGarde, present);
    }
    
    /**
     * Récupérer l'équipe d'une garde
     */
    public List<EquipeGarde> getEquipeGarde(Long idGarde) {
        return equipeGardeDAO.findByGarde(idGarde);
    }
}
