package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.GardeDAO;
import com.pharmacie.model.Garde;
import com.pharmacie.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GardeDAOImpl implements GardeDAO{
	@Override
    public Garde create(Garde garde) {
        String sql = "INSERT INTO garde (date_debut, date_fin, heure_debut, heure_fin, id_pharmacie, description, statut) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setDate(1, Date.valueOf(garde.getDateDebut()));
            stmt.setDate(2, Date.valueOf(garde.getDateFin()));
            stmt.setTime(3, Time.valueOf(garde.getHeureDebut()));
            stmt.setTime(4, Time.valueOf(garde.getHeureFin()));
            stmt.setLong(5, garde.getIdPharmacie());
            stmt.setString(6, garde.getDescription());
            stmt.setString(7, garde.getStatut());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    garde.setIdGarde(rs.getLong(1));
                }
            }
            
            return garde;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la garde", e);
        }
    }

    @Override
    public Garde update(Garde garde) {
        String sql = "UPDATE garde SET date_debut = ?, date_fin = ?, heure_debut = ?, heure_fin = ?, description = ?, statut = ? WHERE id_garde = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(garde.getDateDebut()));
            stmt.setDate(2, Date.valueOf(garde.getDateFin()));
            stmt.setTime(3, Time.valueOf(garde.getHeureDebut()));
            stmt.setTime(4, Time.valueOf(garde.getHeureFin()));
            stmt.setString(5, garde.getDescription());
            stmt.setString(6, garde.getStatut());
            stmt.setLong(7, garde.getIdGarde());
            
            stmt.executeUpdate();
            return garde;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la garde", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "UPDATE garde SET statut = 'ANNULEE' WHERE id_garde = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la garde", e);
        }
    }

    @Override
    public Optional<Garde> findById(Long id) {
        String sql = "SELECT * FROM garde WHERE id_garde = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la garde", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<Garde> findAll() {
        List<Garde> gardes = new ArrayList<>();
        String sql = "SELECT * FROM garde ORDER BY date_debut DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                gardes.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des gardes", e);
        }
        
        return gardes;
    }
    
    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM garde WHERE statut != 'ANNULEE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des gardes", e);
        }
        
        return 0;
    }

    @Override
    public boolean exists(Long id) {
        String sql = "SELECT COUNT(*) FROM garde WHERE id_garde = ?";
        
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
    public List<Garde> findByPharmacie(Long idPharmacie) {
        List<Garde> gardes = new ArrayList<>();
        String sql = "SELECT * FROM garde WHERE id_pharmacie = ? ORDER BY date_debut DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    gardes.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des gardes", e);
        }
        
        return gardes;
    }

    @Override
    public List<Garde> findByPeriode(LocalDate debut, LocalDate fin) {
        List<Garde> gardes = new ArrayList<>();
        String sql = "SELECT * FROM garde WHERE date_debut <= ? AND date_fin >= ? ORDER BY date_debut";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(fin));
            stmt.setDate(2, Date.valueOf(debut));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    gardes.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des gardes par période", e);
        }
        
        return gardes;
    }

    @Override
    public List<Garde> findEnCours() {
        List<Garde> gardes = new ArrayList<>();
        String sql = "SELECT * FROM garde WHERE statut = 'EN_COURS' ORDER BY date_debut";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                gardes.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des gardes en cours", e);
        }
        
        return gardes;
    }

    @Override
    public List<Garde> findAVenir(Long idPharmacie) {
        List<Garde> gardes = new ArrayList<>();
        String sql = "SELECT * FROM garde WHERE id_pharmacie = ? AND date_debut > CURDATE() AND statut = 'PLANIFIEE' ORDER BY date_debut";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    gardes.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des gardes à venir", e);
        }
        
        return gardes;
    }

    @Override
    public boolean isDeGarde(Long idPharmacie, LocalDate date) {
        String sql = "SELECT COUNT(*) FROM garde WHERE id_pharmacie = ? AND ? BETWEEN date_debut AND date_fin AND statut IN ('PLANIFIEE', 'EN_COURS')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            stmt.setDate(2, Date.valueOf(date));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la vérification de la garde", e);
        }
        
        return false;
    }

    @Override
    public boolean updateStatut(Long id, String statut) {
        String sql = "UPDATE garde SET statut = ? WHERE id_garde = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, statut);
            stmt.setLong(2, id);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du statut", e);
        }
    }

    private Garde mapResultSetToEntity(ResultSet rs) throws SQLException {
        Garde garde = new Garde();
        garde.setIdGarde(rs.getLong("id_garde"));
        garde.setDateDebut(rs.getDate("date_debut").toLocalDate());
        garde.setDateFin(rs.getDate("date_fin").toLocalDate());
        garde.setHeureDebut(rs.getTime("heure_debut").toLocalTime());
        garde.setHeureFin(rs.getTime("heure_fin").toLocalTime());
        garde.setIdPharmacie(rs.getLong("id_pharmacie"));
        garde.setDescription(rs.getString("description"));
        garde.setStatut(rs.getString("statut"));
        garde.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        
        return garde;
    }


}
