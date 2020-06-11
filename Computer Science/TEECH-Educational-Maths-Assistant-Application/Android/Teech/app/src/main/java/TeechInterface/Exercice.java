package TeechInterface;

import java.io.IOException;

public class Exercice implements ExerciceInterface {
	
	private int id;
	private String description;
	private int recommandation; //un entier compris entre 0 et 10 (par exemple) qui évalue la pertinence de l'exercice.
	private String enonce; //une chaîne de caractères qui contient l'adresse du fichier (je pense que rentrer les exercices sous forme de photo ou de scan serait vraiment une bonne idée) qui contient l'énoncé de l'exercice.
	private String type;
	private int difficulteAuteur; //un entier compris entre 0 et 10 (par exemple) qui représente la difficulté de l'exercice estimée par son auteur.
	private int idAuteur; // l'identifiant de l'auteur de l'exercice.
	private int dureeConseillee; // un entier, le nombre de minutes nécessaires pour finir l'exercice selon son auteur.
	
	private static String sep = ClientConnection.sep;
	

	public Exercice(int id) throws IOException {

		this.id = id;
		String fromServer = ClientConnection.sentToServer("4"+sep+id);
		String[] data =fromServer.split(sep);

		description = data[1];
		recommandation = Integer.parseInt(data[2]);
		enonce = data[3];
		difficulteAuteur = Integer.parseInt(data[4]);
		idAuteur = Integer.parseInt(data[5]);
		dureeConseillee = Integer.parseInt(data[6]);
		setType(data[7]);

	}


	public Exercice(String msg)  {

		String[] data =msg.split(sep);
		id = Integer.parseInt(data[0]);
		description = data[1];
		recommandation = Integer.parseInt(data[2]);
		enonce = data[3];
		difficulteAuteur = Integer.parseInt(data[4]);
		idAuteur = Integer.parseInt(data[5]);
		dureeConseillee = Integer.parseInt(data[6]);
		setType(data[7]);

	}


	@Override
	public Corrige findCorrige() {
		return Exercice.findCorrige(id);
	}

	public static Corrige findCorrige(int id){
		try {
			String fromServer = ClientConnection.sentToServer("8"+sep+id);
			return new Corrige(fromServer);
		} catch (IOException e) {return null;}
	}


	/**Ajoute un nouveau corrige a l'exercice
	 * @return int
	 * Renvoie l'id du corrige ajoute*/
	public int addCorrige(String description,
						  String indication,
						  int recommandation,
						  String enonce, // contenu exercice photo encodee ou string latex
						  int idAuteur,
						  String type)   throws ServerConnectionException, IOException {
		return Corrige.add(id, description,  indication, recommandation, enonce, idAuteur, type);
	}

	/**Ajoute une matiere existante a l'exercice*/
	public static void addMatiere(
			int id,
			Matiere mat,
			int proportion, //La proportion de la matiere dans l'exercice
			int difficulte //La difficulte de la matiere dans l'exercice ajoute
	) throws IOException, ServerConnectionException {
		String fromServer = ClientConnection.sentToServer("12"+sep+id+sep+mat.getId()+sep+proportion+sep+difficulte);
		if (fromServer.equals("NOK")) throw new ServerConnectionException("addMatiere failed");
	}



	/**ajoute l'execice a la base de donnee*/
	public static int add(String description,
						   int recommandation,
						   String corps, //fichier contenant l'enonce de l'exercice png ou chaine latex
						   int difficulteAuteur,
						   int idAuteur,
						   int dureeConseillee,
						   String type) throws ServerConnectionException, IOException {
		String fromServer = ClientConnection.sentToServer("5"+sep+description+sep+recommandation+sep+corps+sep+difficulteAuteur+sep+idAuteur+sep+dureeConseillee+sep+type);
		if (fromServer.equals("NOK")) {
			throw new ServerConnectionException("Add exercice failed");
		}
		return Integer.parseInt(fromServer);
	}


	public int add() throws ServerConnectionException, IOException{
		return Exercice.add(description, recommandation, enonce, difficulteAuteur, idAuteur, dureeConseillee, type);
	}

	
	//Getters

	public String getDescription() {
		return description;
	}


	public int getRecommandation() {
		return recommandation;
	}


	public String getEnonce() {
		return enonce;
	}


	public int getDifficulteAuteur() {
		return difficulteAuteur;
	}


	public int getIdAuteur() {
		return idAuteur;
	}


	public int getDureeConseillee() {
		return dureeConseillee;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	public int getId(){return id;}
	
}
