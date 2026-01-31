package com.pharmacie.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Produit {

	private Long idProduit;
    private String nom;
    private String description;
    private String categorie;
    private BigDecimal prixUnitaire;
    private int quantiteStock;
    private int seuilMinimal;
    private LocalDate dateExpiration;
    private String codeBarre;
    private String reference;
    private Long idPharmacie;
    private boolean actif;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    // Constructeurs
    public Produit() {
        this.actif = true;
        this.seuilMinimal = 10;
        this.quantiteStock = 0;
        this.dateCreation = LocalDateTime.now();
    }

    public Produit(String nom, String categorie, BigDecimal prixUnitaire, int quantiteStock) {
        this();
        this.nom = nom;
        this.categorie = categorie;
        this.prixUnitaire = prixUnitaire;
        this.quantiteStock = quantiteStock;
    }

    // Getters et Setters
    public Long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public int getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(int quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public int getSeuilMinimal() {
        return seuilMinimal;
    }

    public void setSeuilMinimal(int seuilMinimal) {
        this.seuilMinimal = seuilMinimal;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getCodeBarre() {
        return codeBarre;
    }

    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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
    public boolean estEnRuptureStock() {
        return quantiteStock <= seuilMinimal;
    }

    public boolean estPerime() {
        if (dateExpiration == null) return false;
        return LocalDate.now().isAfter(dateExpiration);
    }

    public boolean estProchePeremption(int joursAvant) {
        if (dateExpiration == null) return false;
        LocalDate dateAlerte = LocalDate.now().plusDays(joursAvant);
        return dateExpiration.isBefore(dateAlerte) || dateExpiration.isEqual(dateAlerte);
    }

    public void ajouterStock(int quantite) {
        this.quantiteStock += quantite;
    }

    public void retirerStock(int quantite) {
        if (quantite > this.quantiteStock) {
            throw new IllegalArgumentException("Stock insuffisant");
        }
        this.quantiteStock -= quantite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produit produit = (Produit) o;
        return Objects.equals(idProduit, produit.idProduit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduit);
    }

    @Override
    public String toString() {
        return "Produit{" +
                "idProduit=" + idProduit +
                ", nom='" + nom + '\'' +
                ", categorie='" + categorie + '\'' +
                ", prixUnitaire=" + prixUnitaire +
                ", quantiteStock=" + quantiteStock +
                ", seuilMinimal=" + seuilMinimal +
                '}';
    }
}
