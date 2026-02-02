package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.LigneVenteDAO;
import com.pharmacie.model.LigneVente;
import com.pharmacie.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LigneVenteDAOImpl implements LigneVenteDAO{
	@Override
    public LigneVente create(LigneVente ligneVente) {
        String sql = "INSERT INTO ligne_vente (id_vente, id_produit, quantite, prix_unitaire, sous_total) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, ligneVente.getIdVente());
            stmt.setLong(2, ligneVente.getIdProduit());
            stmt.setInt(3, ligneVente.getQuantite());
            stmt.setBigDecimal(4, ligneVente.getPrixUnitaire());
            stmt.setBigDecimal(5, ligneVente.getSousTotal());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ligneVente.setIdLigneVente(rs.getLong(1));
                }
            }
            
            return ligneVente;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de la ligne de vente", e);
        }
    }

    @Override
    public LigneVente update(LigneVente ligneVente) {
        String sql = "UPDATE ligne_vente SET quantite = ?, prix_unitaire = ?, sous_total = ? WHERE id_ligne_vente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, ligneVente.getQuantite());
            stmt.setBigDecimal(2, ligneVente.getPrixUnitaire());
            stmt.setBigDecimal(3, ligneVente.getSousTotal());
            stmt.setLong(4, ligneVente.getIdLigneVente());
            
            stmt.executeUpdate();
            return ligneVente;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la ligne de vente", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM ligne_vente WHERE id_ligne_vente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la ligne de vente", e);
        }
    }

    @Override
    public Optional<LigneVente> findById(Long id) {
        String sql = "SELECT lv.*, p.nom as nom_produit FROM ligne_vente lv " +
                     "LEFT JOIN produit p ON lv.id_produit = p.id_produit " +
                     "WHERE lv.id_ligne_vente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la ligne de vente", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<LigneVente> findAll() {
        List<LigneVente> lignes = new ArrayList<>();
        String sql = "SELECT lv.*, p.nom as nom_produit FROM ligne_vente lv " +
                     "LEFT JOIN produit p ON lv.id_produit = p.id_produit";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                lignes.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des lignes de vente", e);
        }
        
        return lignes;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM ligne_vente";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des lignes de vente", e);
        }
        
        return 0;
    }

    @Override
    public boolean exists(Long id) {
        String sql = "SELECT COUNT(*) FROM ligne_vente WHERE id_ligne_vente = ?";
        
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
    public List<LigneVente> findByVente(Long idVente) {
        List<LigneVente> lignes = new ArrayList<>();
        String sql = "SELECT lv.*, p.nom as nom_produit FROM ligne_vente lv " +
                     "LEFT JOIN produit p ON lv.id_produit = p.id_produit " +
                     "WHERE lv.id_vente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idVente);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lignes.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des lignes de vente", e);
        }
        
        return lignes;
    }

    @Override
    public boolean deleteByVente(Long idVente) {
        String sql = "DELETE FROM ligne_vente WHERE id_vente = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idVente);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression des lignes de vente", e);
        }
    }

    @Override
    public boolean createBatch(List<LigneVente> lignes) {
        String sql = "INSERT INTO ligne_vente (id_vente, id_produit, quantite, prix_unitaire, sous_total) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            conn.setAutoCommit(false);
            
            for (LigneVente ligne : lignes) {
                stmt.setLong(1, ligne.getIdVente());
                stmt.setLong(2, ligne.getIdProduit());
                stmt.setInt(3, ligne.getQuantite());
                stmt.setBigDecimal(4, ligne.getPrixUnitaire());
                stmt.setBigDecimal(5, ligne.getSousTotal());
                stmt.addBatch();
            }
            
            stmt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            
            return true;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création en lot des lignes de vente", e);
        }
    }

    @Override
    public List<Object[]> getProduitsLesPlusVendus(Long idPharmacie, int limite) {
        List<Object[]> produits = new ArrayList<>();
        String sql = "SELECT p.id_produit, p.nom, SUM(lv.quantite) as total_vendu, " +
                     "COUNT(DISTINCT lv.id_vente) as nb_ventes " +
                     "FROM ligne_vente lv " +
                     "INNER JOIN produit p ON lv.id_produit = p.id_produit " +
                     "INNER JOIN vente v ON lv.id_vente = v.id_vente " +
                     "WHERE v.id_pharmacie = ? AND v.statut = 'VALIDEE' " +
                     "GROUP BY p.id_produit, p.nom " +
                     "ORDER BY total_vendu DESC " +
                     "LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            stmt.setInt(2, limite);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] produit = new Object[4];
                    produit[0] = rs.getLong("id_produit");
                    produit[1] = rs.getString("nom");
                    produit[2] = rs.getInt("total_vendu");
                    produit[3] = rs.getInt("nb_ventes");
                    produits.add(produit);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des produits les plus vendus", e);
        }
        
        return produits;
    }

    private LigneVente mapResultSetToEntity(ResultSet rs) throws SQLException {
        LigneVente ligne = new LigneVente();
        ligne.setIdLigneVente(rs.getLong("id_ligne_vente"));
        ligne.setIdVente(rs.getLong("id_vente"));
        ligne.setIdProduit(rs.getLong("id_produit"));
        ligne.setQuantite(rs.getInt("quantite"));
        ligne.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
        ligne.setSousTotal(rs.getBigDecimal("sous_total"));
        ligne.setNomProduit(rs.getString("nom_produit"));
        
        return ligne;
    }
}
