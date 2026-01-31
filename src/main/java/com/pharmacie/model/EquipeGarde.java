package com.pharmacie.model;

import java.time.LocalTime;
import java.util.Objects;

public class EquipeGarde {
	
	private Long idEquipeGarde;
	private Long idGarde;
	private Long idEmploye;
	private LocalTime heureArrivee;
	private LocalTime heureDepart;
	private boolean present;
	

	// Constructeurs
	public EquipeGarde() {
	    this.present = false;
	}

	public EquipeGarde(Long idGarde, Long idEmploye) {
	    this();
	    this.idGarde = idGarde;
	    this.idEmploye = idEmploye;
	}

	// Getters et Setters
	public Long getIdEquipeGarde() {
	    return idEquipeGarde;
	}

	public void setIdEquipeGarde(Long idEquipeGarde) {
	    this.idEquipeGarde = idEquipeGarde;
	}

	public Long getIdGarde() {
	    return idGarde;
	}

	public void setIdGarde(Long idGarde) {
	    this.idGarde = idGarde;
	}

	public Long getIdEmploye() {
	    return idEmploye;
	}

	public void setIdEmploye(Long idEmploye) {
	    this.idEmploye = idEmploye;
	}

	public LocalTime getHeureArrivee() {
	    return heureArrivee;
	}

	public void setHeureArrivee(LocalTime heureArrivee) {
	    this.heureArrivee = heureArrivee;
	}

	public LocalTime getHeureDepart() {
	    return heureDepart;
	}

	public void setHeureDepart(LocalTime heureDepart) {
	    this.heureDepart = heureDepart;
	}

	public boolean isPresent() {
	    return present;
	}

	public void setPresent(boolean present) {
	    this.present = present;
	}



	// Méthodes métier
	public void enregistrerArrivee() {
	    this.heureArrivee = LocalTime.now();
	    this.present = true;
	}
	
	public void assigner(Long idGarde) {
		this.idGarde = idGarde;
	}

	public void enregistrerDepart() {
	    this.heureDepart = LocalTime.now();
	}

	public double calculerHeuresTravaillees() {
	    if (heureArrivee != null && heureDepart != null) {
	        long minutes = java.time.Duration.between(heureArrivee, heureDepart).toMinutes();
	        return minutes / 60.0;
	    }
	    return 0.0;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    EquipeGarde that = (EquipeGarde) o;
	    return Objects.equals(idEquipeGarde, that.idEquipeGarde);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(idEquipeGarde);
	}

	@Override
	public String toString() {
	    return "EquipeGarde{" +
	            "idEquipeGarde=" + idEquipeGarde +
	            ", idGarde=" + idGarde +
	            ", idEmploye=" + idEmploye +
	            ", present=" + present +
	            '}';
	}
}
