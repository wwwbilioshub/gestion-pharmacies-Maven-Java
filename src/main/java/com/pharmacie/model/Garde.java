package com.pharmacie.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Garde {
	
	private Long idGarde;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Long idPharmacie;
    private String description;
    private String statut;
    private LocalDateTime dateCreation;
    private List<EquipeGarde> equipeGarde;

    // Constructeurs
    public Garde() {
        this.statut = "PLANIFIEE";
        this.dateCreation = LocalDateTime.now();
        this.equipeGarde = new ArrayList<>();
    }

    public Garde(LocalDate dateDebut, LocalDate dateFin, Long idPharmacie) {
        this();
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.idPharmacie = idPharmacie;
    }

    // Getters et Setters
    public Long getIdGarde() {
        return idGarde;
    }

    public void setIdGarde(Long idGarde) {
        this.idGarde = idGarde;
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

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public Long getIdPharmacie() {
        return idPharmacie;
    }

    public void setIdPharmacie(Long idPharmacie) {
        this.idPharmacie = idPharmacie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public List<EquipeGarde> getEquipeGarde() {
        return equipeGarde;
    }

    public void setEquipeGarde(List<EquipeGarde> equipeGarde) {
        this.equipeGarde = equipeGarde;
    }

    // Méthodes métier
    public void annuler() {
        this.statut = "ANNULEE";
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

    public void demarrer() {
        this.statut = "EN_COURS";
    }

    public void terminer() {
        this.statut = "TERMINEE";
    }

    public int getNombreMembres() {
        return equipeGarde.size();
    }

    public long getDureeEnJours() {
        if (dateDebut != null && dateFin != null) {
            return java.time.temporal.ChronoUnit.DAYS.between(dateDebut, dateFin) + 1;
        }
        return 0;
    }

    public void ajouterMembre(EquipeGarde membre) {
        this.equipeGarde.add(membre);
    }

    public void retirerMembre(EquipeGarde membre) {
        this.equipeGarde.remove(membre);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Garde garde = (Garde) o;
        return Objects.equals(idGarde, garde.idGarde);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idGarde);
    }

    @Override
    public String toString() {
        return "Garde{" +
                "idGarde=" + idGarde +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", idPharmacie=" + idPharmacie +
                ", statut='" + statut + '\'' +
                '}';
    }
}
