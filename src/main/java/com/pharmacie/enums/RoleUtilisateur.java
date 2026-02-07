package com.pharmacie.enums;

public enum RoleUtilisateur {
	 ADMINISTRATEUR("Administrateur", "Accès complet au système"),
	    PHARMACIEN("Pharmacien", "Accès aux ventes et gestion du stock"),
	    CAISSIER("Caissier", "Accès uniquement aux ventes"),
	    GESTIONNAIRE_STOCK("Gestionnaire de Stock", "Accès uniquement à la gestion du stock");

	    private final String libelle;
	    private final String description;

	    RoleUtilisateur(String libelle, String description) {
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

	    // Méthode utilitaire pour obtenir le rôle depuis une chaîne
	    public static RoleUtilisateur fromString(String text) {
	        for (RoleUtilisateur role : RoleUtilisateur.values()) {
	            if (role.name().equalsIgnoreCase(text) || role.libelle.equalsIgnoreCase(text)) {
	                return role;
	            }
	        }
	        throw new IllegalArgumentException("Aucun rôle ne correspond à : " + text);
	    }

	    // Permissions
	    public boolean peutGererPharmacies() {
	        return this == ADMINISTRATEUR;
	    }

	    public boolean peutGererEmployes() {
	        return this == ADMINISTRATEUR;
	    }

	    public boolean peutGererProduits() {
	        return this == ADMINISTRATEUR || this == PHARMACIEN || this == GESTIONNAIRE_STOCK;
	    }

	    public boolean peutEffectuerVentes() {
	        return this == ADMINISTRATEUR || this == PHARMACIEN || this == CAISSIER;
	    }

	    public boolean peutVoirRapports() {
	        return this == ADMINISTRATEUR || this == PHARMACIEN;
	    }

	    public boolean peutGererGardes() {
	        return this == ADMINISTRATEUR || this == PHARMACIEN;
	    }

	    public boolean peutGererHoraires() {
	        return this == ADMINISTRATEUR || this == PHARMACIEN;
	    }

	    public boolean peutGererPaies() {
	        return this == ADMINISTRATEUR;
	    }
}
