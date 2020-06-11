package server;


import java.io.*;
import java.util.Base64;
import tableEntities.*;

public class TeechProtocol {
	
	public static final String sep = "##";
	public static final String arraySep = "x_ARRAY_x";
	public static final String ENDLINE = "x_ENDLINE_x";
	
	public final static String processInput(String input) throws Exception {
		
		String[] args = input.replace(ENDLINE, "\n").split(sep);
		int request = Integer.parseInt(args[0]);
		Utilisateur user;
		Exercice exercice;
		Corrige corrige;
		Matiere matiere;
		String msg = "NOK";
		
		switch (request) {
		
		case 0 :
			return "hello !";
		
			//Case 1 et 2 : Visualisation des profils 
		case 1 : //Création Utilisateur à partir de identifiant 
			user = new Utilisateur(args[1]);
			int id = user.getId();	
			if (user.getMotDePasse().equals(args[2])) msg = user.convertToMsg();
			break ;	
					
	
		case 2: //Création Utilisateur à partir de id
			id = Integer.parseInt(args[1]);
			user = new Utilisateur(id);
			msg = user.convertToMsg() ;	
			break;
			
			//Case 3 : Ajout d'un utilisateur (professeur ou eleve)
		case 3: 		
			user = Utilisateur.add(args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9], args[10]);
			msg =  Integer.toString(user.getId());	
			break;
		
			//Case 4 : Consultation d'un exercice 
		case 4:
			id = Integer.parseInt(args[1]);
			exercice = new Exercice(id);
			msg = exercice.convertToMsg();
			break;
			
			//Case 5 : Ajout de l'enonce d'un exercice
		case 5:
			
			
			System.out.println("op0");
			exercice = Exercice.add(args[1], Integer.parseInt(args[2]), args[3].replace(ENDLINE,"\n"), Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]), args[7]);
			msg= Integer.toString(exercice.getId());
			break;
			
			//Case 6 : Consultation d'un corrige
		case 6:
			id = Integer.parseInt(args[1]);
			corrige = new Corrige(id);
			msg = corrige.convertToMsg();
			break;
			
			//Case 7 : Ajout d'un corrige
		case 7 :
			System.out.println("op0");
			corrige = Corrige.add(Integer.parseInt(args[1]), args[2], args[3], Integer.parseInt(args[4]) , args[5], Integer.parseInt(args[6]), args[7]);
			msg = Integer.toString(corrige.getId());;
			System.out.println("opERR");
			break;
			
			//Case 8 :renvoie d'un corrige de l'exercice 
		case 8 :
			id = Integer.parseInt(args[1]);
			exercice = new Exercice(id);
			corrige = exercice.findCorrige();
			msg = corrige.convertToMsg();
			break;
		
			//Case 9 :ajout d'une matiere:
		case 9 :
			matiere = Matiere.add(args[1], args[2], Integer.parseInt(args[3]));
			msg = Integer.toString(matiere.getId());
			break;
			
			//Case 10 : consultation d'une matiere
		case 10 :
			id = Integer.parseInt(args[1]);
			matiere = new Matiere(id);
			msg = matiere.convertToMsg();
			break;
			
			//Case 11 : getAll matiere
		case 11 :
			msg = Matiere.convertArrayToMsg(Matiere.getAll());
			break;
			
			//Case 11 : ajout d'une matiere a un exercice
		case 12 : 
			id = Integer.parseInt(args[1]);
			exercice = new Exercice(id);
			exercice.addMatiere(Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
			msg = "OK";
			break;
			
		default :
			return "unknown case";
		
						
		}
		
		return msg.replace("\n", ENDLINE);
		
	} 
	
	
		
	/**Encode imageByteArry en une chaine de characteres
	 * @return String*/
	public static String encodeImage(byte[] imageByteArray) {
        return Base64.getEncoder().encodeToString(imageByteArray);
    }
	
	public static byte[] decodeImage(String imageDataString) {
        return Base64.getDecoder().decode(imageDataString);
    }
		
	/** Convertit le fichier imageName en tableau de byte
	 * @return byte[]*/
	public static byte[] extractBytes (String ImageName) throws IOException {
		 // open image
		 File imgPath = new File(ImageName);
		 FileInputStream fis= new FileInputStream(imgPath);
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 byte[] buf = new byte[1024];
		
		 for(int i; (i= fis.read(buf))!=-1;) {
				 baos.write(buf, 0, i);
			 }
		 
		 
		 byte[] bytes = baos.toByteArray();
		 fis.close();
		 baos.close();
		 return bytes;
		
		}
	

	public static String extractText (String fileName) throws IOException {
		File textFile = new File(fileName);
		String text = "";
		FileReader fr = new FileReader(textFile);
		BufferedReader br = new BufferedReader(fr);
		String line;
		
		while ((line = br.readLine())!=null) {
			text+=line+"[ENDLINE]"; // ++++ sera interpreter comme un retour a la ligne
		}
		
		fr.close();
		br.close();
		return text;
	}
}
