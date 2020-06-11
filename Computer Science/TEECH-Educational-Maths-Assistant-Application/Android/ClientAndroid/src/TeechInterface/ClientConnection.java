package TeechInterface;

import java.io.*;
import java.net.*;
import java.util.Base64;

public class ClientConnection implements Closeable {
	
	private static String serverIP = "0.0.0.0"; 
	private static int portNumber = 8080;
	public static final String sep = "##";
	public static final String arraySep = "x_ARRAY_x";
	public static final String ENDLINE = "x_ENDLINE_x";
	
	Socket androidSocket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	InputStreamReader isr = null;
	
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
	public static String sentToServer(String msg) throws IOException {
		
		ClientConnection cc = new ClientConnection();
		cc.out.println(msg.replace("\n", ENDLINE));
		System.out.println("***************************Sent Message***************************");
		System.out.println(msg);
		String answer = cc.in.readLine().replace(ENDLINE, "\n");
		System.out.println("*************************Received Message*************************");
		System.out.println(answer);
		cc.close();
		return answer;
	}
	
	/**Encode imageByteArry en une chaine de characteres
	 * @return String*/
	public static String encodeImage(byte[] imageByteArray) {
        return Base64.getEncoder().encodeToString(imageByteArray);
    }
	
	public static byte[] decodeImage(String imageDataString) {
        return Base64.getDecoder().decode(imageDataString);
    }
	
	public static byte[] extractBytes (String imageName) throws IOException {
		 // open image
		 File imgPath = new File(imageName);
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
	
	public ClientConnection() {	
		
		try 
        {
			androidSocket = new Socket(serverIP, portNumber);
			out = new PrintWriter(androidSocket.getOutputStream(), true);
			isr = new InputStreamReader(androidSocket.getInputStream()); 
			in = new BufferedReader(isr);      	
        }
        
		catch (ConnectException e) {
			System.out.println("Server : " + serverIP + " at port " + portNumber + " unreachable");
		}
		
        catch(Exception e ) {
        	System.out.println("Error "+ e +" when trying to connect to : " + serverIP + " at port " + portNumber); 
        	e.printStackTrace();
        }
	}

	@Override
	public void close() {
		
		try {
			if (out!= null) out.close();
			if (in!= null) in.close();
			if (isr != null) isr.close();
			if (androidSocket!= null) androidSocket.close();
			System.out.println("*** Disconnected ***");
		}
		
		catch (IOException e) {
			e.printStackTrace();
			}
		
	}
	

}
	
	


