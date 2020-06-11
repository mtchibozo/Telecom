package TeechInterface;


import java.io.IOException;
import java.util.ArrayList;

public interface UserInterface {

    void sentMessageTo(String contenu, String identifiantDestinataire) throws IOException, ServerConnectionException;

    String getIdentifiant();

    int getId();

    byte[] getPhotoDeProfil();

    String getEmail();

    String getStatut();

    String getNiveau();

    String getDescription();

    String getClasse();

    String getVille();

    ArrayList<Message> getReceivedMessages()  throws IOException;

    ArrayList<Message> getSentMessages()  throws IOException;}
