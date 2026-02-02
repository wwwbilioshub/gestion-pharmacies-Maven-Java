package com.pharmacie.model;
import java.util.Objects;

public class LigneInventaire {
	 private Long idLigneInventaire;
	    private Long idInventaire;
	    private Long idProduit;
	    private int quantiteTheorique;
	    private int quantiteReelle;
	    private int ecart;
	    
	    // Pour affichage
	    private String nomProduit;
	    private String categorie;
	    private String reference;

	    // Constructeurs
	    public LigneInventaire() {
	        this.quantiteTheorique = 0;
	        this.quantiteReelle = 0;
	        this.ecart = 0;
	    }

	    public LigneInventaire(Long idInventaire, Long idProduit, int quantiteTheorique) {
	        this();
	        this.idInventaire = idInventaire;
	        this.idProduit = idProduit;
	        this.quantiteTheorique = quantiteTheorique;
	    }

	    // Getters et Setters
	    public Long getIdLigneInventaire() {
	        return idLigneInventaire;
	    }

	    public void setIdLigneInventaire(Long idLigneInventaire) {
	        this.idLigneInventaire = idLigneInventaire;
	    }

	    public Long getIdInventaire() {
	        return idInventaire;
	    }

	    public void setIdInventaire(Long idInventaire) {
	        this.idInventaire = idInventaire;
	    }

	    public Long getIdProduit() {
	        return idProduit;
	    }

	    public void setIdProduit(Long idProduit) {
	        this.idProduit = idProduit;
	    }

	    public int getQuantiteTheorique() {
	        return quantiteTheorique;
	    }

	    public void setQuantiteTheorique(int quantiteTheorique) {
	        this.quantiteTheorique = quantiteTheorique;
	        calculerEcart();
	    }

	    public int getQuantiteReelle() {
	        return quantiteReelle;
	    }

	    public void setQuantiteReelle(int quantiteReelle) {
	        this.quantiteReelle = quantiteReelle;
	        calculerEcart();
	    }

	    public int getEcart() {
	        return ecart;
	    }

	    public void setEcart(int ecart) {
	        this.ecart = ecart;
	    }

	    public String getNomProduit() {
	        return nomProduit;
	    }

	    public void setNomProduit(String nomProduit) {
	        this.nomProduit = nomProduit;
	    }

	    public String getCategorie() {
	        return categorie;
	    }

	    public void setCategorie(String categorie) {
	        this.categorie = categorie;
	    }

	    public String getReference() {
	        return reference;
	    }

	    public void setReference(String reference) {
	        this.reference = reference;
	    }

	    // Méthodes métier
	    public void calculerEcart() {
	        this.ecart = quantiteReelle - quantiteTheorique;
	    }

	    public boolean aUnEcart() {
	        return ecart != 0;
	    }

	    public boolean estExcedent() {
	        return ecart > 0;
	    }

	    public boolean estManquant() {
	        return ecart < 0;
	    }

	    public boolean estConforme() {
	        return ecart == 0;
	    }

	    public int getEcartAbsolu() {
	        return Math.abs(ecart);
	    }

	    public double getPourcentageEcart() {
	        if (quantiteTheorique == 0) {
	            return quantiteReelle > 0 ? 100.0 : 0.0;
	        }
	        return (ecart * 100.0) / quantiteTheorique;
	    }

	    public String getTypeEcart() {
	        if (estExcedent()) {
	            return "Excédent";
	        } else if (estManquant()) {
	            return "Manquant";
	        } else {
	            return "Conforme";
	        }
	    }
	    
	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        LigneInventaire that = (LigneInventaire) o;
	        return Objects.equals(idLigneInventaire, that.idLigneInventaire);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(idLigneInventaire);
	    }

	    @Override
	    public String toString() {
	        return "LigneInventaire{" +
	                "idLigneInventaire=" + idLigneInventaire +
	                ", idProduit=" + idProduit +
	                ", nomProduit='" + nomProduit + '\'' +
	                ", quantiteTheorique=" + quantiteTheorique +
	                ", quantiteReelle=" + quantiteReelle +
	                ", ecart=" + ecart +
	                ", typeEcart='" + getTypeEcart() + '\'' +
	                '}';
	    }
}
