package com.pharmacie.controllers;

import com.pharmacie.model.Utilisateur;

import com.pharmacie.services.AuthentificationService;

import javax.swing.*;
import java.util.Optional;

public class LoginController {
    
    private final AuthentificationService authService;
    
    public LoginController() {
        this.authService = new AuthentificationService();
    }
    
    /**
     * Gérer la connexion d'un utilisateur
     */
    public boolean login(String login, String motDePasse) {
        try {
            // Validation basique
            if (login == null || login.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "Veuillez saisir votre login", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (motDePasse == null || motDePasse.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, 
                    "Veuillez saisir votre mot de passe", 
                    "Erreur", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            // Authentification
            Optional<Utilisateur> userOpt = authService.authentifier(login, motDePasse);
            
            if (userOpt.isPresent()) {
                Utilisateur user = userOpt.get();
                JOptionPane.showMessageDialog(null, 
                    "Bienvenue " + user.getLogin() + " !", 
                    "Connexion réussie", 
                    JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Login ou mot de passe incorrect", 
                    "Erreur d'authentification", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Erreur lors de la connexion : " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    /**
     * Déconnecter l'utilisateur
     */
    public void logout() {
        authService.deconnecter();
        JOptionPane.showMessageDialog(null, 
            "Vous avez été déconnecté avec succès", 
            "Déconnexion", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Obtenir l'utilisateur connecté
     */
    public Utilisateur getUtilisateurConnecte() {
        return authService.getUtilisateurConnecte();
    }
    
    /**
     * Vérifier si un utilisateur est connecté
     */
    public boolean isConnecte() {
        return authService.isConnecte();
    }
    
    /**
     * Vérifier si l'utilisateur est admin
     */
    public boolean isAdmin() {
        return authService.isAdmin();
    }
    
    /**
     * Vérifier une permission
     */
    public boolean hasPermission(String permission) {
        return authService.hasPermission(permission);
    }
}
