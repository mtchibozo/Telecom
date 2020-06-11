package TeechInterface;

import java.io.IOException;

public interface UserInterface {
	
	public static String sep = ClientConnection.sep;
	
	
	public static UserInterface login(String identifiant, String mdp) throws Exception{
	
		return Utilisateur.login(identifiant, mdp);
	};
	
	public static int add(
			String identifiant,
			String motDePasse,
			byte[] photoDeProfil,
			String email,
			String statut, 
			String niveau, 
			String description,
			String etablissement,
			String classe,
			String ville) throws ServerConnectionException, IOException {
		
		String img;
		if (photoDeProfil == null) {
			img = "default";
		}
		else {
			img = ClientConnection.encodeImage(photoDeProfil);
		}
		String fromServer = ClientConnection.sentToServer("3"+sep+identifiant+sep+motDePasse+sep+img+sep+email+sep+statut+sep+niveau+sep+description+sep+etablissement+sep+classe+sep+ville);
		if (fromServer == "NOK") {
			throw new ServerConnectionException("Add user failed");
		}
		else {
			return Integer.parseInt(fromServer);
		}

	};
	
	public boolean isIndentifiantIn(String identifiant);
	
    public boolean isEmailIn(String email);
    //Renvoie true si l'email est present dans la base de donnees.
    
    public String getIdentifiant();
    
    public byte[] getPhotoDeProfil();
    
    public String getEmail();
    
    public String getStatut();
    
    public String getNiveau();
    
    public String getDescription();
    
    public String getClasse();
    
    public String getVille();
}
