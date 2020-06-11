package tableEntities;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Algorithmie.IAinterface;
import bdd.DatabaseConnection;
import bdd.ImageFrame;
import server.TeechProtocol;

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
	
	
	public Utilisateur(ResultSet data) throws SQLException {
		super("UTILISATEURS", data);
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
	
	/**Convertit en message que le serveur peut envoyer
	 * @return String
	 * @throws IOException, SQLException*/
	public String convertToMsg() throws IOException, SQLException {
		String sep = TeechProtocol.sep;
		byte[] img = TeechProtocol.extractBytes(photoDeProfil);
		String photo = TeechProtocol.encodeImage(img);
		String msg = id+sep+identifiant+sep+"[CONFIDENTIAL]"+sep+photo+sep+email+sep+statut+sep+niveau+sep+description+sep+etablissement+sep+classe+sep+ville;	
		return msg;
	}
	
	
	/** Ajoute une entree dans la base donnee et renvoie l'utilisateur correspondant
	 * @return Utilisateur
	 * @throws IOException */
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
			String ville) throws SQLException, IOException {
		
		Utilisateur user = new Utilisateur(identifiant, motDePasse, photoDeProfil, email, statut, niveau, description, etablissement, classe, ville);
		int id = user.newId();
		user.setId(id);
		String query =  "INSERT INTO UTILISATEURS (id, identifiant, motDePasse, photoDeProfil, email, statut, niveau, description, etablissement, classe, ville) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1,id);
		pstmt.setString(2, identifiant);
		pstmt.setString(3, motDePasse);
		File outputfile;
		
		if (photoDeProfil.equals("default")) {
			byte[] img = TeechProtocol.decodeImage(photoDeProfil);
			BufferedImage bi = ImageIO.read(new ByteArrayInputStream(img));
			outputfile = new File("data/pictures/PdP_"+id+".png");
			ImageIO.write(bi,"png", outputfile);
			pstmt.setString(4, outputfile.getPath());
		}
		
		else {
			pstmt.setString(4,"data/pictures/PdP_default.png");
		}
		
				
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
	
	
	
	//Gestion du profil des eleves 
	
	
	public int getAisance(Matiere mat) throws SQLException {
		String query =  
				"SELECT aisance FROM PROFILS WHERE idUtilisateur = ? AND idMatiere = ? AND date = (SELECT MAX(date) FROM PROFILS WHERE idUtilisateur = ? AND idMatiere= ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		pstmt.setInt(2, mat.getId());
		pstmt.setInt(3, id);
		pstmt.setInt(4, mat.getId());
		return pstmt.executeQuery().getInt(1);
	}
	
	public void setAisance(Matiere mat, int aisance) throws SQLException {
		String query =  
				"INSERT INTO PROFILS (idUtilisateur, idMatiere, aisance, date)  VALUES(?,?,?,?) ";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		pstmt.setInt(2, mat.getId());
		pstmt.setInt(3, aisance);
		java.util.Date date = new java.util.Date();	
		pstmt.setString(4, DatabaseConnection.SDF.format(date));
		pstmt.executeUpdate();
		return ;
	}
	
	public ArrayList<Evaluation> getAllExercicesDone() throws SQLException {
		String query =  
				"SELECT * FROM EVALUATIONS WHERE idUtilisateur = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		ResultSet rs = pstmt.executeQuery();
		ArrayList<Evaluation> array = new ArrayList<Evaluation>();
		while(rs.next()) {
			array.add(new Evaluation(rs));
		}
		return array;
	} 
	
	/**renvoie true si l'utilisateur a deja fait l'exercice
	 * return boolean*/
	public boolean hasAlreadyDone(Exercice exo) throws SQLException {
		String query =  
				"SELECT * FROM EVALUATIONS WHERE idUtilisateur = ? and idExercice = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);
		pstmt.setInt(2, exo.getId());
		ResultSet rs = pstmt.executeQuery();
		return rs.next();
	} 
	
	
	public ArrayList<Exercice> findExercice(Matiere mat, int difficulte) throws SQLException {
		DemandesUtilisateurs.add(mat.getId(), difficulte);
		return IAinterface.findGoodExercices(this, mat, difficulte);
		
	}
	
	
	
	//utile pour test
	public void show() {
		new ImageFrame(identifiant, photoDeProfil); 
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
