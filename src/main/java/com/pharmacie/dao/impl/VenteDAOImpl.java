package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.VenteDAO;
import com.pharmacie.model.Vente;
import com.pharmacie.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class VenteDAOImpl implements VenteDAO {
	@Override
    public Vente create(Vente vente) {
        String sql = "INSERT INTO vente (numero_ticket, date_vente, montant_total, id_employe, id_pharmacie, mode_paiement, statut) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, vente.getNumeroTicket());
            stmt.setTimestamp(2, Timestamp.valueOf(vente.getDateVente()));
            stmt.setBigDecimal(3, vente.getMontantTotal());
            stmt.setLong(4, vente.getIdEmploye());
            stmt.setLong(5, vente.getIdPharmacie());
            stmt.setString(6, vente.getModePaiement());
            stmt.setString(7, vente.getStatut());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    vente.setIdVente(rs.getLong(1));
                }
            }
            
            return vente;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la vente", e);
        }
    }

    @Override
    public Vente update(Vente vente) {
        String sql = "UPDATE vente SET numero_ticket = ?, montant_total = ?, mode_paiement = ?, statut = ? WHERE id_vente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, vente.getNumeroTicket());
            stmt.setBigDecimal(2, vente.getMontantTotal());
            stmt.setString(3, vente.getModePaiement());
            stmt.setString(4, vente.getStatut());
            stmt.setLong(5, vente.getIdVente());
            
            stmt.executeUpdate();
            return vente;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la vente", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "UPDATE vente SET statut = 'ANNULEE' WHERE id_vente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la vente", e);
        }
    }

    @Override
    public Optional<Vente> findById(Long id) {
        String sql = "SELECT * FROM vente WHERE id_vente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la vente", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<Vente> findAll() {
        List<Vente> ventes = new ArrayList<>();
        String sql = "SELECT * FROM vente ORDER BY date_vente DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                ventes.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ventes", e);
        }
        
        return ventes;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM vente WHERE statut != 'ANNULEE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des ventes", e);
        }
        
        return 0;
    }

    @Override
    public boolean exists(Long id) {
        String sql = "SELECT COUNT(*) FROM vente WHERE id_vente = ?";
        
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
    public List<Vente> findByPharmacie(Long idPharmacie) {
        List<Vente> ventes = new ArrayList<>();
        String sql = "SELECT * FROM vente WHERE id_pharmacie = ? ORDER BY date_vente DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ventes.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ventes", e);
        }
        
        return ventes;
    }

    @Override
    public List<Vente> findByEmploye(Long idEmploye) {
        List<Vente> ventes = new ArrayList<>();
        String sql = "SELECT * FROM vente WHERE id_employe = ? ORDER BY date_vente DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idEmploye);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ventes.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ventes", e);
        }
        
        return ventes;
    }

    @Override
    public List<Vente> findByPeriode(Long idPharmacie, LocalDateTime debut, LocalDateTime fin) {
        List<Vente> ventes = new ArrayList<>();
        String sql = "SELECT * FROM vente WHERE id_pharmacie = ? AND date_vente BETWEEN ? AND ? ORDER BY date_vente DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            stmt.setTimestamp(2, Timestamp.valueOf(debut));
            stmt.setTimestamp(3, Timestamp.valueOf(fin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ventes.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ventes par période", e);
        }
        
        return ventes;
    }

    @Override
    public List<Vente> findVentesDuJour(Long idPharmacie) {
        List<Vente> ventes = new ArrayList<>();
        String sql = "SELECT * FROM vente WHERE id_pharmacie = ? AND DATE(date_vente) = CURDATE() ORDER BY date_vente DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ventes.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des ventes du jour", e);
        }
        
        return ventes;
    }

    @Override
    public Vente findByNumeroTicket(String numeroTicket) {
        String sql = "SELECT * FROM vente WHERE numero_ticket = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, numeroTicket);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par numéro de ticket", e);
        }
        
        return null;
    }

    @Override
    public double getChiffreAffaires(Long idPharmacie, LocalDateTime debut, LocalDateTime fin) {
        String sql = "SELECT COALESCE(SUM(montant_total), 0) FROM vente WHERE id_pharmacie = ? AND date_vente BETWEEN ? AND ? AND statut = 'VALIDEE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            stmt.setTimestamp(2, Timestamp.valueOf(debut));
            stmt.setTimestamp(3, Timestamp.valueOf(fin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du calcul du chiffre d'affaires", e);
        }
        
        return 0.0;
    }

    @Override
    public long countByPeriode(Long idPharmacie, LocalDateTime debut, LocalDateTime fin) {
        String sql = "SELECT COUNT(*) FROM vente WHERE id_pharmacie = ? AND date_vente BETWEEN ? AND ? AND statut = 'VALIDEE'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            stmt.setTimestamp(2, Timestamp.valueOf(debut));
            stmt.setTimestamp(3, Timestamp.valueOf(fin));
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des ventes", e);
        }
        
        return 0;
    }

    @Override
    public List<Object[]> getStatistiquesParEmploye(Long idPharmacie, LocalDate debut, LocalDate fin) {
        List<Object[]> stats = new ArrayList<>();
        String sql = "SELECT e.id_employe, CONCAT(e.prenom, ' ', e.nom) as nom_employe, " +
                     "COUNT(v.id_vente) as nb_ventes, COALESCE(SUM(v.montant_total), 0) as total_ventes " +
                     "FROM employe e " +
                     "LEFT JOIN vente v ON e.id_employe = v.id_employe AND DATE(v.date_vente) BETWEEN ? AND ? " +
                     "WHERE e.id_pharmacie = ? AND e.actif = true " +
                     "GROUP BY e.id_employe, e.prenom, e.nom " +
                     "ORDER BY total_ventes DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDate(1, Date.valueOf(debut));
            stmt.setDate(2, Date.valueOf(fin));
            stmt.setLong(3, idPharmacie);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] stat = new Object[4];
                    stat[0] = rs.getLong("id_employe");
                    stat[1] = rs.getString("nom_employe");
                    stat[2] = rs.getLong("nb_ventes");
                    stat[3] = rs.getDouble("total_ventes");
                    stats.add(stat);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des statistiques", e);
        }
        
        return stats;
    }

    private Vente mapResultSetToEntity(ResultSet rs) throws SQLException {
        Vente vente = new Vente();
        vente.setIdVente(rs.getLong("id_vente"));
        vente.setNumeroTicket(rs.getString("numero_ticket"));
        vente.setDateVente(rs.getTimestamp("date_vente").toLocalDateTime());
        vente.setMontantTotal(rs.getBigDecimal("montant_total"));
        vente.setIdEmploye(rs.getLong("id_employe"));
        vente.setIdPharmacie(rs.getLong("id_pharmacie"));
        vente.setModePaiement(rs.getString("mode_paiement"));
        vente.setStatut(rs.getString("statut"));
        
        return vente;
    }

}
