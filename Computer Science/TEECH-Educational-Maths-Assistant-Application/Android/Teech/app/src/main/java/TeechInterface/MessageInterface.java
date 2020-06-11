package TeechInterface;


import java.io.IOException;

public interface MessageInterface {

    String getDate();

    String getContenu();

    Utilisateur getEmetteur() throws IOException;

    Utilisateur getDestinataire() throws IOException;


}
