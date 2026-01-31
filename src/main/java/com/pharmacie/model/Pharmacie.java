package com.pharmacie.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Pharmacie {
	private Long idPharmacie;
    private String nom;
    private String adresse;
    private String telephone;
    private String email;
    private boolean estDeGarde;
    private boolean active;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    // Constructeurs
    public Pharmacie() {
        this.active = true;
        this.estDeGarde = false;
        this.dateCreation = LocalDateTime.now();
    }

    public Pharmacie(String nom, String adresse, String telephone) {
        this();
        this.nom = nom;
        this.adresse = adresse;
        this.telephone = telephone;
    }

    // Getters et Setters
    public Long getIdPharmacie() {
        return idPharmacie;
    }

    public void setIdPharmacie(Long idPharmacie) {
        this.idPharmacie = idPharmacie;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEstDeGarde() {
        return estDeGarde;
    }

    public void setEstDeGarde(boolean estDeGarde) {
        this.estDeGarde = estDeGarde;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    // Méthodes métier
    public void activerGarde() {
        this.estDeGarde = true;
    }

    public void desactiverGarde() {
        this.estDeGarde = false;
    }

    public void desactiver() {
        this.active = false;
    }

    public void activer() {
        this.active = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pharmacie pharmacie = (Pharmacie) o;
        return Objects.equals(idPharmacie, pharmacie.idPharmacie);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPharmacie);
    }

    @Override
    public String toString() {
        return "Pharmacie{" +
                "idPharmacie=" + idPharmacie +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", telephone='" + telephone + '\'' +
                ", estDeGarde=" + estDeGarde +
                ", active=" + active +
                '}';
    }

}
