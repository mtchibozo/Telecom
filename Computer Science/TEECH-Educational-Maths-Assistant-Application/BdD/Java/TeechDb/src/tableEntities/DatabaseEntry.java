package tableEntities;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import bdd.DatabaseConnection;
import server.TeechProtocol;

public abstract class DatabaseEntry {
	
	protected static Connection conn = DatabaseConnection.getConn(); 
	
	protected int id; //num�ro unique propre � chaque entree.
	protected final String table;
	protected ResultSet data = null; // les donnees contenu dans la ligne 
	protected static final String sep = TeechProtocol.sep;
	protected static final String arraySep = TeechProtocol.arraySep;
	
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
	
	/**Creer une un objet DataBaseEntry en l'initialisant avec les valeurs contenu a la ligne courante du ResultSet data*/
	public DatabaseEntry (String table, ResultSet data) throws SQLException {
		this.id = data.getInt(1);
		this.table = table;
		this.data = data;
	}
	
	public abstract String convertToMsg() throws IOException, SQLException;
	
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
	
	

	
	
	
}
