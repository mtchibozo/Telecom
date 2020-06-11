package tableEntities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Evaluation extends DatabaseEntry {
	
	private int idUtilisateur;
	private int idExercice;
	private int evaluation;
	private int recommandation; 
	private int difficulte; 
	private String date;
	private int idDemande;
	private final Connection conn = dbc.getConn();
	
	public Evaluation(int id) throws SQLException {
		super("EVALUATIONS", id);
		idUtilisateur = data.getInt(2);
		idExercice = data.getInt(3);
		evaluation = data.getInt(4);
		recommandation = data.getInt(5);
		difficulte = data.getInt(6);
		date = data.getString(7);
		idDemande = data.getInt(8);
	}
	
	private Evaluation(
			int idUtilisateur,
			int idExercice,
			int evaluation,
			int recommandation,
			int difficulte, 
			String date,
			int idDemande
			) throws SQLException {
		super("EVALUATIONS");
		this.idUtilisateur=idUtilisateur;
		this.idExercice=idExercice;
		this.evaluation=evaluation;
		this.recommandation=recommandation;
		this.difficulte=difficulte;
		this.date=date;
		this.idDemande=idDemande;
	}

	public static final Evaluation add(
			int idUtilisateur,
			int idExercice,
			int evaluation,
			int recommandation,
			int difficulte, 
			String date,
			int idDemande) throws SQLException {
		
		Evaluation eval = new Evaluation(idUtilisateur, idExercice, evaluation, recommandation, difficulte, date, idDemande );
		int id = eval.newId();
		eval.setId(id);
		Connection conn = dbc.getConn();
		String query =  "INSERT INTO DEMANDES_UTILISATEURS (id, idMatiere, typeTravail, date) VALUES(?, ?, ?, ?)";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1,id);
		pstmt.setInt(2,idUtilisateur);
		pstmt.setInt(3, idExercice);
		pstmt.setInt(4,evaluation);
		pstmt.setInt(5,recommandation);
		pstmt.setInt(6, difficulte);
		pstmt.setString(7,date);
		pstmt.setInt(8,idDemande);
		pstmt.executeUpdate();
		
		return eval;
	}

	//Getters
	public int getIdUtilisateur() {
		return idUtilisateur;
	}

	public int getIdExercice() {
		return idExercice;
	}

	public int getEvaluation() {
		return evaluation;
	}

	public int getRecommandation() {
		return recommandation;
	}

	public int getDifficulte() {
		return difficulte;
	}

	public String getDate() {
		return date;
	}

	public int getIdDemande() {
		return idDemande;
	}

	public Connection getConn() {
		return conn;
	}
	
	
	
	
}
