package TeechInterface;


import java.io.IOException;
import java.util.ArrayList;


public class Message implements MessageInterface{
    private int emetteur;
    private  int destinataire;
    private String contenu;
    private final String date;

    Message(String msg) throws NumberFormatException, IOException {

        String[] data =msg.split(ClientConnection.sep);
        emetteur = Integer.parseInt(data[0]);
        contenu = data[1];
        destinataire = Integer.parseInt(data[2]);
        date = data[3];
    }


    public String getContenu() {
        return contenu;
    }
    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Utilisateur getEmetteur() throws IOException{
        return new Utilisateur(emetteur);
    }

    public Utilisateur getDestinataire() throws IOException {
        return new Utilisateur(destinataire);
    }

    @Override
    public String getDate() {

        return date;
    }

	public static ArrayList<Message> getDiscussion(UserInterface usr1, UserInterface usr2) throws IOException {

		String fromServer = ClientConnection.sentToServer(18+ClientConnection.sep+usr1.getId()+ClientConnection.sep+usr2.getId());
		String[] data = fromServer.split(ClientConnection.arraySep);
		if (fromServer.equals("empty")) return null;
		ArrayList<Message> array = new ArrayList<>();
        for (String aData : data) {
            array.add(new Message(aData));
        }

		return array;
	}
}
