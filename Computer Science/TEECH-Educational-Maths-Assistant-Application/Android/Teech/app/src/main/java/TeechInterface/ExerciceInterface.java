package TeechInterface;

import java.io.IOException;

public interface ExerciceInterface {
	
	String sep = ClientConnection.sep;

	/**Ajoute un corrige a cet exercice*/
	int addCorrige(String description,
			String indication,
			int recommandation,
			String fileName, // contenu exercice photo encodee ou string latex
			int idAuteur,
			String type)   throws ServerConnectionException, IOException;

	Corrige findCorrige();


}

