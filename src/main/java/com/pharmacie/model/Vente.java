package com.pharmacie.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vente {

	 private Long idVente;
	    private String numeroTicket;
	    private LocalDateTime dateVente;
	    private BigDecimal montantTotal;
	    private Long idEmploye;
	    private Long idPharmacie;
	    private String modePaiement;
	    private String statut;
	    private List<LigneVente> lignesVente;

	    // Constructeurs
	    public Vente() {
	        this.dateVente = LocalDateTime.now();
	        this.montantTotal = BigDecimal.ZERO;
	        this.lignesVente = new ArrayList<>();
	        this.statut = "VALIDEE";
	        this.modePaiement = "ESPECES";
	    }

	    public Vente(Long idEmploye, Long idPharmacie) {
	        this();
	        this.idEmploye = idEmploye;
	        this.idPharmacie = idPharmacie;
	    }

	    // Getters et Setters
	    public Long getIdVente() {
	        return idVente;
	    }

	    public void setIdVente(Long idVente) {
	        this.idVente = idVente;
	    }

	    public String getNumeroTicket() {
	        return numeroTicket;
	    }

	    public void setNumeroTicket(String numeroTicket) {
	        this.numeroTicket = numeroTicket;
	    }

	    public LocalDateTime getDateVente() {
	        return dateVente;
	    }

	    public void setDateVente(LocalDateTime dateVente) {
	        this.dateVente = dateVente;
	    }

	    public BigDecimal getMontantTotal() {
	        return montantTotal;
	    }

	    public void setMontantTotal(BigDecimal montantTotal) {
	        this.montantTotal = montantTotal;
	    }

	    public Long getIdEmploye() {
	        return idEmploye;
	    }

	    public void setIdEmploye(Long idEmploye) {
	        this.idEmploye = idEmploye;
	    }

	    public Long getIdPharmacie() {
	        return idPharmacie;
	    }

	    public void setIdPharmacie(Long idPharmacie) {
	        this.idPharmacie = idPharmacie;
	    }

	    public String getModePaiement() {
	        return modePaiement;
	    }

	    public void setModePaiement(String modePaiement) {
	        this.modePaiement = modePaiement;
	    }

	    public String getStatut() {
	        return statut;
	    }

	    public void setStatut(String statut) {
	        this.statut = statut;
	    }

	    public List<LigneVente> getLignesVente() {
	        return lignesVente;
	    }

	    public void setLignesVente(List<LigneVente> lignesVente) {
	        this.lignesVente = lignesVente;
	    }

	    // Méthodes métier
	    public void ajouterLigne(LigneVente ligne) {
	        this.lignesVente.add(ligne);
	        calculerMontantTotal();
	    }

	    public void supprimerLigne(LigneVente ligne) {
	        this.lignesVente.remove(ligne);
	        calculerMontantTotal();
	    }

	    public void calculerMontantTotal() {
	        this.montantTotal = lignesVente.stream()
	                .map(LigneVente::getSousTotal)
	                .reduce(BigDecimal.ZERO, BigDecimal::add);
	    }

	    public String genererNumeroTicket() {
	        // Format: PH{idPharmacie}-{timestamp}
	        return "PH" + idPharmacie + "-" + System.currentTimeMillis();
	    }

	    public void valider() {
	        this.statut = "VALIDEE";
	        if (this.numeroTicket == null) {
	            this.numeroTicket = genererNumeroTicket();
	        }
	    }

	    public void annuler() {
	        this.statut = "ANNULEE";
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        Vente vente = (Vente) o;
	        return Objects.equals(idVente, vente.idVente);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(idVente);
	    }

	    @Override
	    public String toString() {
	        return "Vente{" +
	                "idVente=" + idVente +
	                ", numeroTicket='" + numeroTicket + '\'' +
	                ", dateVente=" + dateVente +
	                ", montantTotal=" + montantTotal +
	                ", statut='" + statut + '\'' +
	                '}';
	    }
}
