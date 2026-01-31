package com.pharmacie.model;

import java.math.BigDecimal;
import java.util.Objects;

public class LigneVente {
	
	private Long idLigneVente;
    private Long idVente;
    private Long idProduit;
    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;
    
    // Pour affichage
    private String nomProduit;

    // Constructeurs
    public LigneVente() {
        this.quantite = 1;
        this.sousTotal = BigDecimal.ZERO;
    }

    public LigneVente(Long idProduit, int quantite, BigDecimal prixUnitaire) {
        this();
        this.idProduit = idProduit;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        calculerSousTotal();
    }

    // Getters et Setters
    public Long getIdLigneVente() {
        return idLigneVente;
    }

    public void setIdLigneVente(Long idLigneVente) {
        this.idLigneVente = idLigneVente;
    }

    public Long getIdVente() {
        return idVente;
    }

    public void setIdVente(Long idVente) {
        this.idVente = idVente;
    }

    public Long getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(Long idProduit) {
        this.idProduit = idProduit;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
        calculerSousTotal();
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        calculerSousTotal();
    }

    public BigDecimal getSousTotal() {
        return sousTotal;
    }

    public void setSousTotal(BigDecimal sousTotal) {
        this.sousTotal = sousTotal;
    }

    public String getNomProduit() {
        return nomProduit;
    }

    public void setNomProduit(String nomProduit) {
        this.nomProduit = nomProduit;
    }

    // Méthodes métier
    public void calculerSousTotal() {
        if (prixUnitaire != null && quantite > 0) {
            this.sousTotal = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LigneVente that = (LigneVente) o;
        return Objects.equals(idLigneVente, that.idLigneVente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLigneVente);
    }

    @Override
    public String toString() {
        return "LigneVente{" +
                "idLigneVente=" + idLigneVente +
                ", idProduit=" + idProduit +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", sousTotal=" + sousTotal +
                '}';
    }

}
