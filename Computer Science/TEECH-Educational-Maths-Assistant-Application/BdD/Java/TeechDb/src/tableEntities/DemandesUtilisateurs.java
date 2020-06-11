package tableEntities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bdd.DatabaseConnection;


public class DemandesUtilisateurs extends DatabaseEntry {
	
	private int idMatiere;
	private int typeTravail;
	private String date;
	
	public DemandesUtilisateurs(int id) throws SQLException {
		super("DEMANDES_UTILISATEURS",id);
		idMatiere=data.getInt(2);
		typeTravail=data.getInt(3);
		date=data.getString(4);	
		
	}
	
	public String convertToMsg() {
		return idMatiere+sep+typeTravail+sep+date;
	}
	
	public DemandesUtilisateurs(ResultSet data) throws SQLException {
		super("DEMANDES_UTILISATEURS",data);
		idMatiere=data.getInt(2);
		typeTravail=data.getInt(3);
		date=data.getString(4);	
		
	}
	
	private DemandesUtilisateurs(
					int idMatiere,
					int typeTravail,
					String date) throws SQLException {
		super("DEMANDES_UTILISATEURS");
		this.idMatiere=idMatiere;
		this.typeTravail=typeTravail;
		this.date=date;
	}

	public static final DemandesUtilisateurs add(
			int idMatiere,
			int typeTravail
			) throws SQLException {
		
		java.util.Date now = new java.util.Date();	
		String date = DatabaseConnection.SDF.format(now);
		DemandesUtilisateurs demu = new DemandesUtilisateurs(idMatiere, typeTravail, date);
		int id = demu.newId();
		demu.setId(id);
		String query =  "INSERT INTO DEMANDES_UTILISATEURS (id, idMatiere, typeTravail, date) VALUES(?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1,id);
		pstmt.setInt(2,idMatiere);
		pstmt.setInt(3, typeTravail);
		pstmt.setString(4,date);
		pstmt.executeUpdate();
		
		return demu;
	}

	public int getIdMatiere() {
		return idMatiere;
	}

	public int getTypeTravail() {
		return typeTravail;
	}

	public String getDate() {
		return date;
	}

	
	
	
}
