package com.pharmacie.enums;

public enum PosteEmploye {
	PHARMACIEN_TITULAIRE("Pharmacien Titulaire", "Responsable de la pharmacie"),
    PHARMACIEN_ASSISTANT("Pharmacien Assistant", "Assistant du pharmacien titulaire"),
    PREPARATEUR("Préparateur en Pharmacie", "Prépare et gère les commandes"),
    CAISSIER("Caissier/Caissière", "Gère les transactions"),
    GESTIONNAIRE_STOCK("Gestionnaire de Stock", "Gère les stocks et inventaires"),
    AGENT_ENTRETIEN("Agent d'Entretien", "Responsable de la propreté"),
    LIVREUR("Livreur", "Assure les livraisons à domicile");

    private final String libelle;
    private final String description;

    PosteEmploye(String libelle, String description) {
        this.libelle = libelle;
        this.description = description;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return libelle;
    }

    // Méthode utilitaire pour obtenir le poste depuis une chaîne
    public static PosteEmploye fromString(String text) {
        for (PosteEmploye poste : PosteEmploye.values()) {
            if (poste.name().equalsIgnoreCase(text) || poste.libelle.equalsIgnoreCase(text)) {
                return poste;
            }
        }
        throw new IllegalArgumentException("Aucun poste ne correspond à : " + text);
    }

    // Vérifier si le poste a accès au système
    public boolean aAccesSysteme() {
        return this != AGENT_ENTRETIEN && this != LIVREUR;
    }

    // Vérifier si le poste peut effectuer des ventes
    public boolean peutVendre() {
        return this == PHARMACIEN_TITULAIRE || 
               this == PHARMACIEN_ASSISTANT || 
               this == CAISSIER;
    }

    // Vérifier si le poste peut gérer le stock
    public boolean peutGererStock() {
        return this == PHARMACIEN_TITULAIRE || 
               this == GESTIONNAIRE_STOCK || 
               this == PREPARATEUR;
    }
}
