package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.PharmacieDAO;
import com.pharmacie.model.Pharmacie;
import com.pharmacie.util.DatabaseConnection;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.sql.*;


public class PharmacieDAOImpl implements PharmacieDAO {
	
	@Override
    public Pharmacie create(Pharmacie pharmacie) {
        String sql = "INSERT INTO pharmacie (nom, adresse, telephone, email, est_de_garde, active) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, pharmacie.getNom());
            stmt.setString(2, pharmacie.getAdresse());
            stmt.setString(3, pharmacie.getTelephone());
            stmt.setString(4, pharmacie.getEmail());
            stmt.setBoolean(5, pharmacie.isEstDeGarde());
            stmt.setBoolean(6, pharmacie.isActive());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    pharmacie.setIdPharmacie(rs.getLong(1));
                }
            }
            
            return pharmacie;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la pharmacie", e);
        }
    }

    @Override
    public Pharmacie update(Pharmacie pharmacie) {
        String sql = "UPDATE pharmacie SET nom = ?, adresse = ?, telephone = ?, email = ?, est_de_garde = ?, active = ?, date_modification = CURRENT_TIMESTAMP WHERE id_pharmacie = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, pharmacie.getNom());
            stmt.setString(2, pharmacie.getAdresse());
            stmt.setString(3, pharmacie.getTelephone());
            stmt.setString(4, pharmacie.getEmail());
            stmt.setBoolean(5, pharmacie.isEstDeGarde());
            stmt.setBoolean(6, pharmacie.isActive());
            stmt.setLong(7, pharmacie.getIdPharmacie());
            
            stmt.executeUpdate();
            return pharmacie;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la pharmacie", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "UPDATE pharmacie SET active = false WHERE id_pharmacie = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la pharmacie", e);
        }
    }

    @Override
    public Optional<Pharmacie> findById(Long id) {
        String sql = "SELECT * FROM pharmacie WHERE id_pharmacie = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la pharmacie", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<Pharmacie> findAll() {
        List<Pharmacie> pharmacies = new ArrayList<>();
        String sql = "SELECT * FROM pharmacie ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pharmacies.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des pharmacies", e);
        }
        
        return pharmacies;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM pharmacie";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des pharmacies", e);
        }
        
        return 0;
    }

    @Override
    public boolean exists(Long id) {
        String sql = "SELECT COUNT(*) FROM pharmacie WHERE id_pharmacie = ?";
        
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
    public List<Pharmacie> findActives() {
        List<Pharmacie> pharmacies = new ArrayList<>();
        String sql = "SELECT * FROM pharmacie WHERE active = true ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pharmacies.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des pharmacies actives", e);
        }
        
        return pharmacies;
    }

    @Override
    public List<Pharmacie> findDeGarde() {
        List<Pharmacie> pharmacies = new ArrayList<>();
        String sql = "SELECT * FROM pharmacie WHERE est_de_garde = true AND active = true ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                pharmacies.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des pharmacies de garde", e);
        }
        
        return pharmacies;
    }

    @Override
    public List<Pharmacie> findByNom(String nom) {
        List<Pharmacie> pharmacies = new ArrayList<>();
        String sql = "SELECT * FROM pharmacie WHERE nom LIKE ? ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nom + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    pharmacies.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par nom", e);
        }
        
        return pharmacies;
    }

    @Override
    public boolean toggleActive(Long id) {
        String sql = "UPDATE pharmacie SET active = NOT active WHERE id_pharmacie = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du changement de statut", e);
        }
    }

    @Override
    public boolean toggleGarde(Long id) {
        String sql = "UPDATE pharmacie SET est_de_garde = NOT est_de_garde WHERE id_pharmacie = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du changement de statut de garde", e);
        }
    }

    private Pharmacie mapResultSetToEntity(ResultSet rs) throws SQLException {
        Pharmacie pharmacie = new Pharmacie();
        pharmacie.setIdPharmacie(rs.getLong("id_pharmacie"));
        pharmacie.setNom(rs.getString("nom"));
        pharmacie.setAdresse(rs.getString("adresse"));
        pharmacie.setTelephone(rs.getString("telephone"));
        pharmacie.setEmail(rs.getString("email"));
        pharmacie.setEstDeGarde(rs.getBoolean("est_de_garde"));
        pharmacie.setActive(rs.getBoolean("active"));
        pharmacie.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        
        Timestamp dateModif = rs.getTimestamp("date_modification");
        if (dateModif != null) {
            pharmacie.setDateModification(dateModif.toLocalDateTime());
        }
        
        return pharmacie;
    }

}
