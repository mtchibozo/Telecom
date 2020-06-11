package tableEntities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Matiere extends DatabaseEntry {

	private String nom;
	private String niveau;
	private int idMatiereMere;
	
	
	public Matiere(int id) throws SQLException {
		super("MATIERES", id);
		nom=data.getString(2);
		niveau=data.getString(3);
		idMatiereMere=data.getInt(4);		
		// TODO Auto-generated constructor stub
	}
	
	private Matiere(
			String nom,
			String niveau,
			int idMatiereMere) throws SQLException {
		
		super("MATIERES");
		this.nom=nom;
		this.niveau=niveau;
		this.idMatiereMere=idMatiereMere;
		
	}

	
	public static final Matiere add(
			String nom,
			String niveau,
			int idMatiereMere) throws SQLException {
		
		Matiere mat = new Matiere(nom, niveau, idMatiereMere);
		int id = mat.newId();
		mat.setId(id);
		Connection conn = dbc.getConn();
		String query =  "INSERT INTO MATIERES (id, nom, niveau, idMatiereMere) VALUES(?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1,id);
		pstmt.setString(2,nom);
		pstmt.setString(3, niveau);
		pstmt.setInt(4,idMatiereMere);
		pstmt.executeUpdate();
		
		return mat;
	}
	
	//Getters
	


	public String getNom() {
		return nom;
	}


	public String getNiveau() {
		return niveau;
	}


	public int getIdMatiereMere() {
		return idMatiereMere;
	}

	public Matiere getMatiereMere() throws SQLException {
		return new Matiere(idMatiereMere);
	}

	public Connection getConn() {
		return conn;
	}
	
	
	
	
	
	
}
	
	
	

