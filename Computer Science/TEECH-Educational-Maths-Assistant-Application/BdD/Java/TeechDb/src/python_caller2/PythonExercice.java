package python_caller2 ;

/*
 * Cette classe est celle des exercices au sens de l'algorithme utilise dans python.
 * Ainsi, un exercice au sens de l'algo python est un triplet : (identifiant, vecteur des
 * poids de chaque matiere dans l exercice, vecteur des difficultes de chaque matiere dans 
 * l exercice)
 * Ainsi, cette classe est concue pour que chaque exercice contienne ces trois attributs
 */


import java.util.* ;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PythonExercice 
{
	
	// identifiant de l'exercice
	private final int id ;
	
	
	private ArrayList<Double> weightsVector ;
	private ArrayList<Double> difficultiesVector ;
	
	private int mark ; // compris entre 0 et 10
	
	public PythonExercice(int id, ArrayList<Double> weightsVector, ArrayList<Double> difficultiesVector, int mark)
	{
		this.id = id ;
		this.weightsVector = weightsVector ;
		this.difficultiesVector = difficultiesVector ;
		this.mark = mark ;
	}
	
	
	
	
	// methodes
	
	public String jsonRepresentation()
	{
		// initialisation de Json
		final GsonBuilder builder = new GsonBuilder() ;
		final Gson gson = builder.create();
		
		// conversion de l'attribut exerciceTable au format Json
		final String jsonPythonExercice = gson.toJson(this);
		
		return jsonPythonExercice ;
	}
	
	
	
	
	
	// getters
	
	public int getId()
	{
		return id ;
	}
	
	public ArrayList<Double> getWeightsVector()
	{
		return weightsVector ;
	}
	
	public ArrayList<Double> getdifficultiessVector()
	{
		return difficultiesVector ;
	}
	
	public int getMark()
	{
		return mark ;
	}
	
	
	

}