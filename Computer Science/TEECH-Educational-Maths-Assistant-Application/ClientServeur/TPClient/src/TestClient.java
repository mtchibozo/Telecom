import java.io.*;
import java.net.*;
import java.util.Base64;

public class TestClient {
	
	public static void main (String[] args) throws IOException {
 
        String hostName = "0.0.0.0";
        int portNumber = 43;
        
        try (
        		Socket testSocket = new Socket(hostName, portNumber);
        	
        		PrintWriter out = new PrintWriter(testSocket.getOutputStream(), true);
        		InputStreamReader isr = new InputStreamReader(testSocket.getInputStream());      		
        		BufferedReader in = new BufferedReader(isr);
        		DataInputStream dis = new DataInputStream(testSocket.getInputStream());
        		
        	)
        {
        	String fromServer;
        	String fromUser;
        	String[] arg;
        	BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        	int compt = 0;
        	
        	while ((fromServer = in.readLine())!= null && compt<15) {
        		
        		
        		System.out.println("Server["+compt+"] : " + fromServer);
        		compt++;
        		
        		arg = fromServer.split(" ");
        		
        		if (arg[0].equals("image")) {
        			
        			byte[] byteImage = decodeImage(in.readLine());
        			new ClientFrame(byteImage);
        			
        		}
                
        		if (fromServer.equals("goodbye !")) break;
        		
        		if (!testSocket.isBound()) {
        			System.out.println("*** Socket unbound ***");
        			break; 
        		}
        		
        		System.out.print("In["+compt+"] : " );
                fromUser = stdIn.readLine();
                
                if (fromUser == null || arg[0].equals("stop")) break;
                else {
                    System.out.println("Client["+compt+"] : " + fromUser);
                    out.println(fromUser);
                }
        	
               
        	}
        	
        	System.out.println("*** Disconnected ***");
        	
        }
        
        catch(Exception e ) {
        	System.err.println("Error when trying to connect to : " + hostName + " at port " + portNumber); 
        	System.err.println(e.getMessage());
        	e.printStackTrace();
        }
	}
	
	public static byte[] decodeImage(String imageDataString) {
        return Base64.getDecoder().decode(imageDataString);
    }

}
