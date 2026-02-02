package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.EquipeGardeDAO;
import com.pharmacie.model.EquipeGarde;
import com.pharmacie.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class EquipeGardeDAOImpl implements EquipeGardeDAO {
	 @Override
	    public EquipeGarde create(EquipeGarde equipeGarde) {
	        String sql = "INSERT INTO equipe_garde (id_garde, id_employe, heure_arrivee, heure_depart, present) VALUES (?, ?, ?, ?, ?)";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	            
	            stmt.setLong(1, equipeGarde.getIdGarde());
	            stmt.setLong(2, equipeGarde.getIdEmploye());
	            stmt.setTime(3, equipeGarde.getHeureArrivee() != null ? Time.valueOf(equipeGarde.getHeureArrivee()) : null);
	            stmt.setTime(4, equipeGarde.getHeureDepart() != null ? Time.valueOf(equipeGarde.getHeureDepart()) : null);
	            stmt.setBoolean(5, equipeGarde.isPresent());
	            
	            stmt.executeUpdate();
	            
	            try (ResultSet rs = stmt.getGeneratedKeys()) {
	                if (rs.next()) {
	                    equipeGarde.setIdEquipeGarde(rs.getLong(1));
	                }
	            }
	            
	            return equipeGarde;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la création du membre de l'équipe de garde", e);
	        }
	    }

	    @Override
	    public EquipeGarde update(EquipeGarde equipeGarde) {
	        String sql = "UPDATE equipe_garde SET heure_arrivee = ?, heure_depart = ?, present = ? WHERE id_equipe_garde = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setTime(1, equipeGarde.getHeureArrivee() != null ? Time.valueOf(equipeGarde.getHeureArrivee()) : null);
	            stmt.setTime(2, equipeGarde.getHeureDepart() != null ? Time.valueOf(equipeGarde.getHeureDepart()) : null);
	            stmt.setBoolean(3, equipeGarde.isPresent());
	            stmt.setLong(4, equipeGarde.getIdEquipeGarde());
	            
	            stmt.executeUpdate();
	            return equipeGarde;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la mise à jour du membre de l'équipe de garde", e);
	        }
	    }

	    @Override
	    public boolean delete(Long id) {
	        String sql = "DELETE FROM equipe_garde WHERE id_equipe_garde = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, id);
	            return stmt.executeUpdate() > 0;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la suppression du membre de l'équipe de garde", e);
	        }
	    }

	    @Override
	    public Optional<EquipeGarde> findById(Long id) {
	        String sql = "SELECT eg.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM equipe_garde eg " +
	                     "LEFT JOIN employe e ON eg.id_employe = e.id_employe " +
	                     "WHERE eg.id_equipe_garde = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, id);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return Optional.of(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la recherche du membre de l'équipe de garde", e);
	        }
	        
	        return Optional.empty();
	    }

	    @Override
	    public List<EquipeGarde> findAll() {
	        List<EquipeGarde> equipes = new ArrayList<>();
	        String sql = "SELECT eg.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM equipe_garde eg " +
	                     "LEFT JOIN employe e ON eg.id_employe = e.id_employe";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            while (rs.next()) {
	                equipes.add(mapResultSetToEntity(rs));
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération des équipes de garde", e);
	        }
	        
	        return equipes;
	    }

	    @Override
	    public long count() {
	        String sql = "SELECT COUNT(*) FROM equipe_garde";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            if (rs.next()) {
	                return rs.getLong(1);
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors du comptage des équipes de garde", e);
	        }
	        
	        return 0;
	    }

	    @Override
	    public boolean exists(Long id) {
	        String sql = "SELECT COUNT(*) FROM equipe_garde WHERE id_equipe_garde = ?";
	        
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
	    public List<EquipeGarde> findByGarde(Long idGarde) {
	        List<EquipeGarde> equipes = new ArrayList<>();
	        String sql = "SELECT eg.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM equipe_garde eg " +
	                     "LEFT JOIN employe e ON eg.id_employe = e.id_employe " +
	                     "WHERE eg.id_garde = ? ORDER BY e.nom, e.prenom";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, idGarde);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    equipes.add(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération de l'équipe de garde", e);
	        }
	        
	        return equipes;
	    }

	    @Override
	    public List<EquipeGarde> findByEmploye(Long idEmploye) {
	        List<EquipeGarde> equipes = new ArrayList<>();
	        String sql = "SELECT eg.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM equipe_garde eg " +
	                     "LEFT JOIN employe e ON eg.id_employe = e.id_employe " +
	                     "INNER JOIN garde g ON eg.id_garde = g.id_garde " +
	                     "WHERE eg.id_employe = ? ORDER BY g.date_debut DESC";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, idEmploye);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    equipes.add(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération des gardes de l'employé", e);
	        }
	        
	        return equipes;
	    }

	    @Override
	    public boolean isAssigne(Long idGarde, Long idEmploye) {
	        String sql = "SELECT COUNT(*) FROM equipe_garde WHERE id_garde = ? AND id_employe = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, idGarde);
	            stmt.setLong(2, idEmploye);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return rs.getInt(1) > 0;
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la vérification de l'assignation", e);
	        }
	        
	        return false;
	    }

	    @Override
	    public boolean deleteByGarde(Long idGarde) {
	        String sql = "DELETE FROM equipe_garde WHERE id_garde = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, idGarde);
	            return stmt.executeUpdate() > 0;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la suppression de l'équipe de garde", e);
	        }
	    }

	    @Override
	    public boolean enregistrerPresence(Long id, boolean present) {
	        String sql = "UPDATE equipe_garde SET present = ?, heure_arrivee = ? WHERE id_equipe_garde = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setBoolean(1, present);
	            if (present) {
	                stmt.setTime(2, Time.valueOf(java.time.LocalTime.now()));
	            } else {
	                stmt.setNull(2, Types.TIME);
	            }
	            stmt.setLong(3, id);
	            
	            return stmt.executeUpdate() > 0;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de l'enregistrement de la présence", e);
	        }
	    }

	    private EquipeGarde mapResultSetToEntity(ResultSet rs) throws SQLException {
	        EquipeGarde equipe = new EquipeGarde();
	        equipe.setIdEquipeGarde(rs.getLong("id_equipe_garde"));
	        equipe.setIdGarde(rs.getLong("id_garde"));
	        equipe.setIdEmploye(rs.getLong("id_employe"));
	        
	        Time heureArrivee = rs.getTime("heure_arrivee");
	        if (heureArrivee != null) {
	            equipe.setHeureArrivee(heureArrivee.toLocalTime());
	        }
	        
	        Time heureDepart = rs.getTime("heure_depart");
	        if (heureDepart != null) {
	            equipe.setHeureDepart(heureDepart.toLocalTime());
	        }
	        
	        equipe.setPresent(rs.getBoolean("present"));
	        
	        return equipe;
	    }

}
