package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.InventaireDAO;
import com.pharmacie.model.Inventaire;
import com.pharmacie.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InventaireDAOImpl implements InventaireDAO {
	 @Override
	    public Inventaire create(Inventaire inventaire) {
	        String sql = "INSERT INTO inventaire (id_pharmacie, date_inventaire, id_employe, statut, observations) VALUES (?, ?, ?, ?, ?)";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	            
	            stmt.setLong(1, inventaire.getIdPharmacie());
	            stmt.setTimestamp(2, Timestamp.valueOf(inventaire.getDateInventaire()));
	            stmt.setLong(3, inventaire.getIdEmploye());
	            stmt.setString(4, inventaire.getStatut());
	            stmt.setString(5, inventaire.getObservations());
	            
	            stmt.executeUpdate();
	            
	            try (ResultSet rs = stmt.getGeneratedKeys()) {
	                if (rs.next()) {
	                    inventaire.setIdInventaire(rs.getLong(1));
	                }
	            }
	            
	            return inventaire;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la création de l'inventaire", e);
	        }
	    }

	    @Override
	    public Inventaire update(Inventaire inventaire) {
	        String sql = "UPDATE inventaire SET statut = ?, observations = ? WHERE id_inventaire = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setString(1, inventaire.getStatut());
	            stmt.setString(2, inventaire.getObservations());
	            stmt.setLong(3, inventaire.getIdInventaire());
	            
	            stmt.executeUpdate();
	            return inventaire;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la mise à jour de l'inventaire", e);
	        }
	    }

	    @Override
	    public boolean delete(Long id) {
	        String sql = "UPDATE inventaire SET statut = 'ANNULE' WHERE id_inventaire = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, id);
	            return stmt.executeUpdate() > 0;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la suppression de l'inventaire", e);
	        }
	    }

	    @Override
	    public Optional<Inventaire> findById(Long id) {
	        String sql = "SELECT i.*, p.nom as nom_pharmacie, CONCAT(e.prenom, ' ', e.nom) as nom_employe " +
	                     "FROM inventaire i " +
	                     "LEFT JOIN pharmacie p ON i.id_pharmacie = p.id_pharmacie " +
	                     "LEFT JOIN employe e ON i.id_employe = e.id_employe " +
	                     "WHERE i.id_inventaire = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, id);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return Optional.of(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la recherche de l'inventaire", e);
	        }
	        
	        return Optional.empty();
	    }

	    @Override
	    public List<Inventaire> findAll() {
	        List<Inventaire> inventaires = new ArrayList<>();
	        String sql = "SELECT i.*, p.nom as nom_pharmacie, CONCAT(e.prenom, ' ', e.nom) as nom_employe " +
	                     "FROM inventaire i " +
	                     "LEFT JOIN pharmacie p ON i.id_pharmacie = p.id_pharmacie " +
	                     "LEFT JOIN employe e ON i.id_employe = e.id_employe " +
	                     "ORDER BY i.date_inventaire DESC";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            while (rs.next()) {
	                inventaires.add(mapResultSetToEntity(rs));
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération des inventaires", e);
	        }
	        
	        return inventaires;
	    }

	    @Override
	    public long count() {
	        String sql = "SELECT COUNT(*) FROM inventaire WHERE statut != 'ANNULE'";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            if (rs.next()) {
	                return rs.getLong(1);
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors du comptage des inventaires", e);
	        }
	        
	        return 0;
	    }

	    @Override
	    public boolean exists(Long id) {
	        String sql = "SELECT COUNT(*) FROM inventaire WHERE id_inventaire = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, id);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return rs.getInt(1) > 0;
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la vérification de l'existence", e);
	        }
	        
	        return false;
	    }

	    @Override
	    public List<Inventaire> findByPharmacie(Long idPharmacie) {
	        List<Inventaire> inventaires = new ArrayList<>();
	        String sql = "SELECT i.*, p.nom as nom_pharmacie, CONCAT(e.prenom, ' ', e.nom) as nom_employe " +
	                     "FROM inventaire i " +
	                     "LEFT JOIN pharmacie p ON i.id_pharmacie = p.id_pharmacie " +
	                     "LEFT JOIN employe e ON i.id_employe = e.id_employe " +
	                     "WHERE i.id_pharmacie = ? ORDER BY i.date_inventaire DESC";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, idPharmacie);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    inventaires.add(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération des inventaires", e);
	        }
	        
	        return inventaires;
	    }

	    @Override
	    public List<Inventaire> findByPeriode(Long idPharmacie, LocalDateTime debut, LocalDateTime fin) {
	        List<Inventaire> inventaires = new ArrayList<>();
	        String sql = "SELECT i.*, p.nom as nom_pharmacie, CONCAT(e.prenom, ' ', e.nom) as nom_employe " +
	                     "FROM inventaire i " +
	                     "LEFT JOIN pharmacie p ON i.id_pharmacie = p.id_pharmacie " +
	                     "LEFT JOIN employe e ON i.id_employe = e.id_employe " +
	                     "WHERE i.id_pharmacie = ? AND i.date_inventaire BETWEEN ? AND ? " +
	                     "ORDER BY i.date_inventaire DESC";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, idPharmacie);
	            stmt.setTimestamp(2, Timestamp.valueOf(debut));
	            stmt.setTimestamp(3, Timestamp.valueOf(fin));
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    inventaires.add(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération des inventaires par période", e);
	        }
	        
	        return inventaires;
	    }

	    @Override
	    public List<Inventaire> findByStatut(String statut) {
	        List<Inventaire> inventaires = new ArrayList<>();
	        String sql = "SELECT i.*, p.nom as nom_pharmacie, CONCAT(e.prenom, ' ', e.nom) as nom_employe " +
	                     "FROM inventaire i " +
	                     "LEFT JOIN pharmacie p ON i.id_pharmacie = p.id_pharmacie " +
	                     "LEFT JOIN employe e ON i.id_employe = e.id_employe " +
	                     "WHERE i.statut = ? ORDER BY i.date_inventaire DESC";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setString(1, statut);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    inventaires.add(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération des inventaires par statut", e);
	        }
	        
	        return inventaires;
	    }

	    @Override
	    public List<Inventaire> findEnCours() {
	        return findByStatut("EN_COURS");
	    }

	    @Override
	    public boolean updateStatut(Long id, String statut) {
	        String sql = "UPDATE inventaire SET statut = ? WHERE id_inventaire = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setString(1, statut);
	            stmt.setLong(2, id);
	            
	            return stmt.executeUpdate() > 0;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la mise à jour du statut", e);
	        }
	    }

	    @Override
	    public boolean hasInventaireEnCours(Long idPharmacie) {
	        String sql = "SELECT COUNT(*) FROM inventaire WHERE id_pharmacie = ? AND statut = 'EN_COURS'";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, idPharmacie);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return rs.getInt(1) > 0;
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la vérification de l'inventaire en cours", e);
	        }
	        
	        return false;
	    }

	    private Inventaire mapResultSetToEntity(ResultSet rs) throws SQLException {
	        Inventaire inventaire = new Inventaire();
	        inventaire.setIdInventaire(rs.getLong("id_inventaire"));
	        inventaire.setIdPharmacie(rs.getLong("id_pharmacie"));
	        inventaire.setDateInventaire(rs.getTimestamp("date_inventaire").toLocalDateTime());
	        inventaire.setIdEmploye(rs.getLong("id_employe"));
	        inventaire.setStatut(rs.getString("statut"));
	        inventaire.setObservations(rs.getString("observations"));
	        inventaire.setNomPharmacie(rs.getString("nom_pharmacie"));
	        inventaire.setNomEmploye(rs.getString("nom_employe"));
	        
	        return inventaire;
	    }
}
