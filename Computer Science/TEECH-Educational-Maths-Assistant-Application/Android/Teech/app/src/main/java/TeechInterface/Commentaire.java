package TeechInterface;

import java.io.IOException;
import java.util.ArrayList;

public class Commentaire {
	
	private int id;
	private int idEmetteur;
	private String contenu; // texte du commentaire
	private int idContenu;
	private int appreciation; // somme des likes (+1) et des dislikes (-1) mis sur le commentable
	private String date;
	private int reponseA; //reponseA vous 0 si le commentaire n'est pas une reponse
	
	public static String sep = ClientConnection.sep;

	
	public Commentaire(int id) throws IOException {
		this.id = id;
		String fromServer = ClientConnection.sentToServer("21"+sep+id);
		String[] data =fromServer.split(sep);
		idEmetteur = Integer.parseInt(data[1]);
		contenu = data[2];
		idContenu = Integer.parseInt(data[3]);
		appreciation = Integer.parseInt(data[4]);
		date = data[5];
		reponseA = Integer.parseInt(data[6]);
		
	}
	
	private Commentaire(String msg) {
		String[] data =msg.split(ClientConnection.sep);
		id = Integer.parseInt(data[0]);
		idEmetteur =  Integer.parseInt(data[1]);
		contenu = data[2];
		idContenu = Integer.parseInt(data[3]);
		appreciation = Integer.parseInt(data[4]);
		date = data[5];
		reponseA = Integer.parseInt(data[6]);
	}
	
	public static Commentaire add(int idEmetteur, String contenu, int idContenu, int reponseA) throws IOException { 
		String fromServer = ClientConnection.sentToServer("22"+sep+idEmetteur+sep+contenu+sep+idContenu+sep+reponseA);
		return new Commentaire(fromServer);
	}
	
	/**Met a jour dans la base de donnee l'appreciation de l'exercice, renvoie la nouvelle appreciation
	 * @return la nouvelle valeur de l'appreciation*/
	public int updateAppreciation(int upOrDown) throws IOException {
		String fromServer = ClientConnection.sentToServer("23"+sep+id+sep+upOrDown);
		return Integer.parseInt(fromServer);
		
	}
	
	public static ArrayList<Commentaire> getAllCommentsOn(int id) throws IOException {
		String arraySep = ClientConnection.arraySep;
		String fromServer =  ClientConnection.sentToServer("24"+sep+id);//idMatiereMere -> mettre par defaut 0.
		if(fromServer.equals("empty")){
			return new ArrayList<>();
		}
		String[] data = fromServer.split(arraySep);
		ArrayList<Commentaire> array = new ArrayList<>();
		for (String aData : data) {
			array.add(new Commentaire(aData));
		}
		return array;
	}
	
	//Getters
	
	public int getId() {
		return id;
	}


	public int getIdEmetteur() {
		return idEmetteur;
	}


	public String getContenu() {
		return contenu;
	}


	public int getIdContenu() {
		return idContenu;
	}


	public int getAppreciation() {
		return appreciation;
	}


	public String getDate() {
		return date;
	}


	public int getReponseA() {
		return reponseA;
	}


	public String getSep() {
		return sep;
	}
	
	
	
}
