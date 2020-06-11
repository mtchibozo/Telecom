package tableEntities;

import java.sql.*;
import bdd.*;

public class Exercice extends DatabaseEntry {
	
	private String description;
	private int recommandation; //un entier compris entre 0 et 10 (par exemple) qui évalue la pertinence de l'exercice.
	private String enonce; // une chaîne de caractères qui contient l'adresse du fichier (je pense que rentrer les exercices sous forme de photo ou de scan serait vraiment une bonne idée) qui contient l’énoncé de l'exercice.
	private int difficulteAuteur; //un entier compris entre 0 et 10 (par exemple) qui représente la difficulté de l'exercice estimée par son auteur.
	private int idAuteur; // l'identifiant de l'auteur de l'exercice.
	private int dureeConseillee; // un entier, le nombre de minutes nécessaires à finir l'exercice selon son auteur.

	


	public Exercice(int id) throws SQLException {
		super("EXERCICES", id);
		description = data.getString(2);
		recommandation = data.getInt(3);
		enonce = data.getString(4);
		difficulteAuteur = data.getInt(5);
		idAuteur = data.getInt(6);
		dureeConseillee = data.getInt(7);
	}
	
	
	private Exercice(String description, 
			int recommandation,
			String enonce,
			int difficulteAuteur,
			int idAuteur,
			int dureeConseillee) throws SQLException {
		
		super("EXERCICES");
		this.recommandation = recommandation;
		this.enonce = enonce;
		this.difficulteAuteur = difficulteAuteur;
		this.idAuteur =idAuteur;
		this.dureeConseillee = dureeConseillee;
		
		
	}
		
	public static Exercice add(String description, 
							int recommandation,
							String enonce,
							int difficulteAuteur,
							int idAuteur,
							int dureeConseillee) throws SQLException {
		
		Exercice exo = new Exercice(description, recommandation, enonce, difficulteAuteur, idAuteur, dureeConseillee) ;
		
		int id = exo.newId();
		exo.setId(id);
		String query =  "INSERT INTO EXERCICES VALUES (id, description, recommandation, enonce, difficulteAuteur, idAuteur, dureeConseillee)"
				+ "VALUES(?,?,?,?,?,?,?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1,id);
		pstmt.setString(2, description);
		pstmt.setInt(3, recommandation);
		pstmt.setString(4, enonce);
		pstmt.setInt(5, difficulteAuteur);
		pstmt.setInt(6, idAuteur);
		pstmt.setInt(7, dureeConseillee);
		pstmt.executeUpdate();
		
		return exo;
		
	}
	
	
	/**Associe une matiere a l'exercice
	 * @throws SQLException*/
	public final void addMatiere(int idMatiere, int proportion, int difficulte) throws SQLException {
		String query = "INSERT INTO MATIERE_EXERCICE (idMatiere, idExercice, proportion , difficulte) VALUES(?, ?, ?)"; // Ajout d'une entree dans la table MATIERE_EXERCICE
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, idMatiere);
		pstmt.setInt(2, id);
		pstmt.setInt(3, proportion);
		pstmt.setInt(4, difficulte);
	}
	
	
	public void show() {
		new ImageFrame(getDescription(), getEnonce()); 
	}


	//Getters

	public String getDescription() {
		return description;
	}




	public int getRecommandation() {
		return recommandation;
	}




	public String getEnonce() {
		return enonce;
	}




	public int getDifficulteAuteur() {
		return difficulteAuteur;
	}




	public int getIdAuteur() {
		return idAuteur;
	}

	
	public Utilisateur getAuteur() throws SQLException {
		return new Utilisateur(idAuteur);
	}


	public int getDureeConseillee() {
		return dureeConseillee;
	} 
	
}
