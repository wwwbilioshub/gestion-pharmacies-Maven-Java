package com.pharmacie.model;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Absence {
	 	private Long idAbsence;
	    private Long idEmploye;
	    private LocalDate dateDebut;
	    private LocalDate dateFin;
	    private String motif;
	    private boolean justifiee;
	    private String documentJustificatif;
	    private boolean approuvee;
	    private LocalDateTime dateCreation;
	    
	    // Pour affichage
	    private String nomEmploye;

	    // Constructeurs
	    public Absence() {
	        this.justifiee = false;
	        this.approuvee = false;
	        this.dateCreation = LocalDateTime.now();
	    }

	    public Absence(Long idEmploye, LocalDate dateDebut, LocalDate dateFin, String motif) {
	        this();
	        this.idEmploye = idEmploye;
	        this.dateDebut = dateDebut;
	        this.dateFin = dateFin;
	        this.motif = motif;
	    }

	    // Getters et Setters
	    public Long getIdAbsence() {
	        return idAbsence;
	    }

	    public void setIdAbsence(Long idAbsence) {
	        this.idAbsence = idAbsence;
	    }

	    public Long getIdEmploye() {
	        return idEmploye;
	    }

	    public void setIdEmploye(Long idEmploye) {
	        this.idEmploye = idEmploye;
	    }

	    public LocalDate getDateDebut() {
	        return dateDebut;
	    }

	    public void setDateDebut(LocalDate dateDebut) {
	        this.dateDebut = dateDebut;
	    }

	    public LocalDate getDateFin() {
	        return dateFin;
	    }

	    public void setDateFin(LocalDate dateFin) {
	        this.dateFin = dateFin;
	    }

	    public String getMotif() {
	        return motif;
	    }

	    public void setMotif(String motif) {
	        this.motif = motif;
	    }

	    public boolean isJustifiee() {
	        return justifiee;
	    }

	    public void setJustifiee(boolean justifiee) {
	        this.justifiee = justifiee;
	    }

	    public String getDocumentJustificatif() {
	        return documentJustificatif;
	    }

	    public void setDocumentJustificatif(String documentJustificatif) {
	        this.documentJustificatif = documentJustificatif;
	    }

	    public boolean isApprouvee() {
	        return approuvee;
	    }

	    public void setApprouvee(boolean approuvee) {
	        this.approuvee = approuvee;
	    }

	    public LocalDateTime getDateCreation() {
	        return dateCreation;
	    }

	    public void setDateCreation(LocalDateTime dateCreation) {
	        this.dateCreation = dateCreation;
	    }

	    public String getNomEmploye() {
	        return nomEmploye;
	    }

	    public void setNomEmploye(String nomEmploye) {
	        this.nomEmploye = nomEmploye;
	    }

	    // Méthodes métier
	    public long calculerNombreJours() {
	        if (dateDebut != null && dateFin != null) {
	            return ChronoUnit.DAYS.between(dateDebut, dateFin) + 1;
	        }
	        return 0;
	    }

	    public void approuver() {
	        this.approuvee = true;
	    }

	    public void rejeter() {
	        this.approuvee = false;
	    }

	    public void ajouterJustificatif(String cheminDocument) {
	        this.documentJustificatif = cheminDocument;
	        this.justifiee = true;
	    }

	    public boolean estEnCours() {
	        LocalDate aujourdhui = LocalDate.now();
	        return !aujourdhui.isBefore(dateDebut) && !aujourdhui.isAfter(dateFin);
	    }

	    public boolean estPassee() {
	        return LocalDate.now().isAfter(dateFin);
	    }

	    public boolean estAVenir() {
	        return LocalDate.now().isBefore(dateDebut);
	    }

	    public String getStatut() {
	        if (estEnCours()) {
	            return "En cours";
	        } else if (estPassee()) {
	            return "Passée";
	        } else {
	            return "À venir";
	        }
	    }

	    public boolean necessiteJustificatif() {
	        // Absences de plus de 3 jours nécessitent un justificatif
	        return calculerNombreJours() > 3;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        Absence absence = (Absence) o;
	        return Objects.equals(idAbsence, absence.idAbsence);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(idAbsence);
	    }

	    @Override
	    public String toString() {
	        return "Absence{" +
	                "idAbsence=" + idAbsence +
	                ", idEmploye=" + idEmploye +
	                ", dateDebut=" + dateDebut +
	                ", dateFin=" + dateFin +
	                ", nombreJours=" + calculerNombreJours() +
	                ", justifiee=" + justifiee +
	                ", approuvee=" + approuvee +
	                '}';
	    }
}
