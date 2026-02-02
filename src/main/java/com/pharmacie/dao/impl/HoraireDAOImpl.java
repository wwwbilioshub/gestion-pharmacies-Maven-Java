package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.HoraireDAO;
import com.pharmacie.model.Horaire;
import com.pharmacie.enums.TypeHoraire;
import com.pharmacie.util.DatabaseConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HoraireDAOImpl implements HoraireDAO{
	@Override
	public Horaire create(Horaire horaire) {
	    String sql = "INSERT INTO horaire (id_employe, date, heure_debut, heure_fin, heures_travaillees, heures_supplementaires, type_horaire, present) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        
	        stmt.setLong(1, horaire.getIdEmploye());
	        stmt.setDate(2, Date.valueOf(horaire.getDate()));
	        stmt.setTime(3, Time.valueOf(horaire.getHeureDebut()));
	        stmt.setTime(4, horaire.getHeureFin() != null ? Time.valueOf(horaire.getHeureFin()) : null);
	        stmt.setDouble(5, horaire.getHeuresTravaillees());
	        stmt.setDouble(6, horaire.getHeuresSupplementaires());
	        stmt.setString(7, horaire.getTypeHoraire().name());
	        stmt.setBoolean(8, horaire.isPresent());
	        
	        stmt.executeUpdate();
	        
	        try (ResultSet rs = stmt.getGeneratedKeys()) {
	            if (rs.next()) {
	                horaire.setIdHoraire(rs.getLong(1));
	            }
	        }
	        
	        return horaire;
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la création de l'horaire", e);
	    }
	}

	@Override
	public Horaire update(Horaire horaire) {
	    String sql = "UPDATE horaire SET heure_debut = ?, heure_fin = ?, heures_travaillees = ?, heures_supplementaires = ?, type_horaire = ?, present = ? WHERE id_horaire = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setTime(1, Time.valueOf(horaire.getHeureDebut()));
	        stmt.setTime(2, horaire.getHeureFin() != null ? Time.valueOf(horaire.getHeureFin()) : null);
	        stmt.setDouble(3, horaire.getHeuresTravaillees());
	        stmt.setDouble(4, horaire.getHeuresSupplementaires());
	        stmt.setString(5, horaire.getTypeHoraire().name());
	        stmt.setBoolean(6, horaire.isPresent());
	        stmt.setLong(7, horaire.getIdHoraire());
	        
	        stmt.executeUpdate();
	        return horaire;
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la mise à jour de l'horaire", e);
	    }
	}

	@Override
	public boolean delete(Long id) {
	    String sql = "DELETE FROM horaire WHERE id_horaire = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, id);
	        return stmt.executeUpdate() > 0;
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la suppression de l'horaire", e);
	    }
	}

	@Override
	public Optional<Horaire> findById(Long id) {
	    String sql = "SELECT h.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM horaire h " +
	                 "LEFT JOIN employe e ON h.id_employe = e.id_employe " +
	                 "WHERE h.id_horaire = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, id);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return Optional.of(mapResultSetToEntity(rs));
	            }
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la recherche de l'horaire", e);
	    }
	    
	    return Optional.empty();
	}

	@Override
	public List<Horaire> findAll() {
	    List<Horaire> horaires = new ArrayList<>();
	    String sql = "SELECT h.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM horaire h " +
	                 "LEFT JOIN employe e ON h.id_employe = e.id_employe " +
	                 "ORDER BY h.date DESC";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        
	        while (rs.next()) {
	            horaires.add(mapResultSetToEntity(rs));
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la récupération des horaires", e);
	    }
	    
	    return horaires;
	}

	@Override
	public long count() {
	    String sql = "SELECT COUNT(*) FROM horaire";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        
	        if (rs.next()) {
	            return rs.getLong(1);
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors du comptage des horaires", e);
	    }
	    
	    return 0;
	}

	@Override
	public boolean exists(Long id) {
	    String sql = "SELECT COUNT(*) FROM horaire WHERE id_horaire = ?";
	    
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
	public List<Horaire> findByEmploye(Long idEmploye) {
	    List<Horaire> horaires = new ArrayList<>();
	    String sql = "SELECT h.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM horaire h " +
	                 "LEFT JOIN employe e ON h.id_employe = e.id_employe " +
	                 "WHERE h.id_employe = ? ORDER BY h.date DESC";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, idEmploye);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                horaires.add(mapResultSetToEntity(rs));
	            }
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la récupération des horaires", e);
	    }
	    
	    return horaires;
	}

	@Override
	public List<Horaire> findByPeriode(Long idEmploye, LocalDate debut, LocalDate fin) {
	    List<Horaire> horaires = new ArrayList<>();
	    String sql = "SELECT h.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM horaire h " +
	                 "LEFT JOIN employe e ON h.id_employe = e.id_employe " +
	                 "WHERE h.id_employe = ? AND h.date BETWEEN ? AND ? ORDER BY h.date";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, idEmploye);
	        stmt.setDate(2, Date.valueOf(debut));
	        stmt.setDate(3, Date.valueOf(fin));
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                horaires.add(mapResultSetToEntity(rs));
	            }
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la récupération des horaires par période", e);
	    }
	    
	    return horaires;
	}

	@Override
	public Horaire findByEmployeAndDate(Long idEmploye, LocalDate date, TypeHoraire type) {
	    String sql = "SELECT h.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM horaire h " +
	                 "LEFT JOIN employe e ON h.id_employe = e.id_employe " +
	                 "WHERE h.id_employe = ? AND h.date = ? AND h.type_horaire = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, idEmploye);
	        stmt.setDate(2, Date.valueOf(date));
	        stmt.setString(3, type.name());
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return mapResultSetToEntity(rs);
	            }
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la recherche de l'horaire", e);
	    }
	    
	    return null;
	}

	@Override
	public double getTotalHeuresTravaillees(Long idEmploye, LocalDate debut, LocalDate fin) {
	    String sql = "SELECT COALESCE(SUM(heures_travaillees), 0) FROM horaire WHERE id_employe = ? AND date BETWEEN ? AND ? AND present = true";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, idEmploye);
	        stmt.setDate(2, Date.valueOf(debut));
	        stmt.setDate(3, Date.valueOf(fin));
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getDouble(1);
	            }
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors du calcul des heures travaillées", e);
	    }
	    
	    return 0.0;
	}

	@Override
	public double getTotalHeuresSupplementaires(Long idEmploye, LocalDate debut, LocalDate fin) {
	    String sql = "SELECT COALESCE(SUM(heures_supplementaires), 0) FROM horaire WHERE id_employe = ? AND date BETWEEN ? AND ? AND present = true";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, idEmploye);
	        stmt.setDate(2, Date.valueOf(debut));
	        stmt.setDate(3, Date.valueOf(fin));
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getDouble(1);
	            }
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors du calcul des heures supplémentaires", e);
	    }
	    
	    return 0.0;
	}

	@Override
	public long countAbsences(Long idEmploye, LocalDate debut, LocalDate fin) {
	    String sql = "SELECT COUNT(*) FROM horaire WHERE id_employe = ? AND date BETWEEN ? AND ? AND present = false";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, idEmploye);
	        stmt.setDate(2, Date.valueOf(debut));
	        stmt.setDate(3, Date.valueOf(fin));
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getLong(1);
	            }
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors du comptage des absences", e);
	    }
	    
	    return 0;
	}

	private Horaire mapResultSetToEntity(ResultSet rs) throws SQLException {
	    Horaire horaire = new Horaire();
	    horaire.setIdHoraire(rs.getLong("id_horaire"));
	    horaire.setIdEmploye(rs.getLong("id_employe"));
	    horaire.setDate(rs.getDate("date").toLocalDate());
	    horaire.setHeureDebut(rs.getTime("heure_debut").toLocalTime());
	    
	    Time heureFin = rs.getTime("heure_fin");
	    if (heureFin != null) {
	        horaire.setHeureFin(heureFin.toLocalTime());
	    }
	    
	    horaire.setHeuresTravaillees(rs.getDouble("heures_travaillees"));
	    horaire.setHeuresSupplementaires(rs.getDouble("heures_supplementaires"));
	    horaire.setTypeHoraire(TypeHoraire.valueOf(rs.getString("type_horaire")));
	    horaire.setPresent(rs.getBoolean("present"));
	    horaire.setNomEmploye(rs.getString("nom_employe"));
	    
	    return horaire;
	}

}
