package tableEntities;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import assocTables.matiereExercice;
import server.TeechProtocol;

public class Exercice extends DatabaseEntry {
	
	private String description;
	private int recommandation; //un entier compris entre 0 et 10 (par exemple) qui �value la pertinence de l'exercice.
	private String enonce; //  une cha�ne de caract�res qui contient le format sous lequel est stocke l'exercice : image ou latex.
	private int difficulteAuteur; //un entier compris entre 0 et 10 (par exemple) qui repr�sente la difficult� de l'exercice estim�e par son auteur.
	private int idAuteur; // l'identifiant de l'auteur de l'exercice.
	private int dureeConseillee; // un entier, le nombre de minutes n�cessaires � finir l'exercice selon son auteur.
	private final String type;
	

	public Exercice(int id) throws SQLException {
		super("EXERCICES", id);
		description = data.getString(2);
		recommandation = data.getInt(3);
		enonce = data.getString(4);
		difficulteAuteur = data.getInt(5);
		idAuteur = data.getInt(6);
		dureeConseillee = data.getInt(7);
		type = data.getString(8);
	}
	
	public Exercice(ResultSet data) throws SQLException {
		super("EXERCICES", data);
		description = data.getString(2);
		recommandation = data.getInt(3);
		enonce = data.getString(4);
		difficulteAuteur = data.getInt(5);
		idAuteur = data.getInt(6);
		dureeConseillee = data.getInt(7);
		type = data.getString(8);
	}
	
	
	private Exercice(String description, 
			int recommandation,
			String enonce,
			int difficulteAuteur,
			int idAuteur,
			int dureeConseillee,
			String type) throws SQLException {
		
		super("EXERCICES");
		this.recommandation = recommandation;
		this.enonce = enonce;
		this.difficulteAuteur = difficulteAuteur;
		this.idAuteur =idAuteur;
		this.dureeConseillee = dureeConseillee;
		this.type = type;
		
		
		
	}
	
	
	public String convertToMsg() throws IOException, SQLException {
		
		
		String sep = TeechProtocol.sep;
		String content;
		switch(type) {
		case "image" : 
			byte[] img = TeechProtocol.extractBytes(enonce);
			content = TeechProtocol.encodeImage(img);
			break;
		case "latex" : 
			content = TeechProtocol.extractText(enonce);
		default :
			throw new SQLException("Unknown type of file" +type +", exercice id : " + id);
		}
		String msg= id+sep+getDescription()+sep+getRecommandation()+sep+content
		+sep+getDifficulteAuteur()+sep+getIdAuteur()+sep+getDureeConseillee()+sep+getType();
		return msg;
	}
	
		
	public static Exercice add(String description, 
							int recommandation,
							String enonce, // contenu exercice photo encodee ou string latex
							int difficulteAuteur,
							int idAuteur,
							int dureeConseillee,
							String type) throws SQLException, IOException {
		
		Exercice exo = new Exercice(description, recommandation, enonce , difficulteAuteur, idAuteur, dureeConseillee, type) ;
		System.out.println("op1");
		int id = exo.newId();
		exo.setId(id);
		String query =  "INSERT INTO EXERCICES (id, description, recommandation, enonce, difficulteAuteur, idAuteur, dureeConseillee, type) "
				+ "VALUES(?,?,?,?,?,?,?,?)";
		
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1,id);
		pstmt.setString(2, description);
		pstmt.setInt(3, recommandation);
		
		File outputfile = null;
		switch(type){
			
			case "photo" :
				byte[] img = TeechProtocol.decodeImage(enonce);
				BufferedImage bi = ImageIO.read(new ByteArrayInputStream(img));
				outputfile = new File("data/exercices/exo_"+id+".png");
				ImageIO.write(bi,"png", outputfile);
				break;
				
			case "latex":		
				outputfile = new File("data/exercices/exo_"+id+".tex");
				PrintWriter pw = new PrintWriter(outputfile);
				System.out.println(enonce);
				pw.print(enonce);	
				pw.close();
				
		}
		
		System.out.println("op2");
		pstmt.setString(4, outputfile.getPath());
		pstmt.setInt(5, difficulteAuteur);
		pstmt.setInt(6, idAuteur);
		pstmt.setInt(7, dureeConseillee);
		pstmt.setString(8, type);
		pstmt.executeUpdate();
		exo.setEnonce(outputfile.getPath());
		return exo;
		
	}
	
	
	private void setEnonce(String enonce) {
		// TODO Auto-generated method stub
		this.enonce = enonce;
	}


	/**Associe une matiere a l'exercice
	 * @throws SQLException*/
	public final void addMatiere(int idMatiere, int proportion, int difficulte) throws SQLException {
		String query = "INSERT INTO MATIERE_EXERCICE (idMatiere, idExercice, proportion , difficulte) VALUES(?, ?, ?, ?)"; // Ajout d'une entree dans la table MATIERE_EXERCICE
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, idMatiere);
		pstmt.setInt(2, id);
		pstmt.setInt(3, proportion);
		pstmt.setInt(4, difficulte);
		pstmt.executeUpdate();
	}
	
	
	/**Renvoie un corrige de l'exercice
	 * @return Corrige
	 * @throws SQLExecption
	 * Si l'exercice n'a aucun corrige*/
	public Corrige findCorrige() throws SQLException {
		String query = "SELECT * FROM CORRIGES WHERE idExercice = ? ";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);	
		ResultSet rs = pstmt.executeQuery();
		return new Corrige(rs);
	}
	
	
	
	//Utiliser les getters sur les elements de l'arraylist en retour
	
	/** Renvoie dans un array les matieres associes a l'exercice avec leurs proportions et leurs difficultes
	 * @return ArrayList<matiereExercice>
	 * @throws SQLException*/
	public ArrayList<matiereExercice> getInfos() throws SQLException{
		String query = "SELECT * FROM MATIERE_EXERCICE WHERE idExercice = ? ";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, id);	
		ResultSet rs = pstmt.executeQuery();
		int i = 0;
		ArrayList<matiereExercice> infos = new ArrayList<matiereExercice>();
		
		while(rs.next()) {
					infos.add(i, new matiereExercice(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4)));	
					i++;
		}
		return(infos);		
	}
	
	public int[][] getExercices(int matiere, int difficulte) throws SQLException{
		String query = "SELECT idExercice, proportion FROM MATIERE_EXERCICE WHERE idMatiere = ? AND difficulte = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, matiere);	
		pstmt.setInt(1, difficulte);
		ResultSet rs = pstmt.executeQuery();
		int i = 0;
		int exercices[][] = new int[2][];
		
		while(rs.next()) {
					exercices[0][i] = rs.getInt(1); //Contient les id de l'exo	
					exercices[1][i] = rs.getInt(2); //Contient les niveau de difficulté auteur	
					i++;
		}
		return(exercices);		
	}


	//Getters

	public String getDescription() {
		return description;
	}




	public int getRecommandation() {
		return recommandation;
	}



	/** Renvoie suivant le type "image" ou "latex" : un string contenant le texte du fichier latex ou l'image encodee en string
	 * @return String
	 * @throws IOException SQLException*/
	public String getEnonce() throws IOException, SQLException {
		switch(type) {
		case "image" : 
			byte[] img = TeechProtocol.extractBytes(enonce);
			return TeechProtocol.encodeImage(img);
		case "latex" : 
			return TeechProtocol.extractText(enonce);
		default :
			throw new SQLException("Unknown type of file, exercice id : " + id);
		}
		
	}
	
	
	/**Renvoie tous les exercices contenus dans la base de donnee dans un array
	 * @return ArrayList<Exercice>
	 * @throws SQLException*/
	public static ArrayList<Exercice> getAll() throws SQLException {
		
		ArrayList<Exercice> array= new ArrayList<Exercice>();
		String query =
				"SELECT * FROM EXERCICES";
		PreparedStatement pstmt = conn.prepareStatement(query);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			array.add(new Exercice(rs));
		}
		return array;
		
	}


	
	

	public int getDifficulteAuteur() {
		return difficulteAuteur;
	}


	public int getIdAuteur() {
		return idAuteur;
	}
	
	public String getType() {
		return type;
	}

	
	public Utilisateur getAuteur() throws SQLException {
		return new Utilisateur(idAuteur);
	}


	public int getDureeConseillee() {
		return dureeConseillee;
	} 
	
}
