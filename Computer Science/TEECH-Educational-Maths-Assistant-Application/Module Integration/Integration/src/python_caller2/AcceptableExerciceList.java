package python_caller2;

import java.util.ArrayList;

public class AcceptableExerciceList extends ExerciceList 
{
	
	// #### ATTRIBUTS SUPPLEMENTAIRES ####
	
	
	
	
	// #### CONSTRUCTEURS HERITES ####
	
	
	public AcceptableExerciceList(int sortedIndicator, ArrayList<PythonExercice> exerciceTable) 
	{
		super(sortedIndicator, exerciceTable);
		
		// TODO Auto-generated constructor stub
	}
	
	public AcceptableExerciceList(PythonExercice[] exerciceTable) 
	{
		super(exerciceTable);
		
		// TODO Auto-generated constructor stub
	}

	public AcceptableExerciceList(String jsonExerciceTable) 
	{
		super(jsonExerciceTable);
		
		// TODO Auto-generated constructor stub
	}
	
	
	// #### CONSTRUCTEURS NOUVEAUX ####
	
	
	
	
	
	// #### METHODES ####
	
	// TODO
	public void updateWithNewLevel(int idMainSubject, LevelVector levelVector, ExerciceList completedExerciceList)
	{
		// fonction qui va chercher tous les exercices de la base de donnees
		// qui ont un niveau donne (levelVector) dans une matiere donnee
		// (idMainSubject)
		
		
	}
	
	
}
