package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.PaieDAO;
import com.pharmacie.model.Paie;
import com.pharmacie.util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class PaieDAOImpl implements PaieDAO{
	@Override
	public Paie create(Paie paie) {
	    String sql = "INSERT INTO paie (id_employe, mois, annee, montant_base, heures_supplementaires, prime_garde, penalite, montant_total, payee) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	        
	        stmt.setLong(1, paie.getIdEmploye());
	        stmt.setInt(2, paie.getMois());
	        stmt.setInt(3, paie.getAnnee());
	        stmt.setBigDecimal(4, paie.getMontantBase());
	        stmt.setBigDecimal(5, paie.getHeuresSupplementaires());
	        stmt.setBigDecimal(6, paie.getPrimeGarde());
	        stmt.setBigDecimal(7, paie.getPenalite());
	        stmt.setBigDecimal(8, paie.getMontantTotal());
	        stmt.setBoolean(9, paie.isPayee());
	        
	        stmt.executeUpdate();
	        
	        try (ResultSet rs = stmt.getGeneratedKeys()) {
	            if (rs.next()) {
	                paie.setIdPaie(rs.getLong(1));
	            }
	        }
	        
	        return paie;
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la création de la paie", e);
	    }
	}

	@Override
	public Paie update(Paie paie) {
	    String sql = "UPDATE paie SET montant_base = ?, heures_supplementaires = ?, prime_garde = ?, penalite = ?, montant_total = ?, payee = ?, date_paiement = ? WHERE id_paie = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setBigDecimal(1, paie.getMontantBase());
	        stmt.setBigDecimal(2, paie.getHeuresSupplementaires());
	        stmt.setBigDecimal(3, paie.getPrimeGarde());
	        stmt.setBigDecimal(4, paie.getPenalite());
	        stmt.setBigDecimal(5, paie.getMontantTotal());
	        stmt.setBoolean(6, paie.isPayee());
	        stmt.setDate(7, paie.getDatePaiement() != null ? Date.valueOf(paie.getDatePaiement()) : null);
	        stmt.setLong(8, paie.getIdPaie());
	        
	        stmt.executeUpdate();
	        return paie;
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la mise à jour de la paie", e);
	    }
	}

	@Override
	public boolean delete(Long id) {
	    String sql = "DELETE FROM paie WHERE id_paie = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, id);
	        return stmt.executeUpdate() > 0;
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la suppression de la paie", e);
	    }
	}

	@Override
	public Optional<Paie> findById(Long id) {
	    String sql = "SELECT p.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM paie p " +
	                 "LEFT JOIN employe e ON p.id_employe = e.id_employe " +
	                 "WHERE p.id_paie = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, id);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return Optional.of(mapResultSetToEntity(rs));
	            }
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la recherche de la paie", e);
	    }
	    
	    return Optional.empty();
	}

	@Override
	public List<Paie> findAll() {
	    List<Paie> paies = new ArrayList<>();
	    String sql = "SELECT p.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM paie p " +
	                 "LEFT JOIN employe e ON p.id_employe = e.id_employe " +
	                 "ORDER BY p.annee DESC, p.mois DESC";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        
	        while (rs.next()) {
	            paies.add(mapResultSetToEntity(rs));
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la récupération des paies", e);
	    }
	    
	    return paies;
	}

	@Override
	public long count() {
	    String sql = "SELECT COUNT(*) FROM paie";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        
	        if (rs.next()) {
	            return rs.getLong(1);
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors du comptage des paies", e);
	    }
	    
	    return 0;
	}

	@Override
	public boolean exists(Long id) {
	    String sql = "SELECT COUNT(*) FROM paie WHERE id_paie = ?";
	    
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
	public List<Paie> findByEmploye(Long idEmploye) {
	    List<Paie> paies = new ArrayList<>();
	    String sql = "SELECT p.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM paie p " +
	                 "LEFT JOIN employe e ON p.id_employe = e.id_employe " +
	                 "WHERE p.id_employe = ? ORDER BY p.annee DESC, p.mois DESC";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, idEmploye);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                paies.add(mapResultSetToEntity(rs));
	            }
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la récupération des paies", e);
	    }
	    
	    return paies;
	}

	@Override
	public Optional<Paie> findByEmployeAndPeriode(Long idEmploye, int mois, int annee) {
	    String sql = "SELECT p.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM paie p " +
	                 "LEFT JOIN employe e ON p.id_employe = e.id_employe " +
	                 "WHERE p.id_employe = ? AND p.mois = ? AND p.annee = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, idEmploye);
	        stmt.setInt(2, mois);
	        stmt.setInt(3, annee);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return Optional.of(mapResultSetToEntity(rs));
	            }
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la recherche de la paie", e);
	    }
	    
	    return Optional.empty();
	}

	@Override
	public List<Paie> findByPeriode(int mois, int annee) {
	    List<Paie> paies = new ArrayList<>();
	    String sql = "SELECT p.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM paie p " +
	                 "LEFT JOIN employe e ON p.id_employe = e.id_employe " +
	                 "WHERE p.mois = ? AND p.annee = ? ORDER BY e.nom, e.prenom";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setInt(1, mois);
	        stmt.setInt(2, annee);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            while (rs.next()) {
	                paies.add(mapResultSetToEntity(rs));
	            }
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la récupération des paies par période", e);
	    }
	    
	    return paies;
	}

	@Override
	public List<Paie> findNonPayees() {
	    List<Paie> paies = new ArrayList<>();
	    String sql = "SELECT p.*, CONCAT(e.prenom, ' ', e.nom) as nom_employe FROM paie p " +
	                 "LEFT JOIN employe e ON p.id_employe = e.id_employe " +
	                 "WHERE p.payee = false ORDER BY p.annee, p.mois";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         Statement stmt = conn.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)) {
	        
	        while (rs.next()) {
	            paies.add(mapResultSetToEntity(rs));
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors de la récupération des paies non payées", e);
	    }
	    
	    return paies;
	}

	@Override
	public boolean marquerPayee(Long id) {
	    String sql = "UPDATE paie SET payee = true, date_paiement = CURDATE() WHERE id_paie = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setLong(1, id);
	        return stmt.executeUpdate() > 0;
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors du marquage de la paie", e);
	    }
	}

	@Override
	public double getTotalSalaires(int mois, int annee) {
	    String sql = "SELECT COALESCE(SUM(montant_total), 0) FROM paie WHERE mois = ? AND annee = ?";
	    
	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {
	        
	        stmt.setInt(1, mois);
	        stmt.setInt(2, annee);
	        
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getDouble(1);
	            }
	        }
	        
	    } catch (SQLException e) {
	        throw new RuntimeException("Erreur lors du calcul du total des salaires", e);
	    }
	    
	    return 0.0;
	}

	private Paie mapResultSetToEntity(ResultSet rs) throws SQLException {
	    Paie paie = new Paie();
	    paie.setIdPaie(rs.getLong("id_paie"));
	    paie.setIdEmploye(rs.getLong("id_employe"));
	    paie.setMois(rs.getInt("mois"));
	    paie.setAnnee(rs.getInt("annee"));
	    paie.setMontantBase(rs.getBigDecimal("montant_base"));
	    paie.setHeuresSupplementaires(rs.getBigDecimal("heures_supplementaires"));
	    paie.setPrimeGarde(rs.getBigDecimal("prime_garde"));
	    paie.setPenalite(rs.getBigDecimal("penalite"));
	    paie.setMontantTotal(rs.getBigDecimal("montant_total"));
	    paie.setDateGeneration(rs.getTimestamp("date_generation").toLocalDateTime());
	    paie.setPayee(rs.getBoolean("payee"));
	    
	    Date datePaiement = rs.getDate("date_paiement");
	    if (datePaiement != null) {
	        paie.setDatePaiement(datePaiement.toLocalDate());
	    }
	    
	    paie.setNomEmploye(rs.getString("nom_employe"));
	    
	    return paie;
	}

}
