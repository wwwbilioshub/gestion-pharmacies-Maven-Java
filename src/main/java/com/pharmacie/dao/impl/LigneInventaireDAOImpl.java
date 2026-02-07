package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.LigneInventaireDAO;
import com.pharmacie.model.LigneInventaire;
import com.pharmacie.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LigneInventaireDAOImpl implements LigneInventaireDAO {
	@Override
    public LigneInventaire create(LigneInventaire ligneInventaire) {
        String sql = "INSERT INTO ligne_inventaire (id_inventaire, id_produit, quantite_theorique, quantite_reelle) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, ligneInventaire.getIdInventaire());
            stmt.setLong(2, ligneInventaire.getIdProduit());
            stmt.setInt(3, ligneInventaire.getQuantiteTheorique());
            stmt.setInt(4, ligneInventaire.getQuantiteReelle());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ligneInventaire.setIdLigneInventaire(rs.getLong(1));
                }
            }
            
            return ligneInventaire;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la ligne d'inventaire", e);
        }
    }

    @Override
    public LigneInventaire update(LigneInventaire ligneInventaire) {
        String sql = "UPDATE ligne_inventaire SET quantite_theorique = ?, quantite_reelle = ? WHERE id_ligne_inventaire = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ligneInventaire.getQuantiteTheorique());
            stmt.setInt(2, ligneInventaire.getQuantiteReelle());
            stmt.setLong(3, ligneInventaire.getIdLigneInventaire());
            
            stmt.executeUpdate();
            return ligneInventaire;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la ligne d'inventaire", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM ligne_inventaire WHERE id_ligne_inventaire = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la ligne d'inventaire", e);
        }
    }

    @Override
    public Optional<LigneInventaire> findById(Long id) {
        String sql = "SELECT li.*, p.nom as nom_produit, p.categorie, p.reference " +
                     "FROM ligne_inventaire li " +
                     "LEFT JOIN produit p ON li.id_produit = p.id_produit " +
                     "WHERE li.id_ligne_inventaire = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la ligne d'inventaire", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<LigneInventaire> findAll() {
        List<LigneInventaire> lignes = new ArrayList<>();
        String sql = "SELECT li.*, p.nom as nom_produit, p.categorie, p.reference " +
                     "FROM ligne_inventaire li " +
                     "LEFT JOIN produit p ON li.id_produit = p.id_produit";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lignes.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des lignes d'inventaire", e);
        }
        
        return lignes;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM ligne_inventaire";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des lignes d'inventaire", e);
        }
        
        return 0;
    }

    @Override
    public boolean exists(Long id) {
        String sql = "SELECT COUNT(*) FROM ligne_inventaire WHERE id_ligne_inventaire = ?";
        
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
    public List<LigneInventaire> findByInventaire(Long idInventaire) {
		List<LigneInventaire> lignes = new ArrayList<>();
		String sql = "SELECT li.*, p.nom as nom_produit, p.categorie, p.reference " +
					 "FROM ligne_inventaire li " +
					 "LEFT JOIN produit p ON li.id_produit = p.id_produit " +
					 "WHERE li.id_inventaire = ?";
		
		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			
			stmt.setLong(1, idInventaire);
			
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					lignes.add(mapResultSetToEntity(rs));
				}
			}
			
		} catch (SQLException e) {
			throw new RuntimeException("Erreur lors de la récupération des lignes d'inventaire par ID d'inventaire", e);
		}
		
		return lignes;
	}
    
   
    private LigneInventaire mapResultSetToEntity(ResultSet rs) throws SQLException {
		LigneInventaire ligne = new LigneInventaire();
		ligne.setIdLigneInventaire(rs.getLong("id_ligne_inventaire"));
		ligne.setIdInventaire(rs.getLong("id_inventaire"));
		ligne.setIdProduit(rs.getLong("id_produit"));
		ligne.setQuantiteTheorique(rs.getInt("quantite_theorique"));
		ligne.setQuantiteReelle(rs.getInt("quantite_reelle"));
		ligne.setNomProduit(rs.getString("nom_produit"));

		return ligne;
	}

}
