package com.pharmacie.services;
import com.pharmacie.dao.interfaces.UtilisateurDAO;
import com.pharmacie.dao.impl.UtilisateurDAOImpl;
import com.pharmacie.model.Utilisateur;
import com.pharmacie.enums.RoleUtilisateur;
import java.util.List;
import java.util.Optional;

public class AuthentificationService {
	 private final UtilisateurDAO utilisateurDAO;
	    private Utilisateur utilisateurConnecte;
	    
	    public AuthentificationService() {
	        this.utilisateurDAO = new UtilisateurDAOImpl();
	    }
	    
	    /**
	     * Authentifier un utilisateur
	     */
	    public Optional<Utilisateur> authentifier(String login, String motDePasse) {
	        if (login == null || login.trim().isEmpty()) {
	            throw new IllegalArgumentException("Le login est obligatoire");
	        }
	        if (motDePasse == null || motDePasse.trim().isEmpty()) {
	            throw new IllegalArgumentException("Le mot de passe est obligatoire");
	        }
	        
	        Optional<Utilisateur> userOpt = utilisateurDAO.authenticate(login, motDePasse);
	        
	        if (userOpt.isPresent()) {
	            this.utilisateurConnecte = userOpt.get();
	        }
	        
	        return userOpt;
	    }
	    
	    /**
	     * Déconnecter l'utilisateur
	     */
	    public void deconnecter() {
	        this.utilisateurConnecte = null;
	    }
	    
	    /**
	     * Obtenir l'utilisateur connecté
	     */
	    public Utilisateur getUtilisateurConnecte() {
	        return utilisateurConnecte;
	    }
	    
	    /**
	     * Vérifier si un utilisateur est connecté
	     */
	    public boolean isConnecte() {
	        return utilisateurConnecte != null;
	    }
	    
	    /**
	     * Créer un nouvel utilisateur
	     */
	    public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
	        // Validation
	        if (utilisateur.getLogin() == null || utilisateur.getLogin().trim().isEmpty()) {
	            throw new IllegalArgumentException("Le login est obligatoire");
	        }
	        if (utilisateur.getMotDePasse() == null || utilisateur.getMotDePasse().trim().isEmpty()) {
	            throw new IllegalArgumentException("Le mot de passe est obligatoire");
	        }
	        if (utilisateur.getRole() == null) {
	            throw new IllegalArgumentException("Le rôle est obligatoire");
	        }
	        
	        // Vérifier si le login existe déjà
	        if (utilisateurDAO.loginExists(utilisateur.getLogin())) {
	            throw new IllegalArgumentException("Ce login existe déjà");
	        }
	        
	        // Validation du mot de passe
	        if (utilisateur.getMotDePasse().length() < 6) {
	            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
	        }
	        
	        return utilisateurDAO.create(utilisateur);
	    }
	    
	    /**
	     * Modifier un utilisateur
	     */
	    public Utilisateur modifierUtilisateur(Utilisateur utilisateur) {
	        if (utilisateur.getIdUtilisateur() == null) {
	            throw new IllegalArgumentException("L'ID de l'utilisateur est obligatoire pour la modification");
	        }
	        
	        if (!utilisateurDAO.exists(utilisateur.getIdUtilisateur())) {
	            throw new IllegalArgumentException("Utilisateur introuvable");
	        }
	        
	        return utilisateurDAO.update(utilisateur);
	    }
	    
	    /**
	     * Changer le mot de passe
	     */
	    public boolean changerMotDePasse(Long idUtilisateur, String ancienMotDePasse, String nouveauMotDePasse) {
	        Optional<Utilisateur> userOpt = utilisateurDAO.findById(idUtilisateur);
	        
	        if (userOpt.isEmpty()) {
	            throw new IllegalArgumentException("Utilisateur introuvable");
	        }
	        
	        // Vérifier l'ancien mot de passe
	        Utilisateur user = userOpt.get();
	        Optional<Utilisateur> authOpt = utilisateurDAO.authenticate(user.getLogin(), ancienMotDePasse);
	        
	        if (authOpt.isEmpty()) {
	            throw new IllegalArgumentException("Ancien mot de passe incorrect");
	        }
	        
	        // Validation du nouveau mot de passe
	        if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 6) {
	            throw new IllegalArgumentException("Le nouveau mot de passe doit contenir au moins 6 caractères");
	        }
	        
	        return utilisateurDAO.updatePassword(idUtilisateur, nouveauMotDePasse);
	    }
	    
	    /**
	     * Réinitialiser le mot de passe (pour admin)
	     */
	    public boolean reinitialiserMotDePasse(Long idUtilisateur, String nouveauMotDePasse) {
	        if (!isAdmin()) {
	            throw new SecurityException("Opération réservée aux administrateurs");
	        }
	        
	        if (nouveauMotDePasse == null || nouveauMotDePasse.length() < 6) {
	            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
	        }
	        
	        return utilisateurDAO.updatePassword(idUtilisateur, nouveauMotDePasse);
	    }
	    
	    /**
	     * Récupérer tous les utilisateurs
	     */
	    public List<Utilisateur> getAllUtilisateurs() {
	        return utilisateurDAO.findAll();
	    }
	    
	    /**
	     * Récupérer les utilisateurs actifs
	     */
	    public List<Utilisateur> getUtilisateursActifs() {
	        return utilisateurDAO.findActifs();
	    }
	    
	    /**
	     * Récupérer les utilisateurs par rôle
	     */
	    public List<Utilisateur> getUtilisateursByRole(RoleUtilisateur role) {
	        return utilisateurDAO.findByRole(role);
	    }
	    
	    /**
	     * Activer/Désactiver un utilisateur
	     */
	    public boolean toggleActif(Long id) {
	        if (!isAdmin()) {
	            throw new SecurityException("Opération réservée aux administrateurs");
	        }
	        return utilisateurDAO.toggleActif(id);
	    }
	    
	    /**
	     * Vérifier si l'utilisateur connecté est admin
	     */
	    public boolean isAdmin() {
	        return isConnecte() && utilisateurConnecte.isAdmin();
	    }
	    
	    /**
	     * Vérifier une permission
	     */
	    public boolean hasPermission(String permission) {
	        if (!isConnecte()) {
	            return false;
	        }
	        
	        RoleUtilisateur role = utilisateurConnecte.getRole();
	        
	        switch (permission) {
	            case "GERER_PHARMACIES":
	                return role.peutGererPharmacies();
	            case "GERER_EMPLOYES":
	                return role.peutGererEmployes();
	            case "GERER_PRODUITS":
	                return role.peutGererProduits();
	            case "EFFECTUER_VENTES":
	                return role.peutEffectuerVentes();
	            case "VOIR_RAPPORTS":
	                return role.peutVoirRapports();
	            case "GERER_GARDES":
	                return role.peutGererGardes();
	            case "GERER_HORAIRES":
	                return role.peutGererHoraires();
	            case "GERER_PAIES":
	                return role.peutGererPaies();
	            default:
	                return false;
	        }
	    }
}
