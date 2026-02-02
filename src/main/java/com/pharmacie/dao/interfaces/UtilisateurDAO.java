package com.pharmacie.dao.interfaces;
import java.util.List;
import java.util.Optional;

import com.pharmacie.enums.RoleUtilisateur;
import com.pharmacie.model.Utilisateur;

public interface UtilisateurDAO extends GenericDAO<Utilisateur, Long> {
	/**
     * Trouver un utilisateur par login
     */
    Optional<Utilisateur> findByLogin(String login);
    
    /**
     * Authentifier un utilisateur
     */
    Optional<Utilisateur> authenticate(String login, String motDePasse);
    
    /**
     * Trouver les utilisateurs actifs
     */
    List<Utilisateur> findActifs();
    
    /**
     * Trouver les utilisateurs par rôle
     */
    List<Utilisateur> findByRole(RoleUtilisateur role);
    
    /**
     * Vérifier si un login existe déjà
     */
    boolean loginExists(String login);
    
    /**
     * Mettre à jour le mot de passe
     */
    boolean updatePassword(Long id, String nouveauMotDePasse);
    
    /**
     * Enregistrer la dernière connexion
     */
    boolean updateDerniereConnexion(Long id);
    
    /**
     * Activer/Désactiver un utilisateur
     */
    boolean toggleActif(Long id);

}
