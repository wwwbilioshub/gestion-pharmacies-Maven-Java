package com.pharmacie.model;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Inventaire {
	private Long idInventaire;
    private Long idPharmacie;
    private LocalDateTime dateInventaire;
    private Long idEmploye;
    private String statut;
    private String observations;
    private List<LigneInventaire> lignesInventaire;
    
    // Pour affichage
    private String nomPharmacie;
    private String nomEmploye;

    // Constructeurs
    public Inventaire() {
        this.dateInventaire = LocalDateTime.now();
        this.statut = "EN_COURS";
        this.lignesInventaire = new ArrayList<>();
    }

    public Inventaire(Long idPharmacie, Long idEmploye) {
        this();
        this.idPharmacie = idPharmacie;
        this.idEmploye = idEmploye;
    }

    // Getters et Setters
    public Long getIdInventaire() {
        return idInventaire;
    }

    public void setIdInventaire(Long idInventaire) {
        this.idInventaire = idInventaire;
    }

    public Long getIdPharmacie() {
        return idPharmacie;
    }

    public void setIdPharmacie(Long idPharmacie) {
        this.idPharmacie = idPharmacie;
    }

    public LocalDateTime getDateInventaire() {
        return dateInventaire;
    }

    public void setDateInventaire(LocalDateTime dateInventaire) {
        this.dateInventaire = dateInventaire;
    }

    public Long getIdEmploye() {
        return idEmploye;
    }

    public void setIdEmploye(Long idEmploye) {
        this.idEmploye = idEmploye;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public List<LigneInventaire> getLignesInventaire() {
        return lignesInventaire;
    }

    public void setLignesInventaire(List<LigneInventaire> lignesInventaire) {
        this.lignesInventaire = lignesInventaire;
    }

    public String getNomPharmacie() {
        return nomPharmacie;
    }

    public void setNomPharmacie(String nomPharmacie) {
        this.nomPharmacie = nomPharmacie;
    }

    public String getNomEmploye() {
        return nomEmploye;
    }

    public void setNomEmploye(String nomEmploye) {
        this.nomEmploye = nomEmploye;
    }

    // Méthodes métier
    public void ajouterLigne(LigneInventaire ligne) {
        this.lignesInventaire.add(ligne);
    }

    public void supprimerLigne(LigneInventaire ligne) {
        this.lignesInventaire.remove(ligne);
    }

    public void terminer() {
        this.statut = "TERMINE";
    }

    public void valider() {
        this.statut = "VALIDE";
    }

    public void annuler() {
        this.statut = "ANNULE";
    }

    public int getTotalProduits() {
        return lignesInventaire.size();
    }

    public int getTotalEcarts() {
        return (int) lignesInventaire.stream()
                .filter(ligne -> ligne.getEcart() != 0)
                .count();
    }

    public int getTotalExcedents() {
        return (int) lignesInventaire.stream()
                .filter(LigneInventaire::estExcedent)
                .count();
    }

    public int getTotalManquants() {
        return (int) lignesInventaire.stream()
                .filter(LigneInventaire::estManquant)
                .count();
    }

    public int getEcartTotalQuantite() {
        return lignesInventaire.stream()
                .mapToInt(LigneInventaire::getEcart)
                .sum();
    }

    public double getTauxPrecision() {
        if (lignesInventaire.isEmpty()) {
            return 100.0;
        }
        int produitsConformes = getTotalProduits() - getTotalEcarts();
        return (produitsConformes * 100.0) / getTotalProduits();
    }

    public boolean estModifiable() {
        return "EN_COURS".equals(statut);
    }

    public boolean estFinalise() {
        return "VALIDE".equals(statut) || "ANNULE".equals(statut);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inventaire that = (Inventaire) o;
        return Objects.equals(idInventaire, that.idInventaire);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idInventaire);
    }

    @Override
    public String toString() {
        return "Inventaire{" +
                "idInventaire=" + idInventaire +
                ", idPharmacie=" + idPharmacie +
                ", dateInventaire=" + dateInventaire +
                ", statut='" + statut + '\'' +
                ", totalProduits=" + getTotalProduits() +
                ", totalEcarts=" + getTotalEcarts() +
                '}';
    }

}
