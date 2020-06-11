package TeechInterface;

public class Evaluation {
	
	private final int id;
	private final int idUtilisateur;
	private final int idExercice;
	private final int evaluation;
	private final int recommandation; 
	private final int difficulte; 
	private final String date;

	public Evaluation(String msg) {
		String[] data =msg.split(ClientConnection.sep);
		id = Integer.parseInt(data[0]);
		idUtilisateur = Integer.parseInt(data[1]);
		idExercice = Integer.parseInt(data[2]);
		evaluation = Integer.parseInt(data[3]);
		recommandation = Integer.parseInt(data[4]); 
		difficulte = Integer.parseInt(data[5]); 
		date = data[0];
	}
	
	
	
	public int getId() {
		return id;
	}
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
	
	
}
