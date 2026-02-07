package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.UtilisateurDAO;

import com.pharmacie.model.Utilisateur;
import com.pharmacie.enums.RoleUtilisateur;
import com.pharmacie.util.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilisateurDAOImpl implements UtilisateurDAO{
	 @Override
	    public Utilisateur create(Utilisateur utilisateur) {
	        String sql = "INSERT INTO utilisateur (login, mot_de_passe, role, id_employe, actif) VALUES (?, ?, ?, ?, ?)";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
	            
	            stmt.setString(1, utilisateur.getLogin());
	            // Hasher le mot de passe avant de l'enregistrer
	            String hashedPassword = BCrypt.hashpw(utilisateur.getMotDePasse(), BCrypt.gensalt(12));
	            stmt.setString(2, hashedPassword);
	            stmt.setString(3, utilisateur.getRole().name());
	            
	            if (utilisateur.getIdEmploye() != null) {
	                stmt.setLong(4, utilisateur.getIdEmploye());
	            } else {
	                stmt.setNull(4, Types.BIGINT);
	            }
	            
	            stmt.setBoolean(5, utilisateur.isActif());
	            
	            stmt.executeUpdate();
	            
	            try (ResultSet rs = stmt.getGeneratedKeys()) {
	                if (rs.next()) {
	                    utilisateur.setIdUtilisateur(rs.getLong(1));
	                }
	            }
	            
	            return utilisateur;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la création de l'utilisateur", e);
	        }
	    }

	    @Override
	    public Utilisateur update(Utilisateur utilisateur) {
	        String sql = "UPDATE utilisateur SET login = ?, role = ?, id_employe = ?, actif = ? WHERE id_utilisateur = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setString(1, utilisateur.getLogin());
	            stmt.setString(2, utilisateur.getRole().name());
	            
	            if (utilisateur.getIdEmploye() != null) {
	                stmt.setLong(3, utilisateur.getIdEmploye());
	            } else {
	                stmt.setNull(3, Types.BIGINT);
	            }
	            
	            stmt.setBoolean(4, utilisateur.isActif());
	            stmt.setLong(5, utilisateur.getIdUtilisateur());
	            
	            stmt.executeUpdate();
	            return utilisateur;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la mise à jour de l'utilisateur", e);
	        }
	    }

	    @Override
	    public boolean delete(Long id) {
	        String sql = "UPDATE utilisateur SET actif = false WHERE id_utilisateur = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, id);
	            return stmt.executeUpdate() > 0;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la suppression de l'utilisateur", e);
	        }
	    }

	    @Override
	    public Optional<Utilisateur> findById(Long id) {
	        String sql = "SELECT * FROM utilisateur WHERE id_utilisateur = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, id);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return Optional.of(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la recherche de l'utilisateur", e);
	        }
	        
	        return Optional.empty();
	    }

	    @Override
	    public List<Utilisateur> findAll() {
	        List<Utilisateur> utilisateurs = new ArrayList<>();
	        String sql = "SELECT * FROM utilisateur ORDER BY login";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            while (rs.next()) {
	                utilisateurs.add(mapResultSetToEntity(rs));
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération des utilisateurs", e);
	        }
	        
	        return utilisateurs;
	    }

	    @Override
	    public long count() {
	        String sql = "SELECT COUNT(*) FROM utilisateur WHERE actif = true";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            if (rs.next()) {
	                return rs.getLong(1);
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors du comptage des utilisateurs", e);
	        }
	        
	        return 0;
	    }

	    @Override
	    public boolean exists(Long id) {
	        String sql = "SELECT COUNT(*) FROM utilisateur WHERE id_utilisateur = ?";
	        
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
	    public Optional<Utilisateur> findByLogin(String login) {
	        String sql = "SELECT * FROM utilisateur WHERE login = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setString(1, login);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return Optional.of(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la recherche par login", e);
	        }
	        
	        return Optional.empty();
	    }

	    @Override
	    public Optional<Utilisateur> authenticate(String login, String motDePasse) {
	        Optional<Utilisateur> userOpt = findByLogin(login);
	        
	        if (userOpt.isPresent() && BCrypt.checkpw(motDePasse,userOpt.get().getMotDePasse()) ){
	            Utilisateur user = userOpt.get();
	            if (user.isActif()) {
	                updateDerniereConnexion(user.getIdUtilisateur());
	                return Optional.of(user);
	            }
	        }
	        
	        return Optional.empty();
	    }

	    @Override
	    public List<Utilisateur> findActifs() {
	        List<Utilisateur> utilisateurs = new ArrayList<>();
	        String sql = "SELECT * FROM utilisateur WHERE actif = true ORDER BY login";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(sql)) {
	            
	            while (rs.next()) {
	                utilisateurs.add(mapResultSetToEntity(rs));
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération des utilisateurs actifs", e);
	        }
	        
	        return utilisateurs;
	    }

	    @Override
	    public List<Utilisateur> findByRole(RoleUtilisateur role) {
	        List<Utilisateur> utilisateurs = new ArrayList<>();
	        String sql = "SELECT * FROM utilisateur WHERE role = ? AND actif = true ORDER BY login";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setString(1, role.name());
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    utilisateurs.add(mapResultSetToEntity(rs));
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la récupération par rôle", e);
	        }
	        
	        return utilisateurs;
	    }

	    @Override
	    public boolean loginExists(String login) {
	        String sql = "SELECT COUNT(*) FROM utilisateur WHERE login = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setString(1, login);
	            
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return rs.getInt(1) > 0;
	                }
	            }
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la vérification du login", e);
	        }
	        
	        return false;
	    }

	    @Override
	    public boolean updatePassword(Long id, String nouveauMotDePasse) {
	        String sql = "UPDATE utilisateur SET mot_de_passe = ? WHERE id_utilisateur = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            String hashedPassword = BCrypt.hashpw(nouveauMotDePasse, BCrypt.gensalt());
	            stmt.setString(1, hashedPassword);
	            stmt.setLong(2, id);
	            
	            return stmt.executeUpdate() > 0;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la mise à jour du mot de passe", e);
	        }
	    }

	    @Override
	    public boolean updateDerniereConnexion(Long id) {
	        String sql = "UPDATE utilisateur SET derniere_connexion = CURRENT_TIMESTAMP WHERE id_utilisateur = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, id);
	            return stmt.executeUpdate() > 0;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors de la mise à jour de la dernière connexion", e);
	        }
	    }

	    @Override
	    public boolean toggleActif(Long id) {
	        String sql = "UPDATE utilisateur SET actif = NOT actif WHERE id_utilisateur = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setLong(1, id);
	            return stmt.executeUpdate() > 0;
	            
	        } catch (SQLException e) {
	            throw new RuntimeException("Erreur lors du changement de statut", e);
	        }
	    }
	    
	    private Utilisateur mapResultSetToEntity(ResultSet rs) throws SQLException {
	        Utilisateur utilisateur = new Utilisateur();
	        utilisateur.setIdUtilisateur(rs.getLong("id_utilisateur"));
	        utilisateur.setLogin(rs.getString("login"));
	        utilisateur.setMotDePasse(rs.getString("mot_de_passe"));
	        utilisateur.setRole(RoleUtilisateur.valueOf(rs.getString("role")));
	        
	        Long idEmploye = rs.getLong("id_employe");
	        if (!rs.wasNull()) {
	            utilisateur.setIdEmploye(idEmploye);
	        }
	        
	        utilisateur.setActif(rs.getBoolean("actif"));
	        
	        if(rs.getTimestamp("derniere_connexion") != null) {
	            utilisateur.setDerniereConnexion(rs.getTimestamp("derniere_connexion").toLocalDateTime());
	        }
	        
	        Timestamp dateCreation = rs.getTimestamp("date_creation");
	        return utilisateur;
	    }

}
