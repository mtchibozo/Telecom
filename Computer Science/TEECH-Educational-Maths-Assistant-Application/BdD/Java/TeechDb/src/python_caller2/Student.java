package python_caller2;

import java.io.IOException;

import java.util.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


// NB : il faut toujours commencer par mettre a jour la liste des exos termines
// avant de mettre a jour le niveau de l'eleve (car on se sert des exos termines
// pour determiner le niveau).
// Puis on met legerement a jour les exos acceptables
// puis on se sert du niveau et des exercices acceptables pour determiner
// les exercices suivants





public class Student 
{
	
	// ###################################
	// ############ ATTRIBUTS ############
	// ###################################
	
	
	// identifiant d'eleve
	private int id ;
	
	// vecteur representant le niveau de l'eleve dans chaque matiere (attention a l'ordre!)
	private LevelVector levelVector ;
	
	// liste des futurs exercices de l'eleve, dans l'ordre
	private ExerciceList toDoExerciceList ;
	
	// liste des exercices acceptables pour l'eleve : en fait elle correspond aux exos
	// dont la difficulte match avec le niveau de l'eleve dans la matiere à travailler
	// ET QUI N'ONT PAS ETE DEJA FAITS PAR L'ELEVE
	private AcceptableExerciceList acceptableExerciceList ;
	
	// liste des exercices deja faits par l'eleve
	private ExerciceList completedExerciceList ;
	
	// identifiant (numero) de la matiere a travailler
	private int idMainSubject ;
	
	// exercice utile moyen
	private PythonExercice averageUsefulExercice ;
	
	
	
	
	// ###################################
	// ########## CONSTRUCTEURS ##########
	// ###################################
	
	
	// Quand on a deja tous les parametres de l'eleve
	public Student(int id, LevelVector levelVector, AcceptableExerciceList acceptableExerciceList , ExerciceList completedExerciceList)
	{
		this.id = id ;
		this.levelVector = levelVector ;
		this.acceptableExerciceList = acceptableExerciceList ;
		this.completedExerciceList = completedExerciceList ;
	}
	
	
	// Quand on a juste l'id
	public Student(int id)
	{
		this.id = id ;
	}
	
	
	
	
	
	
	
	
	
	// ########################################
	// ########## METHODES D'ACTIONS ##########
	// ########################################
	
	
	
	
	
	
	
	
	
	
	// ########## QUAND L'UTILISATEUR FINIT UN EXERCICE ###########
	
	
	public void newExerciceDone(PythonExercice donePythonExercice) throws IOException
	{
		
		// #### ETAPE 1 : MAJ DES EXOS TERMINES ####
		
		// on commence par ajouter cet exo a la liste des exos completed
		addToCompletedExerciceList(donePythonExercice) ;
		// --> MAJ completedExerciceList termine
		
		// puis on retire cet exo de la liste des exos acceptables
		acceptableExerciceList.removeExercice(donePythonExercice);
		
		
		// #### ETAPE 2 : MAJ DU NIVEAU DE L'ELEVE ####
		
		// on envoie la liste des exos termines et la liste du niveau actuel
		// de l'eleve a Python
		ArrayList<Double> previousLevelTable = this.levelVector.getLevelTable() ;  // on garde en memoire l'ancienne table
		updateLevelTable(levelVector, completedExerciceList);
		// --> MAJ levelTable termine
		
		// TODO : maintenant, le nouveau levelVector est pret a etre envoye a la 
		// base de donnees (mais bon pas du tout oblige de le faire ici)
		
		
		// #### ETAPE 3 : MAJ DES EXOS ACCEPTABLES ####
		
		int comparisonIndicator = 0 ;
		int subjectsNumber = levelVector.getLevelTable().size() ;
		for (int i = 0 ; i < subjectsNumber ; i++)
		{
			if (previousLevelTable.get(i) != levelVector.getLevelTable().get(i))
			{
				comparisonIndicator++ ;
			}
		}
		
		if (comparisonIndicator > 1)
		{
			System.out.println("Il y a un probleme : comparisonIndicator > 1 ");
		}
		
		if (comparisonIndicator > 0)
		{
			acceptableExerciceList.updateWithNewLevel();
		}
		// --> MAJ de acceptableExerciceList termine
		
		
		// #### ETAPE 4 : MAJ DES FUTURS EXOS ####
		
		// on envoie a python une commande avec :
		// - le niveau de l'eleve (levelVector)
		// - le numero de la metiere a travailler (idMainSubject)
		// - la liste des exos acceptables (acceptableExerciceList)
		// - l'exo utile moyen (averageUsefulExercice)
		updateToDoExerciceList();
		// --> MAJ de toDoExerciceList
				
		// TODO : y a plus qu'a envoyer ca a la BDD (mais bon on va peut etre
		// pas le faire ici)

	}
	
	
	
	
	
	// ########## QUAND ON VEUT JUSTE UPDATE LE NIVEAU ###########

	
	public void updateLevelOnly() throws IOException
	{
		
		// #### ETAPE 2 : MAJ DU NIVEAU DE L'ELEVE ####
		
		// on envoie la liste des exos termines et la liste du niveau actuel
		// de l'eleve a Python
		ArrayList<Double> previousLevelTable = this.levelVector.getLevelTable() ;  // on garde en memoire l'ancienne table
		updateLevelTable(this.levelVector, this.completedExerciceList);
		// --> MAJ levelTable termine
		
		
	}
	
	
	
	// ########## QUAND ON VEUT JUSTE CHERCHER DES EXOS ACCEPTABLES ###########

	
	public void lookForAcceptableExercice()
	{
		// dans la BDD des exos tels que :
		// pour l'idMainSubject, le niveau match parfaitement
		// pour les autres matieres, le niveau est inferieur ou egal
		// tous ces exercices n'ont pas deja ete faits
		
		
	}
	
	
	
	
	// ##################################################
	// ####### ASSISTANCES AUX METHODES D'ACTIONS #######
	// ##################################################
	
	
	
	
	
	
	
	
	
	
	public void addToCompletedExerciceList(PythonExercice completedPythonExercice)
	{
		completedExerciceList.addExercice(completedPythonExercice) ;
	}
	
	
	/* INUTILE MTN QUE ACCEPTABLEEXERCICELIST EST UNE CLASSE
	public void removeFromAcceptableExerciceList(PythonExercice wrongPythonExercice)
	{
		acceptableExerciceList.removeExercice(wrongPythonExercice) ;
	}
	*/
	
	
	// Fonctionnement de cette methode :
	// 1) elle commence par tout convertir au format json
	// 2) elle envoie le tout a python et
	// 3) elle exploite les donnees et MAJ la table des niveaux
	public void updateLevelTable(LevelVector levelVector, ExerciceList completedExerciceList) throws IOException
	{
	
		//#### CONVERSION DES DONNEES AU FORMAT JSON ####
		
		// conversion de completedExerciceList
		String jsonCompletedExerciceList ;
		jsonCompletedExerciceList = completedExerciceList.jsonExerciceTableRepresentation() ;
		
		// conversion de la levelTable du levelVector
		String jsonLevelTable ;
		jsonLevelTable = levelVector.jsonLevelTableRepresentation();
		
		
		// #### ENVOI DES DONNEES A PYTHON ####
		
		String newJsonLevelTable ;
		newJsonLevelTable = getNewLevelTableWithPython(jsonLevelTable, jsonCompletedExerciceList);
		
		
		// #### EXPLOITATION DES DONNEES ####
		
		// on demande a levelVector de mettre a jour son levelTable a partir du string
		this.levelVector.updateLevelTableWithJson(newJsonLevelTable);
		
	}
	
	// Focntionnement de cette methode :
	// on envoie a python une commande avec :
	// - le niveau de l'eleve (levelTable)
	// - le numero de la metiere a travailler (idMainSubject)
	// - la liste des exos acceptables (acceptableExerciceList)
	// - l'exo utile moyen (averageUsefulExercice)
	// puis on met a jour la liste toDoExerciceList
	public void updateToDoExerciceList() throws IOException
	{

		//#### CONVERSION DES DONNEES AU FORMAT JSON ####
		
		// conversion de la levelTable du levelVector
		String jsonLevelTable ;
		jsonLevelTable = levelVector.jsonLevelTableRepresentation() ;
		
		// conversion de idMainSubject
		String jsonIdMainSubject ;
		jsonIdMainSubject = idMainSubjectJsonRepresentation() ;
		
		// conversion de acceptableExerciceList
		String jsonAcceptableExerciceList ;
		jsonAcceptableExerciceList = acceptableExerciceList.jsonExerciceTableRepresentation() ;
		
		// conversion de averageUsefulExercice
		String jsonAverageUsefulExercice ;
		jsonAverageUsefulExercice = averageUsefulExercice.jsonRepresentation() ;
				
		// #### ENVOI DES DONNEES A PYTHON ####
		
		String newJsonToDoExerciceList ;
		newJsonToDoExerciceList = getNewJsonToDoExerciceListWithPython(jsonLevelTable, jsonIdMainSubject, jsonAcceptableExerciceList, jsonAverageUsefulExercice);
		
		// #### EXPLOITATION DES DONNEES ####
		
		// on demande a toDoExerciceList de se mettre a jour a partir du json
		this.toDoExerciceList.updateExerciceTableWithJson(newJsonToDoExerciceList) ;
		

	}
	
	
	
	
	
	
	
	
	
	
	// #####################################
	// ########## METHODES PYTHON ##########
	// #####################################
	
	
	
	
	
	
	
	
	
	// renvoie un string json correspondant au nouveau levelVector
	public String getNewLevelTableWithPython(String jsonLevelTable, String jsonCompletedExerciceList) throws IOException
	{
		
		// #### initialisation de la commande ####	
		
		String[] cmd = new String[4];  // 4 car : "python" + chemin_jusqu'a_python + jsonCompletedExerciceList + jsonLevelTable
		
		
		// #### arguments 0 et 1 : "python" et path ####
		// TODO : path peut etre a changer
		String pythonScriptPath = "/Users/jeanvassoyan/Documents/Projets pédagogiques/PACT/workspace - PACT/Python/Integration_python/Java_Python_Java_INTERFACE.py";
		cmd[0] = "python";
		cmd[1] = pythonScriptPath;
		
		// #### argument 2 : table d exercices ####
		
		cmd[2] = jsonCompletedExerciceList ;
		
		// #### argument 3 : table des niveaux de l'eleve ####
	
		cmd[3] = jsonLevelTable ;
	
	
		// #### conversion et execution ####
		
		// create runtime to execute external command
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);
		 
	// #### lecture du retour python ####
		
		BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		String line2 = "line2 n'a pas ete modifie" ; // renverra ca en cas d'erreur

		while((line = bfr.readLine()) != null) 
		{
			// display each output line form python script
			line2 = line ;
		}

		return line2 ;
	}
	
	
	
	
	public String getNewJsonToDoExerciceListWithPython(String jsonLevelTable,
			String jsonIdMainSubject,String jsonAcceptableExerciceList,
			String jsonAverageUsefulExercice) throws IOException
	{

		// #### initialisation de la commande ####	
		
		String[] cmd = new String[6];  // 6 car : "python" + chemin_jusqu'a_python 
									  // + jsonLevelTable + jsonIdMainSubject 
									  // + jsonAcceptableExerciceList + jsonAverageUsefulExercice 
		
		// #### arguments 0 et 1 : "python" et path ####
		
		// TODO : path peut etre a changer
		String pythonScriptPath = "/Users/jeanvassoyan/Documents/Projets pédagogiques/PACT/workspace - PACT/Python/Integration_python/Java_Python_Java_INTERFACE.py";
		cmd[0] = "python";
		cmd[1] = pythonScriptPath;
		
		// #### argument 2 : table des niveaux de l'eleve ####
		
		cmd[2] = jsonLevelTable ;
		
		// #### argument 3 : identifiant matiere principale ####
		
		cmd[3] = jsonIdMainSubject ;
		
		// #### argument 4 : liste des exos acceptables ####
		
		cmd[4] = jsonAcceptableExerciceList ;

		// #### argument 5 : identifiant matiere principale ####
		
		cmd[5] = jsonAverageUsefulExercice ;
		
		
		// #### conversion et execution ####
		
		// create runtime to execute external command
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);
		 
		// #### lecture du retour python ####
		
		BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		String line2 = "line2 n'a pas ete modifie" ; // renverra ca en cas d'erreur

		while((line = bfr.readLine()) != null) 
		{
			// display each output line form python script
			line2 = line ;
		}

		return line2 ;
	}
	
	
	
	
	
	
	
	
	
	
	// #############################################################
	
	
	
	
	
	
	
	
	public void modifyMainSubject(int idMainSubject)
	{
		this.idMainSubject = idMainSubject ;
		
		// ATTENTION //
		// ICI IL FAUT METTRE A JOUR acceptableExerciceList ET LANCER TOUT LE PROCESSUS DERRIERE
		// MAJ exo utile moyen
	}
	
	
	
	// ###################################################
	
	
	
	
	
	// ######################################################
	// ########## METHODES POUR PAS CREER LES OBJETS ##########
	// ######################################################
	
	
	public String idMainSubjectJsonRepresentation()
	{
		// initialisation de Json
		final GsonBuilder builder = new GsonBuilder() ;
		final Gson gson = builder.create();
		
		// conversion de l'attribut idMainSubject au format Json
		final String jsonIdMainSubject = gson.toJson(this.idMainSubject);
		
		return jsonIdMainSubject ;
	}
	
	
	
	// #############################
	// ########## GETTERS ##########
	// #############################
	
	
	public int getIdMainSubject()
	{
		return this.idMainSubject ;
	}
	
	public LevelVector getLevelVector()
	{
		return this.levelVector ;
	}
	
	
	// #############################
	// ########## SETTERS ##########
	// #############################
	
	
	public void setLevelVector(LevelVector levelVector)
	{
		this.levelVector = levelVector ;
	}
	
	
	public void setCompletedExerciceList(ExerciceList completedExerciceList)
	{
		this.completedExerciceList = completedExerciceList ;
	}
	
	
	public void setIdMainSubject(int idMainSubject)
	{
		this.idMainSubject = idMainSubject ;
	}
	
	
	public void setAverageUsefulExercice(PythonExercice averageUsefulExercice)
	{
		this.averageUsefulExercice = averageUsefulExercice ;
	}
	
	
}
