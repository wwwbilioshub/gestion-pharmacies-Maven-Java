package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.ProduitDAO;
import com.pharmacie.model.Produit;
import com.pharmacie.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProduitDAOImpl implements ProduitDAO{
	@Override
    public Produit create(Produit produit) {
        String sql = "INSERT INTO produit (nom, description, categorie, prix_unitaire, quantite_stock, seuil_minimal, date_expiration, code_barre, reference, id_pharmacie, actif) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, produit.getNom());
            stmt.setString(2, produit.getDescription());
            stmt.setString(3, produit.getCategorie());
            stmt.setBigDecimal(4, produit.getPrixUnitaire());
            stmt.setInt(5, produit.getQuantiteStock());
            stmt.setInt(6, produit.getSeuilMinimal());
            stmt.setDate(7, produit.getDateExpiration() != null ? Date.valueOf(produit.getDateExpiration()) : null);
            stmt.setString(8, produit.getCodeBarre());
            stmt.setString(9, produit.getReference());
            stmt.setLong(10, produit.getIdPharmacie());
            stmt.setBoolean(11, produit.isActif());
            
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produit.setIdProduit(rs.getLong(1));
                }
            }
            
            return produit;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création du produit", e);
        }
    }

    @Override
    public Produit update(Produit produit) {
        String sql = "UPDATE produit SET nom = ?, description = ?, categorie = ?, prix_unitaire = ?, quantite_stock = ?, seuil_minimal = ?, date_expiration = ?, code_barre = ?, reference = ?, actif = ? WHERE id_produit = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, produit.getNom());
            stmt.setString(2, produit.getDescription());
            stmt.setString(3, produit.getCategorie());
            stmt.setBigDecimal(4, produit.getPrixUnitaire());
            stmt.setInt(5, produit.getQuantiteStock());
            stmt.setInt(6, produit.getSeuilMinimal());
            stmt.setDate(7, produit.getDateExpiration() != null ? Date.valueOf(produit.getDateExpiration()) : null);
            stmt.setString(8, produit.getCodeBarre());
            stmt.setString(9, produit.getReference());
            stmt.setBoolean(10, produit.isActif());
            stmt.setLong(11, produit.getIdProduit());
            
            stmt.executeUpdate();
            return produit;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du produit", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "UPDATE produit SET actif = false WHERE id_produit = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression du produit", e);
        }
    }

    @Override
    public Optional<Produit> findById(Long id) {
        String sql = "SELECT * FROM produit WHERE id_produit = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche du produit", e);
        }
        
        return Optional.empty();
    }

    @Override
    public List<Produit> findAll() {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit WHERE actif = true ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                produits.add(mapResultSetToEntity(rs));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des produits", e);
        }
        
        return produits;
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM produit WHERE actif = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getLong(1);
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors du comptage des produits", e);
        }
        
        return 0;
    }

    @Override
    public boolean exists(Long id) {
        String sql = "SELECT COUNT(*) FROM produit WHERE id_produit = ?";
        
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
    public List<Produit> findByPharmacie(Long idPharmacie) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit WHERE id_pharmacie = ? AND actif = true ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produits.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des produits", e);
        }
        
        return produits;
    }

    @Override
    public List<Produit> findByCategorie(String categorie) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit WHERE categorie = ? AND actif = true ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categorie);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produits.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération par catégorie", e);
        }
        
        return produits;
    }

    @Override
    public List<Produit> findEnRuptureStock(Long idPharmacie) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit WHERE id_pharmacie = ? AND quantite_stock <= seuil_minimal AND actif = true ORDER BY quantite_stock";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produits.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des produits en rupture", e);
        }
        
        return produits;
    }

    @Override
    public List<Produit> findPerimesOuProches(Long idPharmacie, int joursAvant) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit WHERE id_pharmacie = ? AND date_expiration <= DATE_ADD(CURDATE(), INTERVAL ? DAY) AND actif = true ORDER BY date_expiration";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            stmt.setInt(2, joursAvant);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produits.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des produits périmés", e);
        }
        
        return produits;
    }

    @Override
    public List<Produit> searchByNom(String nom, Long idPharmacie) {
        List<Produit> produits = new ArrayList<>();
        String sql = "SELECT * FROM produit WHERE id_pharmacie = ? AND nom LIKE ? AND actif = true ORDER BY nom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, idPharmacie);
            stmt.setString(2, "%" + nom + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    produits.add(mapResultSetToEntity(rs));
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par nom", e);
        }
        
        return produits;
    }

    @Override
    public Produit findByCodeBarre(String codeBarre) {
        String sql = "SELECT * FROM produit WHERE code_barre = ? AND actif = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, codeBarre);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par code-barres", e);
        }
        
        return null;
    }

    @Override
    public Produit findByReference(String reference) {
        String sql = "SELECT * FROM produit WHERE reference = ? AND actif = true";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, reference);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par référence", e);
        }
        
        return null;
    }

    @Override
    public boolean updateStock(Long idProduit, int nouvelleQuantite) {
        String sql = "UPDATE produit SET quantite_stock = ? WHERE id_produit = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, nouvelleQuantite);
            stmt.setLong(2, idProduit);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du stock", e);
        }
    }

    @Override
    public List<String> findAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT DISTINCT categorie FROM produit WHERE actif = true ORDER BY categorie";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                categories.add(rs.getString("categorie"));
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des catégories", e);
        }
        
        return categories;
    }

    private Produit mapResultSetToEntity(ResultSet rs) throws SQLException {
        Produit produit = new Produit();
        produit.setIdProduit(rs.getLong("id_produit"));
        produit.setNom(rs.getString("nom"));
        produit.setDescription(rs.getString("description"));
        produit.setCategorie(rs.getString("categorie"));
        produit.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
        produit.setQuantiteStock(rs.getInt("quantite_stock"));
        produit.setSeuilMinimal(rs.getInt("seuil_minimal"));
        
        Date dateExp = rs.getDate("date_expiration");
        if (dateExp != null) {
            produit.setDateExpiration(dateExp.toLocalDate());
        }
        
        produit.setCodeBarre(rs.getString("code_barre"));
        produit.setReference(rs.getString("reference"));
        produit.setIdPharmacie(rs.getLong("id_pharmacie"));
        produit.setActif(rs.getBoolean("actif"));
        produit.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
        
        Timestamp dateModif = rs.getTimestamp("date_modification");
        if (dateModif != null) {
            produit.setDateModification(dateModif.toLocalDateTime());
        }
        
        return produit;
    }

}
