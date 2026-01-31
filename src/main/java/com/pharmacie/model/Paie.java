package com.pharmacie.model;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Objects;

public class Paie {
	 	private Long idPaie;
	    private Long idEmploye;
	    private int mois;
	    private int annee;
	    private BigDecimal montantBase;
	    private BigDecimal heuresSupplementaires;
	    private BigDecimal primeGarde;
	    private BigDecimal penalite;
	    private BigDecimal montantTotal;
	    private LocalDateTime dateGeneration;
	    private boolean payee;
	    private LocalDate datePaiement;
	    
	    // Pour affichage
	    private String nomEmploye;

	    // Constructeurs
	    public Paie() {
	        this.dateGeneration = LocalDateTime.now();
	        this.payee = false;
	        this.montantBase = BigDecimal.ZERO;
	        this.heuresSupplementaires = BigDecimal.ZERO;
	        this.primeGarde = BigDecimal.ZERO;
	        this.penalite = BigDecimal.ZERO;
	        this.montantTotal = BigDecimal.ZERO;
	    }

	    public Paie(Long idEmploye, int mois, int annee) {
	        this();
	        this.idEmploye = idEmploye;
	        this.mois = mois;
	        this.annee = annee;
	    }

	    // Getters et Setters
	    public Long getIdPaie() {
	        return idPaie;
	    }

	    public void setIdPaie(Long idPaie) {
	        this.idPaie = idPaie;
	    }

	    public Long getIdEmploye() {
	        return idEmploye;
	    }

	    public void setIdEmploye(Long idEmploye) {
	        this.idEmploye = idEmploye;
	    }

	    public int getMois() {
	        return mois;
	    }

	    public void setMois(int mois) {
	        if (mois < 1 || mois > 12) {
	            throw new IllegalArgumentException("Le mois doit être entre 1 et 12");
	        }
	        this.mois = mois;
	    }

	    public int getAnnee() {
	        return annee;
	    }

	    public void setAnnee(int annee) {
	        this.annee = annee;
	    }

	    public BigDecimal getMontantBase() {
	        return montantBase;
	    }

	    public void setMontantBase(BigDecimal montantBase) {
	        this.montantBase = montantBase;
	    }

	    public BigDecimal getHeuresSupplementaires() {
	        return heuresSupplementaires;
	    }

	    public void setHeuresSupplementaires(BigDecimal heuresSupplementaires) {
	        this.heuresSupplementaires = heuresSupplementaires;
	    }

	    public BigDecimal getPrimeGarde() {
	        return primeGarde;
	    }

	    public void setPrimeGarde(BigDecimal primeGarde) {
	        this.primeGarde = primeGarde;
	    }

	    public BigDecimal getPenalite() {
	        return penalite;
	    }

	    public void setPenalite(BigDecimal penalite) {
	        this.penalite = penalite;
	    }

	    public BigDecimal getMontantTotal() {
	        return montantTotal;
	    }

	    public void setMontantTotal(BigDecimal montantTotal) {
	        this.montantTotal = montantTotal;
	    }

	    public LocalDateTime getDateGeneration() {
	        return dateGeneration;
	    }

	    public void setDateGeneration(LocalDateTime dateGeneration) {
	        this.dateGeneration = dateGeneration;
	    }

	    public boolean isPayee() {
	        return payee;
	    }

	    public void setPayee(boolean payee) {
	        this.payee = payee;
	    }

	    public LocalDate getDatePaiement() {
	        return datePaiement;
	    }

	    public void setDatePaiement(LocalDate datePaiement) {
	        this.datePaiement = datePaiement;
	    }

	    public String getNomEmploye() {
	        return nomEmploye;
	    }

	    public void setNomEmploye(String nomEmploye) {
	        this.nomEmploye = nomEmploye;
	    }

	    // Méthodes métier
	    public void calculerMontantTotal() {
	        this.montantTotal = montantBase
	                .add(heuresSupplementaires)
	                .add(primeGarde)
	                .subtract(penalite)
	                .setScale(2, RoundingMode.HALF_UP);
	    }

	    public void marquerPayee() {
	        this.payee = true;
	        this.datePaiement = LocalDate.now();
	    }

	    public void annulerPaiement() {
	        this.payee = false;
	        this.datePaiement = null;
	    }

	    public String getPeriode() {
	        return String.format("%02d/%d", mois, annee);
	    }

	    public String getPeriodeLongue() {
	        String[] moisNoms = {
	            "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
	            "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
	        };
	        return moisNoms[mois - 1] + " " + annee;
	    }

	    public YearMonth getYearMonth() {
	        return YearMonth.of(annee, mois);
	    }

	    public int getJoursOuvrablesDansMois() {
	        YearMonth yearMonth = YearMonth.of(annee, mois);
	        int joursOuvrables = 0;
	        
	        for (int jour = 1; jour <= yearMonth.lengthOfMonth(); jour++) {
	            LocalDate date = LocalDate.of(annee, mois, jour);
	            // Exclure samedi et dimanche
	            if (date.getDayOfWeek().getValue() < 6) {
	                joursOuvrables++;
	            }
	        }
	        return joursOuvrables;
	    }

	    public BigDecimal getTauxJournalier() {
	        if (montantBase.compareTo(BigDecimal.ZERO) > 0) {
	            int joursOuvrables = getJoursOuvrablesDansMois();
	            return montantBase.divide(BigDecimal.valueOf(joursOuvrables), 2, RoundingMode.HALF_UP);
	        }
	        return BigDecimal.ZERO;
	    }

	    public boolean estEnRetard() {
	        // Une paie est en retard si elle n'est pas payée après le 5 du mois suivant
	        if (!payee) {
	            LocalDate dateLimit = LocalDate.of(annee, mois, 1).plusMonths(1).plusDays(5);
	            return LocalDate.now().isAfter(dateLimit);
	        }
	        return false;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        Paie paie = (Paie) o;
	        return Objects.equals(idPaie, paie.idPaie);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(idPaie);
	    }

	    @Override
	    public String toString() {
	        return "Paie{" +
	                "idPaie=" + idPaie +
	                ", idEmploye=" + idEmploye +
	                ", periode=" + getPeriode() +
	                ", montantTotal=" + montantTotal +
	                ", payee=" + payee +
	                '}';
	    }

}
