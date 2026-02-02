package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.EmployeDAO;
import com.pharmacie.model.Employe;
import com.pharmacie.enums.PosteEmploye;
import com.pharmacie.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeDAOImpl implements EmployeDAO {
	 @Override
	    public Employe create(Employe employe) {
	        String sql = "INSERT INTO employe (nom, prenom, sexe, date_naissance, telephone, email, adresse, poste, salaire_base, taux_horaire, id_pharmacie, actif, date_embauche) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	            
	            stmt.setString(1, employe.getNom());
	            stmt.setString(2, employe.getPrenom());
	            stmt.setString(3, employe.getSexe());
	            stmt.setDate(4, Date.valueOf(employe.getDateNaissance()));
	            stmt.setString(5, employe.getTelephone());
	            stmt.setString(6, employe.getEmail());
	            stmt.setString(7, employe.getAdresse());
	            stmt.setString(8, employe.getPoste().name());
	            stmt.setBigDecimal(9, employe.getSalaireBase());
	            stmt.setBigDecimal(10, employe.getTauxHoraire());
	            stmt.setLong(11, employe.getIdPharmacie());
	            stmt.setBoolean(12, employe.isActif());
	            stmt.setDate(13, Date.valueOf(employe.getDateEmbauche()));
	            
	            stmt.executeUpdate();
	            
	            try (ResultSet rs = stmt.getGeneratedKeys()) {
	                if (rs.next()) {
	                    employe.setIdEmploye(rs.getLong(1));
	                }
	            }
	            
	            return employe;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la création de l'employé", e);
	        }
	    }

	    @Override
	    public Employe update(Employe employe) {
	        String sql = "UPDATE employe SET nom = ?, prenom = ?, sexe = ?, date_naissance = ?, telephone = ?, email = ?, adresse = ?, poste = ?, salaire_base = ?, taux_horaire = ?, actif = ? WHERE id_employe = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setString(1, employe.getNom());
	            stmt.setString(2, employe.getPrenom());
	            stmt.setString(3, employe.getSexe());
	            stmt.setDate(4, Date.valueOf(employe.getDateNaissance()));
	            stmt.setString(5, employe.getTelephone());
	            stmt.setString(6, employe.getEmail());
	            stmt.setString(7, employe.getAdresse());
	            stmt.setString(8, employe.getPoste().name());
	            stmt.setBigDecimal(9, employe.getSalaireBase());
	            stmt.setBigDecimal(10, employe.getTauxHoraire());
	            stmt.setBoolean(11, employe.isActif());
	            stmt.setLong(12, employe.getIdEmploye());
	            
	            stmt.executeUpdate();
	            return employe;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la mise à jour de l'employé", e);
	        }
	    }

	    @Override
	    public boolean delete(Long id) {
	        String sql = "UPDATE employe SET actif = false WHERE id_employe = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, id);
	            return stmt.executeUpdate() > 0;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la suppression de l'employé", e);
	        }
	    }

	    @Override
	    public Optional<Employe> findById(Long id) {
	        String sql = "SELECT * FROM employe WHERE id_employe = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, id);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return Optional.of(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la recherche de l'employé", e);
	        }
	        
	        return Optional.empty();
	    }

	    @Override
	    public List<Employe> findAll() {
	        List<Employe> employes = new ArrayList<>();
	        String sql = "SELECT * FROM employe ORDER BY nom, prenom";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            while (rs.next()) {
	                employes.add(mapResultSetToEntity(rs));
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération des employés", e);
	        }
	        
	        return employes;
	    }

	    @Override
	    public long count() {
	        String sql = "SELECT COUNT(*) FROM employe WHERE actif = true";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            if (rs.next()) {
	                return rs.getLong(1);
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors du comptage des employés", e);
	        }
	        
	        return 0;
	    }

	    @Override
	    public boolean exists(Long id) {
	        String sql = "SELECT COUNT(*) FROM employe WHERE id_employe = ?";
	        
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
	    public List<Employe> findByPharmacie(Long idPharmacie) {
	        List<Employe> employes = new ArrayList<>();
	        String sql = "SELECT * FROM employe WHERE id_pharmacie = ? ORDER BY nom, prenom";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, idPharmacie);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    employes.add(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération des employés", e);
	        }
	        
	        return employes;
	    }

	    @Override
	    public List<Employe> findActifs(Long idPharmacie) {
	        List<Employe> employes = new ArrayList<>();
	        String sql = "SELECT * FROM employe WHERE id_pharmacie = ? AND actif = true ORDER BY nom, prenom";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, idPharmacie);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    employes.add(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération des employés actifs", e);
	        }
	        
	        return employes;
	    }

	    @Override
	    public List<Employe> findByPoste(Long idPharmacie,PosteEmploye poste) {
	        List<Employe> employes = new ArrayList<>();
	        String sql = "SELECT * FROM employe WHERE id_pharmacie = ? AND poste = ? AND actif = true ORDER BY nom, prenom";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, idPharmacie);
	            stmt.setString(2, poste.name());
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    employes.add(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération par poste", e);
	        }
	        
	        return employes;
	    }

	    @Override
	    public List<Employe> searchByNom(String recherche, Long idPharmacie) {
	        List<Employe> employes = new ArrayList<>();
	        String sql = "SELECT * FROM employe WHERE id_pharmacie = ? AND (nom LIKE ? OR prenom LIKE ?) AND actif = true ORDER BY nom, prenom";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, idPharmacie);
	            stmt.setString(2, "%" + recherche + "%");
	            stmt.setString(3, "%" + recherche + "%");
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    employes.add(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la recherche", e);
	        }
	        
	        return employes;
	    }

	    @Override
	    public boolean toggleActif(Long id) {
	        String sql = "UPDATE employe SET actif = NOT actif WHERE id_employe = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, id);
	            return stmt.executeUpdate() > 0;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors du changement de statut", e);
	        }
	    }

	    @Override
	    public long countByPharmacie(Long idPharmacie) {
	        String sql = "SELECT COUNT(*) FROM employe WHERE id_pharmacie = ? AND actif = true";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, idPharmacie);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return rs.getLong(1);
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors du comptage", e);
	        }
	        
	        return 0;
	    }

	    private Employe mapResultSetToEntity(ResultSet rs) throws SQLException {
	        Employe employe = new Employe();
	        employe.setIdEmploye(rs.getLong("id_employe"));
	        employe.setNom(rs.getString("nom"));
	        employe.setPrenom(rs.getString("prenom"));
	        employe.setSexe(rs.getString("sexe"));
	        employe.setDateNaissance(rs.getDate("date_naissance").toLocalDate());
	        employe.setTelephone(rs.getString("telephone"));
	        employe.setEmail(rs.getString("email"));
	        employe.setAdresse(rs.getString("adresse"));
	        employe.setPoste(PosteEmploye.valueOf(rs.getString("poste")));
	        employe.setSalaireBase(rs.getBigDecimal("salaire_base"));
	        employe.setTauxHoraire(rs.getBigDecimal("taux_horaire"));
	        employe.setIdPharmacie(rs.getLong("id_pharmacie"));
	        employe.setActif(rs.getBoolean("actif"));
	        employe.setDateEmbauche(rs.getDate("date_embauche").toLocalDate());
	        employe.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
	        
	        return employe;
	    }
	

}
