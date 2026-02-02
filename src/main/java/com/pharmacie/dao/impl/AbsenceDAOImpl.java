package com.pharmacie.dao.impl;
import com.pharmacie.dao.interfaces.AbsenceDAO;

import com.pharmacie.model.Absence;
import com.pharmacie.util.DatabaseConnection;
import com.pharmacie.dao.interfaces.GenericDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class AbsenceDAOImpl implements AbsenceDAO {
	@Override
	public Absence create(Absence absence) {
		// Implémentation de la création d'une absence
		String sql = "INSERT INTO absence (id_employe, date_debut, date_fin, motif, justifiee, document_justificatif, approuvee) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setLong(1, absence.getIdEmploye());
            stmt.setDate(2, absence.getDateDebut() != null ? Date.valueOf(absence.getDateDebut()) : null);
            stmt.setDate(3, absence.getDateFin() != null ? Date.valueOf(absence.getDateFin()) : null);
            stmt.setString(4, absence.getMotif());
            stmt.setBoolean(5, absence.isJustifiee());
            stmt.setString(6, absence.getDocumentJustificatif());
            stmt.setBoolean(7, absence.isApprouvee());
          
            stmt.executeUpdate();
            
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    absence.setIdAbsence(rs.getLong(1));
                }
            }
            
            return absence;
            
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la création de l'absence", e);
        }
	}
	
	@Override
	public boolean delete(Long idAbsence) {
		// Implementation de la suppression d'une absence
		String sql = "DELETE FROM absence WHERE id_absence = ?";
		
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setLong(1, idAbsence);
			stmt.executeUpdate();
			return true;
			
		}catch(SQLException e) {
			throw new RuntimeException("Erreur lors de la suppression de l'absence", e);
		}
			
	}
	
	@Override
	public Absence update(Absence absence) {
		// Implementation de la mise a jour d'une absence
		
		String sql = "UPDATE absence SET id_employe = ?, date_debut = ?, date_fin = ?, motif = ?, justifiee = ?, document_justificatif = ?, approuvee = ? WHERE id_absence = ?";
		
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setLong(1, absence.getIdEmploye());
			stmt.setDate(2, absence.getDateDebut() != null ? Date.valueOf(absence.getDateDebut()) : null);
			stmt.setDate(3, absence.getDateFin() != null ? Date.valueOf(absence.getDateFin()) : null);
			stmt.setString(4, absence.getMotif());
			stmt.setBoolean(5, absence.isJustifiee());
			stmt.setString(6, absence.getDocumentJustificatif());
			stmt.setBoolean(7, absence.isApprouvee());
			stmt.setLong(8, absence.getIdAbsence());
			
			stmt.executeUpdate();
			return absence;
		}catch(SQLException e) {
			throw new RuntimeException("Erreur lors de la mise à jour de l'absence", e);
		}
	}
	
	@Override
	public List<Absence> findAll(){
		// Implementation de la recuperation de toutes les absences
		String sql = "SELECT * FROM absence";
		List<Absence> absences = new java.util.ArrayList<>();
		
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()){
			
			while(rs.next()) {
				Absence absence = mapResultSetToAbsence(rs);
				absences.add(absence);
			}
		}catch(SQLException e) {
			throw new RuntimeException("Erreur lors de la récupération des absences", e);
		}
		
		return absences;
	}
	
	@Override 
	public Optional<Absence> findById(Long idAbsence){
		// Implementation de la recuperation d'une absence par son id
		String sql = "SELECT * FROM absence WHERE id_absence = ?";
		
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setLong(1, idAbsence);
			try(ResultSet rs =  stmt.executeQuery()){
				if(rs.next()) {
					Absence absence = mapResultSetToAbsence(rs);
					return Optional.of(absence);
				}
			}
			
		}catch(SQLException e) {
			throw new RuntimeException("Erreur lors de la récupération de l'absence", e);
		}
		
		return Optional.empty();
	}
	
	@Override
	public long count() {
		// Implementation du comptage des absences
		String sql = "SELECT COUNT(*) AS total FROM absence";
		
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery()){
			
			if(rs.next()) {
				return rs.getLong("total");
			}
			
		}catch(SQLException e) {
			throw new RuntimeException("Erreur lors du comptage des absences", e);
		}
		
		return 0;
	}
	
	@Override 
	public boolean exists(Long idAbsence) {
		// Implementation de la verification de l'existence d'une absence par son id
		String sql = "SELECT 1 FROM absence WHERE id_absence = ?";
		
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setLong(1, idAbsence);
			try(ResultSet rs = stmt.executeQuery()){
				return rs.next();
			}
			
		}catch(SQLException e) {
			throw new RuntimeException("Erreur lors de la vérification de l'existence de l'absence", e);
		}
	}
	
	@Override 
	public List<Absence> findByEmploye(Long idEmploye){
		List<Absence> absences = new ArrayList<>();
		String sql = "SELECT * FROM absence WHERE id_employe = ?";
		
		try(Connection conn = DatabaseConnection.getConnection();
			PreparedStatement stmt = conn.prepareStatement(sql)){
			
			stmt.setLong(1, idEmploye);
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()) {
					absences.add(mapResultSetToAbsence(rs));
				}
			}
				
		}catch(SQLException e) {
			throw new RuntimeException("Erreur lors de la recherche des absences de l'employe", e);
		}
		return absences;
	}
	
	private Absence mapResultSetToAbsence(ResultSet rs) throws SQLException {
		Absence absence = new Absence();
		absence.setIdAbsence(rs.getLong("id_absence"));
		absence.setIdEmploye(rs.getLong("id_employe"));
		absence.setDateDebut(rs.getDate("date_debut") != null ? rs.getDate("date_debut").toLocalDate() : null);
		absence.setDateFin(rs.getDate("date_fin") != null ? rs.getDate("date_fin").toLocalDate() : null);
		absence.setMotif(rs.getString("motif"));
		absence.setJustifiee(rs.getBoolean("justifiee"));
		absence.setDocumentJustificatif(rs.getString("document_justificatif"));
		absence.setApprouvee(rs.getBoolean("approuvee"));
		return absence;
		
	}
	
}
