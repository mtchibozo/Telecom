package assocTables;

public class matiereExercice {
	private final int idExercice;
	private final int idMatiere;
	private final int proportion;
	private final int difficulte;
	
	public matiereExercice(int idExercice, int idMatiere, int proportion, int difficulte) {
		super();
		this.idExercice = idExercice;
		this.idMatiere = idMatiere;
		this.proportion = proportion;
		this.difficulte = difficulte;
	}
	
	
	public int getIdExercice() {
		return idExercice;
	}
	public int getIdMatiere() {
		return idMatiere;
	}
	public int getProportion() {
		return proportion;
	}
	public int getDifficulte() {
		return difficulte;
	}
	
}
