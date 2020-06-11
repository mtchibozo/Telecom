package tableEntities;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Matiere extends DatabaseEntry {

	private String nom;
	private String niveau;
	private int idMatiereMere;
	
	
	public Matiere(int id) throws SQLException {
		super("MATIERES", id);
		nom=data.getString(2);
		niveau=data.getString(3);
		idMatiereMere=data.getInt(4);		
		
	}
	
	public Matiere(ResultSet data) throws SQLException {
		super("MATIERES", data);
		nom=data.getString(2);
		niveau=data.getString(3);
		idMatiereMere=data.getInt(4);		
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
<<<<<<< HEAD

	public String convertToMsg() {
		return id+sep+nom+sep+niveau+sep+idMatiereMere;
	}
=======
	
>>>>>>> origin/Jean-Module_integration
	
	public static final Matiere add(
			String nom,
			String niveau,
			int idMatiereMere) throws SQLException {
		
		Matiere mat = new Matiere(nom, niveau, idMatiereMere);
		int id = mat.newId();
		mat.setId(id);
		String query =  "INSERT INTO MATIERES (id, nom, niveau, idMatiereMere) VALUES(?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1,id);
		pstmt.setString(2,nom);
		pstmt.setString(3, niveau);
		pstmt.setInt(4,idMatiereMere);
		pstmt.executeUpdate();
		
		return mat;
	}
	
	/**Renvoie toutes les matieres contenus dans la base de donnee dans un array
	 * @return ArrayList<Matiere>
	 * @throws SQLException*/
	public static ArrayList<Matiere> getAll() throws SQLException {
		
		ArrayList<Matiere> array= new ArrayList<Matiere>();
		String query =
				"SELECT * FROM MATIERES";
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			array.add(new Matiere(rs));
		}
		
		return array;
		
	}
	
	public static String convertArrayToMsg(ArrayList<Matiere> array) {
		int n = array.size();
		String msg = array.get(0).convertToMsg();
		for (int i = 1 ; i<n ; i++) {
			msg += arraySep+array.get(i).convertToMsg() ;
		}
		return msg;
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

	public static Connection getConn() {
		return conn;
	}
	
	
	
	
	
	
}
	
	
	

