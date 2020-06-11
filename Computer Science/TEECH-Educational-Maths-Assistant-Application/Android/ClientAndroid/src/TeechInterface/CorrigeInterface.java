package TeechInterface;

import java.io.IOException;

public interface CorrigeInterface {
	
	public static String sep = ClientConnection.sep;
	
	public static int add(
			int idExercice,
			String description, 
			String indication,
			int recommendation,
			String fileName, // contenu exercice photo encodee ou string latex
			int idAuteur,
			String type)   throws ServerConnectionException, IOException {
		
		String enonce;
		
		if (type.equals("latex")) enonce = ClientConnection.extractText(fileName);
		
		else {
			byte[] img = ClientConnection.extractBytes(fileName);
			enonce = ClientConnection.encodeImage(img);
		}

		String fromServer = ClientConnection.sentToServer("7"+sep+idExercice+sep+description+sep+indication+sep+recommendation+sep+enonce+sep+idAuteur+sep+type);
		
		if (fromServer == "NOK") {
			throw new ServerConnectionException("Add exercice failed");
		}
		
		return Integer.parseInt(fromServer);
	
	

	}
	public int getId();

}
