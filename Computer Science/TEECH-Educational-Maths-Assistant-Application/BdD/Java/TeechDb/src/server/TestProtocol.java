package server;


import java.io.*;
import java.util.Base64;
import tableEntities.Utilisateur;

public class TestProtocol {
	
	
	private PrintWriter outToClient;
	private BufferedReader inFromClient; 
	
	public TestProtocol(BufferedReader in, PrintWriter out) {
		
		try {
			
			this.outToClient = out;
			}
		catch (Exception e ) {System.err.println(e);}
	}
	
	public String processInput(String input) throws Exception {
		String[] args = input.split("/");
		int request = Integer.parseInt(args[0]);
		Utilisateur user;
		
		switch (request) {
		
		case 0 :
			
			return "hello !";
		
		case 1 : 
			user = new Utilisateur(args[1]);
			String mdp = user.getMotDePasse();
			String identifiant = user.getIdentifiant();
			int id = user.getId();
			
			return "id : " + id + " - identifiant : " + identifiant + " - mdp : " + mdp ;	
					
		case 2 :
			user = new Utilisateur(args[1]);
			byte[] data = extractBytes(user.getPhotoDeProfil());
			outToClient.println("image : " + user.getIdentifiant() );
			
			return encodeImage(data);
		
						
		}
		
		return "stop";
	} 
	
	
		
	/**Encode imageByteArry en une chaine de characteres
	 * @return String*/
	public static String encodeImage(byte[] imageByteArray) {
        return Base64.getEncoder().encodeToString(imageByteArray);
    }
		
	/** Convertit le fichier imageName en tableau de byte
	 * @return byte[]*/
	public byte[] extractBytes (String ImageName) throws IOException {
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
}
