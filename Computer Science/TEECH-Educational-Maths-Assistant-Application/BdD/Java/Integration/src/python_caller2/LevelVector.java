package python_caller2;

import java.util.* ;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class LevelVector
{
	
	
	// ###################################
	// ############ ATTRIBUTS ############
	// ###################################
	
	
	// la liste de niveaux par matiere
	ArrayList<Double> levelTable ;
	
	// l'eleve auquel elle est attribuee
	Student student ;
	
	
	
	
	// ###################################
	// ########## CONSTRUCTEURS ##########
	// ###################################
	
	
	public LevelVector(Student student, ArrayList<Double> levelTable)
	{
		this.student = student ;
		this.levelTable = levelTable ;
		
	}
	
	
	
	
	
	
	
	// ##############################
	// ########## METHODES ##########
	// ##############################
	
	// en fait on ne represente en json que la levelTable, c est plus simple apres
	public String jsonLevelTableRepresentation()
	{
		// initialisation de Json
		final GsonBuilder builder = new GsonBuilder() ;
		final Gson gson = builder.create();
		
		// conversion de l'attribut levelTable au format Json
		final String jsonLevelTable = gson.toJson(this.levelTable);
		
		return jsonLevelTable ;
	}
	
	
	
	public void updateLevelTableWithJson(String newJsonLevelTable)
	{
		// initialisation de Json
		final GsonBuilder builder = new GsonBuilder() ;
		final Gson gson = builder.create();
		
		// concretisation du json : creation d une levelTable a partir du json
		ArrayList<Double> newLevelTable = gson.fromJson(newJsonLevelTable, ArrayList.class) ;
		
		// mise a jour de exerciceTable et sortedIndicato
		this.levelTable = newLevelTable ;
		
	}
	
	
	
	
	// ##############################
	// ########## GETTERS ###########
	// ##############################
	
	
	public ArrayList<Double> getLevelTable()
	{
		return this.levelTable ;
	}
	
	
	
	
	
	
	
	
	
	
	

}
