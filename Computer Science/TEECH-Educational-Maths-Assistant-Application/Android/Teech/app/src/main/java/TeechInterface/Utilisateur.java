package TeechInterface;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.IOException;
import java.util.ArrayList;

public class Utilisateur implements UserInterface, Parcelable {

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
		String[] data =fromServer.split(sep);
		this.id=id;
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

		String[] data =msg.split(sep);
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


	public static int add( String identifiant,
					String motDePasse,
					byte[] photoDeProfil,
					String email,
					String statut,
					String niveau,
					String description,
					String etablissement,
					String ville,
					String classe) throws ServerConnectionException, IOException {

		String img;
		String fromServer;
		if (photoDeProfil == null) {
			img = "default";
		}
		else {
			img = ClientConnection.encodeImage(photoDeProfil);
		}

		fromServer = ClientConnection.sentToServer("3"+sep+identifiant+sep+motDePasse+sep+img+sep+email+sep+statut+sep+niveau+sep+description+sep+etablissement+sep+ville+sep+classe);
		if (fromServer.equals("NOK")) {
			throw new ServerConnectionException("Add user failed");
		}else{
			return Integer.parseInt(fromServer);
		}
	}



    public int addUtilisateur() throws ServerConnectionException, IOException{
			return Utilisateur.add(identifiant, motDePasse, photoDeProfil, email, statut, niveau, description, etablissement, classe, ville);
	}



	public static Utilisateur login(String identifiant, String mdp) throws IOException, ServerConnectionException {
		String fromServer = ClientConnection.sentToServer("1"+sep+identifiant+sep+mdp);
		if (fromServer.equals("NOK")) throw new ServerConnectionException("Wrong Password");
		return new Utilisateur(fromServer);
	}


	/**Renvoie un exercice de la matiere mat de la difficulte recherchee correspondant au profil de l'eleve
	 * @return Exercice*/
	public Exercice findExercice(Matiere mat, int difficulte) throws IOException {
		String fromServer = ClientConnection.sentToServer("13"+sep+id+sep+mat.getId()+sep+difficulte);
		return new Exercice(fromServer);
	}



	/**Envoie a la base de donnees recuellies lorsque l'eleve a fait l'exercice exo
	 * @return int
	 * Entier sans vraiment d'importance (id de l'evalution ajoutee dans la base)*/
	public int addEvaluation(Exercice exo,
			int evaluation,
			int recommandation,
			int difficulte ) throws IOException {
		String fromServer = ClientConnection.sentToServer("14"+sep+id+sep+exo.getId()+sep+evaluation+sep+recommandation+sep+difficulte);
		return Integer.parseInt(fromServer);
	}


	public static int addEval(int idUsr, int idExo, int evaluation, int recommandation, int difficulte) throws IOException {
		String fromServer = ClientConnection.sentToServer("14" + sep + idUsr + sep + idExo + sep + evaluation + sep + recommandation + sep + difficulte);
		if (!fromServer.equals("NOK")) {
			return Integer.parseInt(fromServer);
		} else {return -1;}
	}


	public static boolean isEmailIn(String email) {
		//TODO: faire une fonction
		return false;
	}

	public static boolean isIdentifiantIn(String identifiant) throws IOException {
		String serverBoolean = ClientConnection.sentToServer("19"+sep+identifiant);
		return serverBoolean.equals("TRUE");
	}

	@Override
	public String getIdentifiant() {return identifiant;}
	public int getId(){return id;}
    public void setId(int id){this.id = id;}
    @Override
	public byte[] getPhotoDeProfil() {return photoDeProfil;}


	public static void modifyPhotoDeProfil(String encodedImage, int id) throws IOException, ServerConnectionException {
		String fromServer = ClientConnection.sentToServer("20"+sep+id+sep+encodedImage);
		if (fromServer.equals("NOK")) throw new ServerConnectionException("photoDeProfil modification failed");
	}

	public void sentMessageTo(String contenu, String identifiantDestinataire) throws IOException, ServerConnectionException {
		if (isIdentifiantIn(identifiantDestinataire)) {
			String fromServer = ClientConnection.sentToServer("15"+sep+id+sep+contenu+sep+identifiantDestinataire);
			//return new Message(fromServer);
		}
		else throw new ServerConnectionException("identifiant destinataire inconnu");
	}

	@Override
	public String getEmail() {
		return email;
	}

	@Override
	public String getStatut() {
		return statut;
	}

	@Override
	public String getNiveau() {
		return niveau;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getClasse() {
		return classe;
	}

	public String getEtablissement(){
		return etablissement;
	}

	@Override
	public String getVille(){
		return ville;
	}




	
	
	public ArrayList<Message> getReceivedMessages() throws IOException {
		ArrayList<Message> array = new ArrayList<>();
		String fromServer = ClientConnection.sentToServer("16"+sep+id);
		if (fromServer.equals("empty")) return null;
		String[] data = fromServer.split(ClientConnection.arraySep);
		for (String aData : data) {
			array.add(new Message(aData));
		}
		return array;
	}
	
	
	public ArrayList<Message> getSentMessages() throws IOException {
		ArrayList<Message> array = new ArrayList<>();
		String fromServer = ClientConnection.sentToServer("17"+sep+id);
		if (fromServer.equals("empty")) return null;
		String[] data = fromServer.split(ClientConnection.arraySep);
		for (String aData : data) {
			array.add(new Message(aData));
		}
		return array;
		
		
	}
	
	public static ArrayList<String> getExercicesDone(int id) throws IOException {
		String fromServer = ClientConnection.sentToServer(27+sep+id);
		if (fromServer.equals("empty")) return null;
		String[] data = fromServer.split(ClientConnection.arraySep);
		ArrayList<String> array = new ArrayList<>();
		for (String aData : data) {
			String[] donnees = aData.split(sep);
			array.add(donnees[1] + " du " + donnees[5]);
		}
		return array;
	}



	//constructeur qui permet de créer un nouvel utilisateur
	public Utilisateur(String identifiant, String statut, String email, String password, String etab, String ville, String classe){
		this.id = 0;
		this.identifiant = identifiant;
		this.photoDeProfil = null;
		this.statut=statut;
		this.email=email;
		this.motDePasse=password;
		this.etablissement=etab;
		this.ville=ville;
		this.classe=classe;
		this.niveau = "n";
	}


	@Override
	public int describeContents() {
		return 0;
	}


	protected Utilisateur(Parcel in) {
		identifiant = in.readString();
		motDePasse = in.readString();
		email = in.readString();
		statut = in.readString();
		niveau = in.readString();
		description = in.readString();
		etablissement = in.readString();
		classe = in.readString();
		ville = in.readString();
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(identifiant);
		dest.writeString(motDePasse);
		dest.writeString(email);
		dest.writeString(statut);
		dest.writeString(niveau);
		dest.writeString(description);
		dest.writeString(etablissement);
		dest.writeString(classe);
		dest.writeString(ville);
	}

	public static final Parcelable.Creator<Utilisateur> CREATOR = new Parcelable.Creator<Utilisateur>() {
		@Override
		public Utilisateur createFromParcel(Parcel in) {
			return new Utilisateur(in);
		}

		@Override
		public Utilisateur[] newArray(int size) {
			return new Utilisateur[size];
		}
	};

}
