package TeechInterface;

import java.io.IOException;
import java.util.ArrayList;

public interface MatiereInterface {
	
	String sep = ClientConnection.sep;
	String arraySep = ClientConnection.arraySep;
	
	public static int add(String nom,
			String niveau,
			int idMatiereMere) throws IOException {
		
		String fromServer =  ClientConnection.sentToServer("9"+sep+nom+sep+niveau+sep+idMatiereMere);//idMatiereMere -> mettre par defaut 0.
		return Integer.parseInt(fromServer);
	};
	
	/** Renvoie toutes les matieres presentes dans la base de donnees*
	 * @return  ArrayList<Matiere>
	 * @throws IOException*/
	public static ArrayList<Matiere> getAll() throws IOException {
		
		String fromServer =  ClientConnection.sentToServer("11");//idMatiereMere -> mettre par defaut 0.
		String[] data = fromServer.split(arraySep);
		int n = data.length;
		System.out.println("getAll n : " + n);
		ArrayList<Matiere> array = new ArrayList<Matiere>();
		for (int i = 0 ; i<n ; i++) {
			array.add(new Matiere(data[i]));
		}
		return array;
	}
	
	public String getNom();
	
	public String getNiveau();
	
	public int getId();
	
	
}
