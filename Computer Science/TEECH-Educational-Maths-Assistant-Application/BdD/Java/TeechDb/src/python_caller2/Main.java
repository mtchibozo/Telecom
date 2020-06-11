package python_caller2;

import java.util.* ;
import java.io.* ;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Main {

	public static void main(String[] args) throws IOException 
	{
		/*
					///// On cree un nouvel exercice : ex0 /////
		
		int id = 0 ;
		
		ArrayList weightsVector = new ArrayList();
		weightsVector.add(0.5) ;
		weightsVector.add(0.25) ;
		weightsVector.add(0.25) ;
		
		ArrayList difficultiesVector = new ArrayList();
		difficultiesVector.add(1);
		difficultiesVector.add(2);
		difficultiesVector.add(3);
		
		
		PythonExercice pythonExercice0 = new PythonExercice(id, weightsVector, difficultiesVector);
		
		
					///// On cree un nouvel exercice : ex1 /////

		int id1 = 111 ;
				
		ArrayList weightsVector1 = new ArrayList();
		weightsVector1.add(0.1) ;
		weightsVector1.add(0.1) ;
		weightsVector1.add(0.1) ;
		
		ArrayList difficultiesVector1 = new ArrayList();
		difficultiesVector1.add(1);
		difficultiesVector1.add(1);
		difficultiesVector1.add(1);
		
		
		PythonExercice pythonExercice1 = new PythonExercice(id1, weightsVector1, difficultiesVector1);		
		
		
				 ///// On cree un nouvel exercice : ex2 /////

		int id2 = 222 ;
					
		ArrayList weightsVector2 = new ArrayList();
		weightsVector2.add(0.2) ;
		weightsVector2.add(0.2) ;
		weightsVector2.add(0.2) ;
		
		ArrayList difficultiesVector2 = new ArrayList();
		difficultiesVector2.add(2);
		difficultiesVector2.add(2);
		difficultiesVector2.add(2);
		
		
		PythonExercice pythonExercice2 = new PythonExercice(id2, weightsVector2, difficultiesVector2);	
		
		
		
		            ///// Creation du tableau d exercices /////
		
		// taille du tableau
		int tableSize = 3 ;
		
		// tableau
		PythonExercice[] exerciceTable = new PythonExercice[tableSize] ;
		
		// remplissage tableau
		exerciceTable[0] = pythonExercice0 ;
		exerciceTable[1] = pythonExercice1 ;
		exerciceTable[2] = pythonExercice2 ;

		
		
					///// On le convertit au format Json /////
		
		
		// initialisation de Json
		
		final GsonBuilder builder = new GsonBuilder() ;
		final Gson gson = builder.create();
		
		// convertir au format Json
		
		final String jsonTable = gson.toJson(exerciceTable);
		System.out.println("Voici le tableau envoye a python : ");
		System.out.println(jsonTable);
		
		
					///// On l envoie a Python /////
		
		// path : /Users/jeanvassoyan/python_prog_easy_access/From_command_to_dict.py
		
		
			// set up the command and parameter
			// creation de la commande cmd
		
		// arguments 0 et 1 : "python" et path
		String pythonScriptPath = "/Users/jeanvassoyan/python_prog_easy_access/From_command_to_dict.py";
		int numberOfArguments = 1 ;
		String[] cmd = new String[2 + numberOfArguments];
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
		while((line = bfr.readLine()) != null) 
		{
		// display each output line form python script
			System.out.println("Voici la liste renvoyee par python :");
			System.out.println(line);
		}
		*/
		
		
		/*
		final GsonBuilder builder = new GsonBuilder() ;
		final Gson gson = builder.create();
		
		
		String receivedTable ;
		receivedTable = "[{\"id\":0,\"weightsVector\":[0.5,0.25,0.25],\"difficultiesVector\":[1,2,3]},{\"id\":111,\"weightsVector\":[0.1,0.1,0.1],\"difficultiesVector\":[1,1,1]},{\"id\":222,\"weightsVector\":[0.2,0.2,0.2],\"difficultiesVector\":[2,2,2]}]" ;
		
		PythonExercice[] pythonExerciceTable = gson.fromJson(receivedTable, PythonExercice[].class);
		
		int id = pythonExerciceTable[0].getId();
		double[] weightsVector = pythonExerciceTable[0].getWeightsVector() ;
		
		//System.out.println(id); //expected : 0
		//System.out.println(weightsVector[1]); // expected : [0.5,0.25,0.25]
		
		
		
		String json = gson.toJson(pythonExerciceTable);
		
		System.out.println(json);
		System.out.println(pythonExerciceTable[0].getClass());
		*/
		
		
		
		/*
		// creation de la table de base
		String jsonExerciceTable ;
		jsonExerciceTable = "[{\"id\":0,\"weightsVector\":[0.5,0.25,0.25],\"difficultiesVector\":[1,2,3]},{\"id\":111,\"weightsVector\":[0.1,0.1,0.1],\"difficultiesVector\":[1,1,1]},{\"id\":222,\"weightsVector\":[0.2,0.2,0.2],\"difficultiesVector\":[2,2,2]}]" ;
		//System.out.println("On est au debut Voici la jsonExerciceTable : " + jsonExerciceTable);

		
		// creation de l objet
		ExerciceList exerciceList = new ExerciceList(jsonExerciceTable);
		
		// traitement par python
		exerciceList.sortWithPython();
		
		//affichage
		System.out.println(exerciceList.getTable()[0].getId());  // renvoie l'id du premier exo
		System.out.println(exerciceList.getTable()[0].getWeightsVector()[1]); // renvoie...
		System.out.println(exerciceList.getTable()[0].getdifficultiessVector()[2]); // renvoie...
		*/
		
		
		// ---------------------------------------
		
		
		
		
		// L'id de l'eleve est donne
		int id = 10 ;
		
		
		// ### creation de l'eleve ###
		
		Student student = new Student(id) ;
		
		// levelVector
		
		LevelVector levelVector ;
		
		
		// completedExerciceList
		
		ExerciceList completedExerciceList ;
		
		// idMainSubject
		
		int idMainSubject ;
		
		// averageUsefulExercice
		
		PythonExercice averageUsefulExercice ;
		
		
		// creer student a partir de ces parametres
		
		student.setLevelVector(levelVector);
		student.setIdMainSubject(idMainSubject);
		student.setCompletedExerciceList(completedExerciceList);
		student.setAverageUsefulExercice(averageUsefulExercice);
		
		// ### calculer le nouveau levelVector ###
		
		student.updateLevelOnly();
		
		// ### faire la liste des exos acceptables ###
		
		
		
		
		// ### envoyer exos acceptables a python pour en tirer toDoExercice ###
		
		
		
		
		

	}
	
	

}






