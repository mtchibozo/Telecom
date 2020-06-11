package TeechInterface;

import java.io.IOException;

public class Corrige implements CorrigeInterface {

	private int id;
	private int idExercice; //identifiant de l'exercie que le corrig� traite.
	private String indication; //une cha�ne de caract�res qui donne des pistes de r�solution pour l'exercice.
	private String description;
	private int recommandation; //un entier compris entre 0 et 10 (par exemple) qui �value la pertinence de l'exercice.
	private String enonce; //Suivant le type : le texte de l'enonce latex ou la photo encodee
	private int idAuteur; 
	private String type; //une cha�ne de caracteres qui contient le format sous lequel est stocke le corrige : image ou latex
	private static String sep = ClientConnection.sep;
	
	public Corrige(int id) throws IOException {
		
		this.id = id;
		String fromServer = ClientConnection.sentToServer("6"+sep+id);
		String[] data =fromServer.split("##");
		idExercice = Integer.parseInt(data[1]);
		indication = data[2];
		description = data[3];
		recommandation = Integer.parseInt(data[4]);
		enonce = data[5];
		idAuteur = Integer.parseInt(data[6]);
		type = data[7];
		
	}
	
	Corrige(String msg) throws IOException {
		
		String[] data =msg.split(sep);
		id = Integer.parseInt(data[0]) ;
		idExercice = Integer.parseInt(data[1]);
		indication = data[2];
		description = data[3];
		recommandation = Integer.parseInt(data[4]);
		enonce = data[5];
		idAuteur = Integer.parseInt(data[6]);
		type = data[7];
		
	}


	public static int add(
			int idExercice,
			String description,
			String indication,
			int recommendation,
			String enonce, // contenu exercice photo encodee ou string latex
			int idAuteur,
			String type)   throws ServerConnectionException, IOException {

		String fromServer = ClientConnection.sentToServer("7"+sep+idExercice+sep+description+sep+indication+sep+recommendation+sep+enonce+sep+idAuteur+sep+type);

		if (fromServer.equals("NOK")) {
			throw new ServerConnectionException("Add exercice failed");
		}

		return Integer.parseInt(fromServer);



	}
	
	
	//Getters

	public int getId() {
		return id;
	}

	public int getIdExercice() {
		return idExercice;
	}

	public String getIndication() {
		return indication;
	}

	public String getDescription() {
		return description;
	}

	public int getRecommandation() {
		return recommandation;
	}

	public String getEnonce() {
		return enonce;
	}

	public int getIdAuteur() {
		return idAuteur;
	}

	public String getType() {
		return type;
	}

	public static String getSep() {
		return sep;
	}
	
}
