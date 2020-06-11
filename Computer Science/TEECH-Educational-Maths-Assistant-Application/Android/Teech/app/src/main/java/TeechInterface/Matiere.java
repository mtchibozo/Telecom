package TeechInterface;

import java.io.IOException;
import java.util.ArrayList;

public class Matiere implements MatiereInterface {
	
	private static String sep = ClientConnection.sep;
	private int id;
	private String nom; //nom de la matiere
	private String niveau; //exemple : sup, spe
	private int idMatiereMere;
	
	public Matiere(int id) throws IOException {
		String fromServer = ClientConnection.sentToServer("10"+sep+id);
		String[] data = fromServer.split(sep);
		this.id = id;
		nom = data[1];
		niveau = data[2];
		idMatiereMere=0;
	}
	
	private Matiere(String msg) {
		String[] data = msg.split(sep);
		id = Integer.parseInt(data[0]);
		nom = data[1];
		niveau = data[2];
	}


	public static int add(String nom, String niveau, int idMatiereMere) throws IOException {
		String fromServer =  ClientConnection.sentToServer("9"+sep+nom+sep+niveau+sep+idMatiereMere);//idMatiereMere -> mettre par defaut 0.
		return Integer.parseInt(fromServer);
	}

	/** Renvoie toutes les matieres presentes dans la base de donnees*
	 * @return  ArrayList<Matiere>*/
	public static ArrayList<Matiere> getAll() throws IOException {

		String fromServer =  ClientConnection.sentToServer("11");//idMatiereMere -> mettre par defaut 0.
		String[] data = fromServer.split(arraySep);
		ArrayList<Matiere> array = new ArrayList<>();
		for (String aData : data) {
			array.add(new Matiere(aData));
		}
		return array;
	}

	public int getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public String getNiveau() {
		return niveau;
	}


	public int getIdMatiereMere() {
		return idMatiereMere;
	}


	@Override
	public String toString(){
		return nom + " " + niveau;
	}

	
	

}
