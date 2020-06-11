package TeechInterface;

import java.io.IOException;

public interface ExerciceInterface {
	
	public static String sep = ClientConnection.sep;

	/**ajoute l'execice a la base de donnee*/
	public static int add(String description,
			int recommandation,
			String fileName, //fichier contenant l'enonce de l'exercice latex ou png
			int difficulteAuteur,
			int idAuteur,
			int dureeConseillee,
			String type) throws ServerConnectionException, IOException {
		
		String enonce;
		
		if (type.equals("latex")) enonce = ClientConnection.extractText(fileName);
		else {
			byte[] img = ClientConnection.extractBytes(fileName);
			enonce = ClientConnection.encodeImage(img);
		}

		String fromServer = ClientConnection.sentToServer("5"+sep+description+sep+recommandation+sep+enonce+sep+difficulteAuteur+sep+idAuteur+sep+dureeConseillee+sep+type);
		
		if (fromServer == "NOK") {
			throw new ServerConnectionException("Add exercice failed");
		}
		
		return Integer.parseInt(fromServer);


	}
	
	/**Ajoute un corrige a cet exercice*/
	public int addCorrige(String description, 
			String indication,
			int recommandation,
			String fileName, // contenu exercice photo encodee ou string latex
			int idAuteur,
			String type)   throws ServerConnectionException, IOException;
	
	public Corrige findCorrige();
	
}
