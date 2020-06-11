package bdd;


import java.sql.*;
import java.text.SimpleDateFormat;


public final class DatabaseConnection  {
	
	private static Connection conn = null;
	public static SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yy HH:mm");
	
	
	public static void open() throws Exception {
		
		
			String url = "jdbc:sqlite:data/TEECH.sqlite";
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection(url);
		
		
	}
	
	// Renvoie un identifiant non utiliser dans la table tbl, la table doit contenir un champs id
	
	
	
	
	/**Recupere l'identifiant de tous les utilisateurs*/
	public ResultSet getUsers() {
		try {
			Statement state = conn.createStatement();
			return state.executeQuery("SELECT identifiant FROM UTILISATEURS");					
		}
		
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// Ajout d'une entree dans la table APPARTENANCE_GROUPES
	public void addAppartenanceGroupe(int idUtilisateur, int idGroupe, String statut) throws SQLException {
		
		String query =  "INSERT INTO APPARTENANCE_GROUPE (idUtilisateur, idGroupe, statut) VALUES(?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, idUtilisateur);
		pstmt.setInt(2, idGroupe);
		pstmt.setString(3, statut);
		pstmt.executeUpdate();
		
	}
	
	
	
	
	
	
	
	public static void close() {
		// TODO Auto-generated method stub
		
		if (conn != null) {
			
			try {conn.close();}
			
			catch (SQLException e ){
			}
		
		}
		
	}


	public static Connection getConn() {
		return conn;
	}
	
	
// Fonctions d'affichage pour les tests 
	
	
	/**Affiche le resultat d'une requete de maniere brute sur la console*/
	public void printQueryResult(ResultSet rs) {
		
		try {
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();
			System.out.println("*****");
			
			
			if (rs.next()) { // si next() renvoie false il n'y pas de resultat
				
				do {
					for (int i = 1 ; i < numberOfColumns ; i++) {
						System.out.print(rs.getObject(i) + " - ");
					}
				
					System.out.print(rs.getObject(numberOfColumns) + " # ");
					System.out.println("");	
				}
		
				while(rs.next());		
			}
			
			else { System.out.println("Pas de resultat"); }
			
			System.out.println("*****");
		}
		
		catch(Exception e) {
			e.printStackTrace();
		}
		
	};
	
	


	
	
	
}
