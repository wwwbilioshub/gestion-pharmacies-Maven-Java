package com.pharmacie.services;

import com.pharmacie.dao.interfaces.PaieDAO;
import com.pharmacie.dao.interfaces.EmployeDAO;
import com.pharmacie.dao.interfaces.HoraireDAO;
import com.pharmacie.dao.impl.PaieDAOImpl;
import com.pharmacie.dao.impl.EmployeDAOImpl;
import com.pharmacie.dao.impl.HoraireDAOImpl;
import com.pharmacie.model.Paie;
import com.pharmacie.model.Employe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public class PaieService {
    
    private final PaieDAO paieDAO;
    private final EmployeDAO employeDAO;
    private final HoraireDAO horaireDAO;
    
    public PaieService() {
        this.paieDAO = new PaieDAOImpl();
        this.employeDAO = new EmployeDAOImpl();
        this.horaireDAO = new HoraireDAOImpl();
    }
    
    /**
     * Générer la paie d'un employé pour un mois donné
     */
    public Paie genererPaie(Long idEmploye, int mois, int annee) {
        // Vérifier si la paie existe déjà
        Optional<Paie> existingPaie = paieDAO.findByEmployeAndPeriode(idEmploye, mois, annee);
        if (existingPaie.isPresent()) {
            throw new IllegalArgumentException("La paie pour cette période existe déjà");
        }
        
        // Récupérer l'employé
        Optional<Employe> employeOpt = employeDAO.findById(idEmploye);
        if (employeOpt.isEmpty()) {
            throw new IllegalArgumentException("Employé introuvable");
        }
        
        Employe employe = employeOpt.get();
        
        // Calculer la période
        YearMonth yearMonth = YearMonth.of(annee, mois);
        LocalDate debut = yearMonth.atDay(1);
        LocalDate fin = yearMonth.atEndOfMonth();
        
        // Récupérer les heures travaillées
        double heuresTravaillees = horaireDAO.getTotalHeuresTravaillees(idEmploye, debut, fin);
        double heuresSupplementaires = horaireDAO.getTotalHeuresSupplementaires(idEmploye, debut, fin);
        
        // Créer la paie
        Paie paie = new Paie(idEmploye, mois, annee);
        paie.setMontantBase(employe.getSalaireBase());
        
        // Calculer les heures supplémentaires (taux majoré de 150%)
        BigDecimal montantHS = employe.getTauxHoraire()
                .multiply(BigDecimal.valueOf(heuresSupplementaires))
                .multiply(BigDecimal.valueOf(1.5))
                .setScale(2, RoundingMode.HALF_UP);
        paie.setHeuresSupplementaires(montantHS);
        
        // Prime de garde (à définir selon la logique métier)
        paie.setPrimeGarde(BigDecimal.ZERO);
        
        // Pénalités (à définir selon la logique métier)
        paie.setPenalite(BigDecimal.ZERO);
        
        // Calculer le montant total
        paie.calculerMontantTotal();
        
        return paieDAO.create(paie);
    }
    
    /**
     * Modifier une paie
     */
    public Paie modifierPaie(Paie paie) {
        if (paie.getIdPaie() == null) {
            throw new IllegalArgumentException("L'ID de la paie est obligatoire pour la modification");
        }
        
        if (!paieDAO.exists(paie.getIdPaie())) {
            throw new IllegalArgumentException("Paie introuvable");
        }
        
        // Recalculer le montant total
        paie.calculerMontantTotal();
        
        return paieDAO.update(paie);
    }
    
    /**
     * Supprimer une paie
     */
    public boolean supprimerPaie(Long id) {
        return paieDAO.delete(id);
    }
    
    /**
     * Récupérer une paie par ID
     */
    public Optional<Paie> getPaie(Long id) {
        return paieDAO.findById(id);
    }
    
    /**
     * Récupérer toutes les paies
     */
    public List<Paie> getAllPaies() {
        return paieDAO.findAll();
    }
    
    /**
     * Récupérer les paies d'un employé
     */
    public List<Paie> getPaiesByEmploye(Long idEmploye) {
        return paieDAO.findByEmploye(idEmploye);
    }
    
    /**
     * Récupérer la paie d'un employé pour une période
     */
    public Optional<Paie> getPaieByPeriode(Long idEmploye, int mois, int annee) {
        return paieDAO.findByEmployeAndPeriode(idEmploye, mois, annee);
    }
    
    /**
     * Récupérer les paies par période
     */
    public List<Paie> getPaiesByPeriode(int mois, int annee) {
        return paieDAO.findByPeriode(mois, annee);
    }
    
    /**
     * Récupérer les paies non payées
     */
    public List<Paie> getPaiesNonPayees() {
        return paieDAO.findNonPayees();
    }
    
    /**
     * Marquer une paie comme payée
     */
    public boolean marquerPayee(Long id) {
        return paieDAO.marquerPayee(id);
    }
    
    /**
     * Calculer le total des salaires pour une période
     */
    public double getTotalSalaires(int mois, int annee) {
        return paieDAO.getTotalSalaires(mois, annee);
    }
    
    /**
     * Générer les paies pour tous les employés d'une pharmacie
     */
    public List<Paie> genererPaiesPourPharmacie(Long idPharmacie, int mois, int annee) {
        List<Employe> employes = employeDAO.findActifs(idPharmacie);
        List<Paie> paies = new java.util.ArrayList<>();
        
        for (Employe employe : employes) {
            try {
                Paie paie = genererPaie(employe.getIdEmploye(), mois, annee);
                paies.add(paie);
            } catch (IllegalArgumentException e) {
                // La paie existe déjà, on continue
                System.out.println("Paie déjà existante pour " + employe.getNomComplet());
            }
        }
        
        return paies;
    }
}
