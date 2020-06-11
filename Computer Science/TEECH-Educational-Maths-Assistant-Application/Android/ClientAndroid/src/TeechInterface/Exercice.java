package TeechInterface;

import java.io.IOException;

public class Exercice implements ExerciceInterface {
	
	private int id;
	private String description;
	private int recommandation; //un entier compris entre 0 et 10 (par exemple) qui ï¿½value la pertinence de l'exercice.
	private String enonce; //Suivant le type : le texte de l'enonce latex ou la photo encodee
	private String type; //une cha�ne de caracteres qui contient le format sous lequel est stocke le corrige : image ou latex
	private int difficulteAuteur; //un entier compris entre 0 et 10 (par exemple) qui reprï¿½sente la difficultï¿½ de l'exercice estimï¿½e par son auteur.
	private int idAuteur; // l'identifiant de l'auteur de l'exercice.
	private int dureeConseillee; // un entier, le nombre de minutes nï¿½cessaires ï¿½ finir l'exercice selon son auteur.
	
	private static String sep = ClientConnection.sep;
	

	public Exercice(int id) throws IOException {
		
		this.id = id;
		String fromServer = ClientConnection.sentToServer("4"+sep+id);
		String[] data =fromServer.split("##");
		
		description = data[1];
		recommandation = Integer.parseInt(data[2]);
		enonce = data[3];
		difficulteAuteur = Integer.parseInt(data[4]);
		idAuteur = Integer.parseInt(data[5]);
		dureeConseillee = Integer.parseInt(data[6]);
		setType(data[7]);
		
	}
	
	public Exercice(String msg)  {
		
		
		String[] data =msg.split("##");
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
		try {
			String fromServer = ClientConnection.sentToServer("8"+sep+id);
			return new Corrige(fromServer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	} 
	
	/**Ajoute un nouveau corrige a l'exercice
	 * @return int
	 * Renvoie l'id du corrige ajoute*/
	public int addCorrige(String description, 
			String indication,
			int recommandation,
			String fileName, // contenu exercice photo encodee ou string latex
			int idAuteur,
			String type)   throws ServerConnectionException, IOException {
		return CorrigeInterface.add(id, description,  indication, recommandation, fileName, idAuteur, type);
	}
	
	/**Ajoute une matiere existante a l'exercice
	 * @throws IOException 
	 * @throws ServerConnectionException */
	public void addMatiere(
			Matiere mat,
			int proportion, //La proportion de la matiere dans l'exercice
			int difficulte //La difficulte de la matiere dans l'exercice ajoute
			) throws IOException, ServerConnectionException {
		String fromServer = ClientConnection.sentToServer("12"+sep+id+sep+mat.getId()+sep+proportion+sep+difficulte);
		if (fromServer.equals("NOK")) throw new ServerConnectionException("addMatiere failed");
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





	public int getId() {
		return id;
	}


	
}
