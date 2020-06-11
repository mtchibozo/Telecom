package TeechInterface;

import java.io.IOException;

public class Matiere implements MatiereInterface {
	
	String sep = ClientConnection.sep;
	private int id;
	private String nom; //nom de la matiere
	private String niveau; //exemple : sup, spe
	private int idMatiereMere = 0;
	
	public Matiere(int id) throws IOException {
		String fromServer = ClientConnection.sentToServer("10"+sep+id);
		String[] data = fromServer.split("##");
		id = Integer.parseInt(data[0]);
		nom = data[1];
		niveau = data[2];
	}
	
	public Matiere(String msg) {
		String[] data = msg.split("##");
		id = Integer.parseInt(data[0]);
		nom = data[1];
		niveau = data[2];
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

	
	

}
