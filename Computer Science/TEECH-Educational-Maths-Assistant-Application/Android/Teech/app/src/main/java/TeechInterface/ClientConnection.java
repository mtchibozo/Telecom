package TeechInterface;

import java.io.*;
import java.net.*;
import android.util.Base64;
import android.util.Log;

public class ClientConnection implements Closeable {
	
	private static String serverIP = "137.194.92.85";
	private static int portNumber = 8080;
	public static String sep = "##";
	static final String arraySep = "x_ARRAY_x";
	private static final String ENDLINE = "x_ENDLINE_x";

	private Socket androidSocket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private InputStreamReader isr = null;

	public static void setServerIP(String ip) {
		serverIP = ip;
	}

	public static void setPortNumber(int pn) {
		portNumber = pn;
	}

	/** Envoie un message au serveur
	 * @return String
	 * @throws IOException
	 * Si il est impossible de lire les messages entrant dans la socket*/
	 static String sentToServer(String msg) throws IOException {
		ClientConnection cc = new ClientConnection();
		cc.out.println(msg.replace("\n", ENDLINE));
		String answer = cc.in.readLine().replace(ENDLINE, "\n");
		cc.close();
		return answer.replace(ENDLINE,"\n");
	}

	/**Encode imageByteArry en une chaine de characteres
	 * @return String*/
	public static String encodeImage(byte[] imageByteArray) {
        return Base64.encodeToString(imageByteArray, Base64.NO_WRAP);
    }

	public static byte[] decodeImage(String imageDataString) {
        return Base64.decode(imageDataString, Base64.DEFAULT);
    }

	public ClientConnection() {	
		try {
			androidSocket = new Socket(serverIP, portNumber);
			out = new PrintWriter(androidSocket.getOutputStream(), true);
			isr = new InputStreamReader(androidSocket.getInputStream());
			in = new BufferedReader(isr);
        }
        catch(ConnectException e ) {
			Log.d("login", "Server : " + serverIP + " at port " + portNumber + "Unreachable");
        }catch(Exception e ) {
        	Log.d("login", "Error when trying to connect to : " + serverIP + " at port " + portNumber);
			Log.d("login", Log.getStackTraceString(e));
        }
	}

    @Override
	public void close(){
		try {
			if (out != null) out.close();
			if (in != null) in.close();
			if (isr != null) isr.close();
			if (androidSocket != null) androidSocket.close();
		} catch(IOException e){
			Log.d("login", Log.getStackTraceString(e));
		}

	}

}


