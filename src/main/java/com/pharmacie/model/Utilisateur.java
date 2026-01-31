package com.pharmacie.model;

import com.pharmacie.enums.RoleUtilisateur;
import java.time.LocalDateTime;
import java.util.Objects;

public class Utilisateur {
	
	private Long idUtilisateur;
    private String login;
    private String motDePasse;
    private RoleUtilisateur role;
    private Long idEmploye;
    private boolean actif;
    private LocalDateTime derniereConnexion;
    private LocalDateTime dateCreation;

    // Constructeurs
    public Utilisateur() {
        this.actif = true;
        this.dateCreation = LocalDateTime.now();
    }

    public Utilisateur(String login, String motDePasse, RoleUtilisateur role) {
        this();
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Getters et Setters
    public Long getIdUtilisateur() {
        return idUtilisateur;
    }

    public void setIdUtilisateur(Long idUtilisateur) {
        this.idUtilisateur = idUtilisateur;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public RoleUtilisateur getRole() {
        return role;
    }

    public void setRole(RoleUtilisateur role) {
        this.role = role;
    }

    public Long getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(Long idEmploye) {
        this.idEmploye = idEmploye;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public LocalDateTime getDerniereConnexion() {
        return derniereConnexion;
    }

    public void setDerniereConnexion(LocalDateTime derniereConnexion) {
        this.derniereConnexion = derniereConnexion;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    // Méthodes métier
    public void enregistrerConnexion() {
        this.derniereConnexion = LocalDateTime.now();
    }

    public boolean hasRole(RoleUtilisateur role) {
        return this.role == role;
    }

    public boolean isAdmin() {
        return this.role == RoleUtilisateur.ADMINISTRATEUR;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return Objects.equals(idUtilisateur, that.idUtilisateur);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUtilisateur);
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "idUtilisateur=" + idUtilisateur +
                ", login='" + login + '\'' +
                ", role=" + role +
                ", actif=" + actif +
                '}';
    }

}
