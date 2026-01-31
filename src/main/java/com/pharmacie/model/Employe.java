package com.pharmacie.model;

import com.pharmacie.enums.PosteEmploye;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Employe {

	private Long idEmploye;
    private String nom;
    private String prenom;
    private String sexe;
    private LocalDate dateNaissance;
    private String telephone;
    private String email;
    private String adresse;
    private PosteEmploye poste;
    private BigDecimal salaireBase;
    private BigDecimal tauxHoraire;
    private Long idPharmacie;
    private boolean actif;
    private LocalDate dateEmbauche;
    private LocalDateTime dateCreation;

    // Constructeurs
    public Employe() {
        this.actif = true;
        this.dateCreation = LocalDateTime.now();
        this.dateEmbauche = LocalDate.now();
    }

    public Employe(String nom, String prenom, PosteEmploye poste, BigDecimal salaireBase) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.salaireBase = salaireBase;
    }

    // Getters et Setters
    public Long getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(Long idEmploye) {
        this.idEmploye = idEmploye;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public PosteEmploye getPoste() {
        return poste;
    }

    public void setPoste(PosteEmploye poste) {
        this.poste = poste;
    }

    public BigDecimal getSalaireBase() {
        return salaireBase;
    }

    public void setSalaireBase(BigDecimal salaireBase) {
        this.salaireBase = salaireBase;
    }

    public BigDecimal getTauxHoraire() {
        return tauxHoraire;
    }

    public void setTauxHoraire(BigDecimal tauxHoraire) {
        this.tauxHoraire = tauxHoraire;
    }

    public Long getIdPharmacie() {
        return idPharmacie;
    }

    public void setIdPharmacie(Long idPharmacie) {
        this.idPharmacie = idPharmacie;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public LocalDate getDateEmbauche() {
        return dateEmbauche;
    }

    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    // Méthodes métier
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    public int getAnciennete() {
        return LocalDate.now().getYear() - dateEmbauche.getYear();
    }

    public void desactiver() {
        this.actif = false;
    }

    public void activer() {
        this.actif = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employe employe = (Employe) o;
        return Objects.equals(idEmploye, employe.idEmploye);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmploye);
    }

    @Override
    public String toString() {
        return "Employe{" +
                "idEmploye=" + idEmploye +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", poste=" + poste +
                ", actif=" + actif +
                '}';
    }
}
