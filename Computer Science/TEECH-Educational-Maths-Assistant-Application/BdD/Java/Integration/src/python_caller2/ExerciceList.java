package python_caller2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/*
 * ######################
 * ##### IMPORTANT ######
 * ######################
 * 
 * 
 * ATTENTION : ce code, base sur une communication avec un programme python, n est
 * fonctionnel que dans la mesure ou le path utilise plus bas conduit bel et bien a
 * ce programme.
 * Ce path est utilise dans la methode de signature : 
 * public String sendToPython(String jsonTable) throws IOException
 * 
 * Ainsi, dans le programme d'origine, le path est le suivant :
 * "/Users/jeanvassoyan/Documents/Projets pédagogiques/PACT/workspace - PACT/Python/Integration_python/Java_Python_Java_INTERFACE.py"
 * Mais il est fort possible que sur votre machine, ce path soit a modifier, pour conduire
 * au programme python adequat.
 * 
 *
 *
 * ###################################
 * ### PRESENTATION DU PROGRAMME #####
 * ###################################
 * 
 * 
 * Classe des objets de type liste d'exercices 
 * Ces objets contiennent :
 * - un indicateur pour dire si la liste a ete triee par python (1 si oui, 0 sinon)
 * - un tableau d objets pythonExercice
 *
 *
 *
 * ##############
 * ### MANUEL ###
 * ##############
 * 
 * Soit un objet exerciceList du type ExerciceList
 * On peut le trier en faisant : exerciceList.sortWithPython()
 * 
 * 
 * 
 * 
 */



public class ExerciceList 
{
	
	// ###################################
	// ############ ATTRIBUTS ############
	// ###################################
	
	
	
	// boolean indiquant si la liste d exos exerciceTable est dans le bon ordre
	// IMPORTANT : ce boolean doit repasser a zero a chaque fois que l'eleve termine un
	// exercice (car en fonction du resultat le niveau de l'eleve et donc la liste d'exos 
	// peut changer)
	private int sortedIndicator ;
	
	// Liste d'exercices a faire (pour l'eleve)
	private ArrayList<PythonExercice> exerciceTable ;
	
	// eleve attribue a ces listes
	private Student student ;
	
	
	
	// ###################################
	// ########## CONSTRUCTEURS ##########
	// ###################################
	
	
	
	// 1er constructeur : quand on a deja toutes les infos
	
	public ExerciceList(int sortedIndicator, ArrayList<PythonExercice> exerciceTable)
	{
		
		this.sortedIndicator = sortedIndicator ;
		this.exerciceTable = exerciceTable ;
		
	}
	
	
	// 2e constructeur : quand on n'a pas d info
	
	public ExerciceList(PythonExercice[] exerciceTable)
	{
		
		this.sortedIndicator = 0 ;
		
	}
	
	
	// 3e constructeur : a partir d'un doc json
	
	public ExerciceList(String jsonExerciceTable)
	{
		// initialisation de Json
		final GsonBuilder builder = new GsonBuilder() ;
		final Gson gson = builder.create();
		
		// concretisation du json : creation d un ExerciceList a son image
		this.exerciceTable = gson.fromJson(jsonExerciceTable, ArrayList.class) ;

	}
	
	
	
	
	
	
	
	// ##############################
	// ########## METHODES ##########
	// ##############################
	
	
	
	
	// deduit le string json de l'objet ExerciceList
	
	public String jsonExerciceTableRepresentation()
	{
		// initialisation de Json
		final GsonBuilder builder = new GsonBuilder() ;
		final Gson gson = builder.create();
		
		// conversion de l'attribut exerciceTable au format Json
		final String jsonExerciceTable = gson.toJson(this.exerciceTable);
		
		return jsonExerciceTable ;
	}
	
	
	
	// remodifie exerciceTable a partir du script jsonSortedTable (normalement fourni par python)
	
	public void updateExerciceTableWithJson(String newJsonLevelTable)
	{
		// initialisation de Json
		final GsonBuilder builder = new GsonBuilder() ;
		final Gson gson = builder.create();
		
		// concretisation du json : creation d un ExerciceList a son image
		ArrayList<PythonExercice> newExerciceTable = gson.fromJson(newJsonLevelTable, ArrayList.class) ;
		
		// mise a jour de exerciceTable et sortedIndicator
		this.exerciceTable = newExerciceTable ;
		this.sortedIndicator = 1 ;
		
	}
	
	
	
	// ON JETTE CETTE PARTIE (MAIS PAS SUR DONC ON GARDE EN COMMENTAIRES)
	/*
	
	public void sortWithPython() throws IOException
	{
		System.out.println("On entre dans sortWithPython");
		//creation du json
		String jsonTable ;
		jsonTable = this.jsonRepresentation();
		System.out.println("Voici le json envoye a Python :");
		System.out.println(jsonTable);
	
		
		//envoi du json a Python
		String jsonSortedTable ;
		jsonSortedTable = this.sendToPython(jsonTable);
		System.out.println("Voici le json recu de Python : ");
		System.out.println(jsonSortedTable);
	
	
		//analyse du json et modification de l'objet ExerciceList en consequence
		this.reajust(jsonSortedTable) ;
		
	}
	
	
	// envoie le json de ExerciceList a Python et recupere un nouveau fichier json
	
	public String sendToPython(String jsonTable) throws IOException
	{
	
	// arguments 0 et 1 : "python" et path
	
	String pythonScriptPath = "/Users/jeanvassoyan/Documents/Projets pédagogiques/PACT/workspace - PACT/Python/Integration_python/Java_Python_Java_INTERFACE.py";
	
	String[] cmd = new String[3];  // 3 car : "python" + chemin_jusqu'a_python + jsonTable 
	cmd[0] = "python";
	cmd[1] = pythonScriptPath;
	
	// argument 2 : tableau d exercices
	
	cmd[2] = jsonTable ;
	
	// create runtime to execute external command
	Runtime rt = Runtime.getRuntime();
	Process pr = rt.exec(cmd);
	 
	// retrieve output from python script
	BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
	String line = "";
	String line2 = "line2 non modifie" ;

	while((line = bfr.readLine()) != null) 
	{
		// display each output line form python script
		//System.out.println("Voici la liste renvoyee par python :");
		//System.out.println(line);
		line2 = line ;
	}

	return line2 ;
	
	}
	*/
	
	
	
	// ##############################
	// ########## GETTERS ###########
	// ##############################
	
	
	public int getSortedIndicator()
	{
		return this.sortedIndicator ;
	}
	
	public ArrayList<PythonExercice> getExerciceTable()
	{
		return exerciceTable ;
	}
	
	public Student getStudent()
	{
		return student ;
	}
	
	
	// ##############################
	// ########## SETTERS ###########
	// ##############################	
	
	
	public void removeExercice(PythonExercice wrongPythonExercice)
	{		
		exerciceTable.remove(wrongPythonExercice);
	}
	
	
	public void addExercice(PythonExercice newPythonExercice)
	{
		exerciceTable.add(newPythonExercice) ;
	}
	
	
	//setters
	
	public void setSortedIndicator(int sortedIndicator)
	{
		this.sortedIndicator = sortedIndicator ;
	}
	
	
	

}
