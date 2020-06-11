package TeechInterface;

import java.io.IOException;

public class Utilisateur implements UserInterface {

	private int id;
	private String identifiant;
	private String motDePasse;
	private byte[] photoDeProfil;
	private String email;
	private String statut; 
	private String niveau; 
	private String description;
	private String etablissement;
	private String classe;
	private String ville;
	private static String sep = ClientConnection.sep;
	
	public Utilisateur(int id) throws IOException {
		
		String fromServer = ClientConnection.sentToServer("2"+sep+id);
		String[] data =fromServer.split("##");
		System.out.println(fromServer);
		System.out.println(data[0]+"-"+data[1]+"-"+data[2]);
		
		this.id =id;
		identifiant = data[1];
		motDePasse = data[2];
		photoDeProfil = ClientConnection.decodeImage(data[3]);
		email = data[4];
		statut = data[5];
		niveau = data[6];
		description = data[7];
		etablissement = data[8];
		ville = data[9];
				
		
	}
	
	public Utilisateur(String msg) {
		
		String[] data =msg.split("##");
		id = Integer.parseInt(data[0]);
		identifiant = data[1];
		motDePasse = data[2];
		photoDeProfil = ClientConnection.decodeImage(data[3]);
		email = data[4];
		statut = data[5];
		niveau = data[6];
		description = data[7];
		etablissement = data[8];
		ville = data[9];
				
		
	}
	
	
	public static Utilisateur login(String identifiant, String mDp) throws IOException, ServerConnectionException {
		
		String fromServer = ClientConnection.sentToServer("1"+sep+identifiant+sep+mDp);
		String[] data = fromServer.split("##");
		System.out.println(fromServer);
		System.out.println(data[0]+"-"+data[1]+"-"+data[2]);
		if (fromServer.equals("NOK")) throw new ServerConnectionException("Wrong Password");
		return new Utilisateur(fromServer);
				
		
	}
	
	
	
	

	@Override
	public boolean isEmailIn(String email) {
		// A IMPLEMENTER
		return false;
	}



	public int getId() {
		return id;
	}



	public String getIdentifiant() {
		return identifiant;
	}



	public String getMotDePasse() {
		return motDePasse;
	}



	public byte[] getPhotoDeProfil() {
		return photoDeProfil;
	}



	public String getEmail() {
		return email;
	}



	public String getStatut() {
		return statut;
	}



	public String getNiveau() {
		return niveau;
	}



	public String getDescription() {
		return description;
	}



	public String getEtablissement() {
		return etablissement;
	}



	public String getClasse() {
		return classe;
	}



	public String getVille() {
		return ville;
	}



	@Override
	public boolean isIndentifiantIn(String identifiant) {
		// TODO Auto-generated method stub
		return false;
	}
	
	

	

}
