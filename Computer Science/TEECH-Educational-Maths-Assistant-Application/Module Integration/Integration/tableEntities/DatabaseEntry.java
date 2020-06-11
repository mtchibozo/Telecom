package tableEntities;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bdd.DatabaseConnection;

public abstract class DatabaseEntry {
	
	protected static final  DatabaseConnection dbc = new DatabaseConnection();
	protected static final Connection conn = dbc.getConn(); 
	
	protected int id; //numéro unique propre à chaque entree.
	protected final String table;
	protected ResultSet data = null; // les donnees contenu dans la ligne 
	
	//Constructeurs
	
	/** Pour declarer une entree qui est deja presente dans la table
	 * @throws SQLException */
	public DatabaseEntry (String table,int id) throws SQLException {
		this.id = id;
		this.table = table;
		String query =  
			"SELECT * FROM "+ table +" WHERE id=? ;";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		data = pstmt.executeQuery();
		
		if (!data.next()) throw new SQLException("il n'existe pas d'entree d'id : " +id + " dans la table : " + table);
		
	}
	
	
	/** Pour declarer une entree qui n'est pas encore dans la table 
	 * @throws SQLException */
	protected DatabaseEntry(String table) throws SQLException {
		this.id = -1;
		
		this.table = table;
	}
	
	protected int newId() throws SQLException {
		String query =  
			"SELECT MAX(id) FROM "+ table;
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		return rs.getInt(1)+1;
		
	}
	
	/** Supprime l'entree de la table */
	public void delete() throws SQLException {
		
		String query = "DELETE FROM " + table + " WHERE id=? ;"; 
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
		
	}
	
	
	//Getters & Setters 
	
	public final int getId() {
		return id;
	}
	
	public final void setId(int id) {
		this.id = id;
	}
	
	
	public final String getTable() {
		return table;
	}
	
	/**Retourne l'information contenu dans le champs column pour l'entree d'identifiant id dans la bdd en effectuant une requete
	 * @return Object */
	protected final Object getData(String column) {
		try {
			Connection conn = dbc.getConn();
			String query =  
				"SELECT ? FROM "+ table +" WHERE id = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, column);
			pstmt.setInt(2, id);
			ResultSet rs = pstmt.executeQuery();
			return rs.getObject(1);
		}
		
		catch (Exception e) {	
			e.printStackTrace();
			return null;
		}
	}
	
	/**Retourne les informations contenu dans la ligne ResultSet*/
	protected final ResultSet getData() {
		return data;
	}

	public static DatabaseConnection getDatabaseConnection() {
		return dbc;
	}
	
}
