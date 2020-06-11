package tableEntities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import bdd.ImageFrame;

public class Utilisateur extends DatabaseEntry {

	private String identifiant;
	private String motDePasse;
	private String photoDeProfil;
	private String email;
	private String statut; 
	private String niveau; 
	private String description;
	private String etablissement;
	private String classe;
	private String ville;
	
	
	//Constructeurs si l'utilisateur instancie deja dans la base de donnee//
	
	
	public Utilisateur(int id) throws SQLException {
		super("UTILISATEURS",id);
		identifiant = data.getString(2);
		motDePasse = data.getString(3);
		photoDeProfil = data.getString(4);
		email = data.getString(5);
		statut = data.getString(6);
		niveau = data.getString(7);
		description = data.getString(8);
		etablissement = data.getString(9);
		classe = data.getString(10);
		ville = data.getString(11);
	}
	
	
	
	public Utilisateur(String identifiant) throws SQLException {
		
		super("UTILISATEURS");
		String query =  
				"SELECT * FROM UTILISATEURS WHERE identifiant = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, identifiant);
		ResultSet rs = pstmt.executeQuery();
		
		if (!rs.next()) throw new SQLException("no such user"); 
		id = rs.getInt(1);
		this.identifiant = identifiant;
		motDePasse = rs.getString(3);
		photoDeProfil = rs.getString(4);
		email = rs.getString(5);
		statut = rs.getString(6);
		niveau = rs.getString(7);
		description = rs.getString(8);
		etablissement = rs.getString(9);
		classe = rs.getString(10);
		ville = rs.getString(11);
	}
	
	//Constructeur si l'utilisateur instancie n'est pas la base de donnee, il y est alors ajoute//
	private Utilisateur (
			String identifiant, 
			String motDePasse,
			String photoDeProfil, 
			String email, 
			String statut, 
			String niveau, 
			String description,
			String etablissement,
			String classe, 
			String ville) throws SQLException {
		
		super("UTILISATEURS");
		this.identifiant = identifiant;
		this.motDePasse = motDePasse;
		this.photoDeProfil = photoDeProfil;
		this.email = email;
		this.statut = statut;
		this.niveau = niveau;
		this.description = description;
		this.etablissement = etablissement;
		this.classe = classe;
		this.ville = ville;
		
	}
	
	/** Ajoute une entree dans la base donnee et renvoie l'utilisateur correspondant
	 * @return Utilisateur*/
	public static Utilisateur add(
			String identifiant, 
			String motDePasse,
			String photoDeProfil, 
			String email, 
			String statut, 
			String niveau, 
			String description,
			String etablissement,
			String classe, 
			String ville) throws SQLException {
		
		Utilisateur user = new Utilisateur(identifiant, motDePasse, photoDeProfil, email, statut, niveau, description, etablissement, classe, ville);
		int id = user.newId();
		user.setId(id);
		Connection conn = dbc.getConn();
		String query =  "INSERT INTO UTILISATEURS (id, identifiant, motDePasse, photoDeProfil, email, statut, niveau, description, etablissement, classe, ville) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1,id);
		pstmt.setString(2, identifiant);
		pstmt.setString(3, motDePasse);
		pstmt.setString(4, photoDeProfil);
		pstmt.setString(5, email);
		pstmt.setString(6, statut);
		pstmt.setString(7, niveau);
		pstmt.setString(8, description);
		pstmt.setString(9, etablissement);
		pstmt.setString(10,classe);
		pstmt.setString(11,ville);
		pstmt.executeUpdate();
		
		return user;
	}	
	
	
	public int getAisance(Matiere mat) throws SQLException {
		Connection conn = dbc.getConn(); 
		String query =  
				"SELECT aisance FROM PROFILS WHERE idUtilisateur = ? AND idMatiere = ? AND date=MIN(date)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		pstmt.setInt(2, mat.getId());
		return pstmt.executeQuery().getInt(1);
	}
	
	
	//utile pour test
	public void show() {
		new ImageFrame(identifiant, photoDeProfil); 
	}

	
	public final static Utilisateur get(int id) throws SQLException {
		
		String query =  
				"SELECT idExercice FROM EXERCICES WHERE id = ?";
		PreparedStatement pstmt = dbc.getConn().prepareStatement(query);
		pstmt.setInt(1, id);
		
		if (pstmt.executeQuery().next()) {
			return new Utilisateur(id);
		} 
		
		else throw new SQLException("pas d'utilisateur avec l'id : " + id);
	}

	//Getters

	public String getIdentifiant() {
		return identifiant;
	}

	public String getMotDePasse() {
		return motDePasse;
	}

	public String getPhotoDeProfil() {
		return photoDeProfil;
	}

	public String getEmail() {
		return email;
	}

	public String getStatut() {
		return statut;
	}

	public String getNiveau() {
		return niveau;
	}

	public String getDescription() {
		return description;
	}

	public String getEtablissement() {
		return etablissement;
	}

	public String getClasse() {
		return classe;
	}

	public String getVille() {
		return ville;
	} 
	
	
	

}
