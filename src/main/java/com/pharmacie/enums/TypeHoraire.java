package com.pharmacie.enums;
import java.time.LocalTime;

public enum TypeHoraire {
	 MATIN("Matin", LocalTime.of(8, 0), LocalTime.of(16, 0)),
	    APRES_MIDI("Après-midi", LocalTime.of(13, 0), LocalTime.of(21, 0)),
	    NUIT("Nuit", LocalTime.of(21, 0), LocalTime.of(7, 0)),
	    GARDE("Garde", LocalTime.of(21, 0), LocalTime.of(7, 0));

	    private final String libelle;
	    private final LocalTime heureDebut;
	    private final LocalTime heureFin;

	    TypeHoraire(String libelle, LocalTime heureDebut, LocalTime heureFin) {
	        this.libelle = libelle;
	        this.heureDebut = heureDebut;
	        this.heureFin = heureFin;
	    }

	    public String getLibelle() {
	        return libelle;
	    }

	    public LocalTime getHeureDebut() {
	        return heureDebut;
	    }

	    public LocalTime getHeureFin() {
	        return heureFin;
	    }

	    @Override
	    public String toString() {
	        return libelle;
	    }

	    // Méthode utilitaire pour obtenir le type depuis une chaîne
	    public static TypeHoraire fromString(String text) {
	        for (TypeHoraire type : TypeHoraire.values()) {
	            if (type.name().equalsIgnoreCase(text) || type.libelle.equalsIgnoreCase(text)) {
	                return type;
	            }
	        }
	        throw new IllegalArgumentException("Aucun type d'horaire ne correspond à : " + text);
	    }

	    // Calculer la durée standard en heures
	    public double getDureeStandard() {
	        if (this == NUIT || this == GARDE) {
	            // Pour les horaires de nuit qui passent minuit
	            return 10.0; // 21h à 7h = 10 heures
	        }
	        return 8.0; // Durée standard de travail
	    }

	    // Vérifier si c'est un horaire de nuit
	    public boolean estHoraireNuit() {
	        return this == NUIT || this == GARDE;
	    }

	    // Obtenir le multiplicateur de prime (pour les heures de nuit)
	    public double getPrimeMultiplicateur() {
	        if (estHoraireNuit()) {
	            return 1.5; // 50% de prime pour les horaires de nuit
	        }
	        return 1.0; // Pas de prime pour les horaires normaux
	    }

}
