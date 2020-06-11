package interfaceIA;

import java.util.ArrayList;

public interface ExerciceInterface 
{

	/** Renvoie dans un array la liste de tous les exercices*/
	public ArrayList<ExerciceInterface> getAll();
	
	/** Renvoie dans un array les matières, proportion, difficulté et recommandation associés **/
	public ArrayList<ExerciceInterface> getExercice(int idEx);
	
	
}
