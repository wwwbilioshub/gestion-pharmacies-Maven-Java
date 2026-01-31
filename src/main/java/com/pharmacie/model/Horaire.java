package com.pharmacie.model;
import com.pharmacie.enums.TypeHoraire;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;
import java.util.Objects;


public class Horaire {
	 private Long idHoraire;
	    private Long idEmploye;
	    private LocalDate date;
	    private LocalTime heureDebut;
	    private LocalTime heureFin;
	    private double heuresTravaillees;
	    private double heuresSupplementaires;
	    private TypeHoraire typeHoraire;
	    private boolean present;
	    
	    // Pour affichage
	    private String nomEmploye;

	    // Constructeurs
	    public Horaire() {
	        this.present = true;
	        this.heuresTravaillees = 0.0;
	        this.heuresSupplementaires = 0.0;
	    }

	    public Horaire(Long idEmploye, LocalDate date, TypeHoraire typeHoraire) {
	        this();
	        this.idEmploye = idEmploye;
	        this.date = date;
	        this.typeHoraire = typeHoraire;
	        // Initialiser avec les heures par défaut du type
	        if (typeHoraire != null) {
	            this.heureDebut = typeHoraire.getHeureDebut();
	            this.heureFin = typeHoraire.getHeureFin();
	        }
	    }

	    // Getters et Setters
	    public Long getIdHoraire() {
	        return idHoraire;
	    }

	    public void setIdHoraire(Long idHoraire) {
	        this.idHoraire = idHoraire;
	    }

	    public Long getIdEmploye() {
	        return idEmploye;
	    }

	    public void setIdEmploye(Long idEmploye) {
	        this.idEmploye = idEmploye;
	    }

	    public LocalDate getDate() {
	        return date;
	    }

	    public void setDate(LocalDate date) {
	        this.date = date;
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

	    public double getHeuresTravaillees() {
	        return heuresTravaillees;
	    }

	    public void setHeuresTravaillees(double heuresTravaillees) {
	        this.heuresTravaillees = heuresTravaillees;
	    }

	    public double getHeuresSupplementaires() {
	        return heuresSupplementaires;
	    }

	    public void setHeuresSupplementaires(double heuresSupplementaires) {
	        this.heuresSupplementaires = heuresSupplementaires;
	    }

	    public TypeHoraire getTypeHoraire() {
	        return typeHoraire;
	    }

	    public void setTypeHoraire(TypeHoraire typeHoraire) {
	        this.typeHoraire = typeHoraire;
	    }

	    public boolean isPresent() {
	        return present;
	    }

	    public void setPresent(boolean present) {
	        this.present = present;
	    }

	    public String getNomEmploye() {
	        return nomEmploye;
	    }

	    public void setNomEmploye(String nomEmploye) {
	        this.nomEmploye = nomEmploye;
	    }

	    // Méthodes métier
	    public void calculerHeures() {
	        if (heureDebut != null && heureFin != null && present) {
	            Duration duration;
	            
	            // Gérer le cas où l'horaire passe minuit (horaire de nuit)
	            if (heureFin.isBefore(heureDebut)) {
	                // L'horaire passe minuit
	                Duration toMidnight = Duration.between(heureDebut, LocalTime.MAX);
	                Duration fromMidnight = Duration.between(LocalTime.MIN, heureFin);
	                duration = toMidnight.plus(fromMidnight).plusMinutes(1);
	            } else {
	                duration = Duration.between(heureDebut, heureFin);
	            }
	            
	            this.heuresTravaillees = duration.toMinutes() / 60.0;
	            
	            // Calculer les heures supplémentaires (au-delà de 8h)
	            double heuresStandard = 8.0;
	            if (this.heuresTravaillees > heuresStandard) {
	                this.heuresSupplementaires = this.heuresTravaillees - heuresStandard;
	            } else {
	                this.heuresSupplementaires = 0.0;
	            }
	        } else if (!present) {
	            this.heuresTravaillees = 0.0;
	            this.heuresSupplementaires = 0.0;
	        }
	    }

	    public void marquerAbsent() {
	        this.present = false;
	        this.heuresTravaillees = 0.0;
	        this.heuresSupplementaires = 0.0;
	    }

	    public void marquerPresent() {
	        this.present = true;
	        calculerHeures();
	    }

	    public void enregistrerArrivee() {
	        this.heureDebut = LocalTime.now();
	        this.present = true;
	    }

	    public void enregistrerDepart() {
	        this.heureFin = LocalTime.now();
	        calculerHeures();
	    }

	    public boolean estRetard() {
	        if (typeHoraire != null && heureDebut != null) {
	            LocalTime heureTheorique = typeHoraire.getHeureDebut();
	            return heureDebut.isAfter(heureTheorique.plusMinutes(15)); // Tolérance de 15 min
	        }
	        return false;
	    }

	    public boolean estDepartAnticipe() {
	        if (typeHoraire != null && heureFin != null) {
	            LocalTime heureTheorique = typeHoraire.getHeureFin();
	            return heureFin.isBefore(heureTheorique.minusMinutes(15)); // Tolérance de 15 min
	        }
	        return false;
	    }

	    public int getMinutesRetard() {
	        if (estRetard() && typeHoraire != null && heureDebut != null) {
	            LocalTime heureTheorique = typeHoraire.getHeureDebut();
	            return (int) Duration.between(heureTheorique, heureDebut).toMinutes();
	        }
	        return 0;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        Horaire horaire = (Horaire) o;
	        return Objects.equals(idHoraire, horaire.idHoraire);
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(idHoraire);
	    }

	    @Override
	    public String toString() {
	        return "Horaire{" +
	                "idHoraire=" + idHoraire +
	                ", idEmploye=" + idEmploye +
	                ", date=" + date +
	                ", typeHoraire=" + typeHoraire +
	                ", heuresTravaillees=" + String.format("%.2f", heuresTravaillees) +
	                ", present=" + present +
	                '}';
	    }

}
