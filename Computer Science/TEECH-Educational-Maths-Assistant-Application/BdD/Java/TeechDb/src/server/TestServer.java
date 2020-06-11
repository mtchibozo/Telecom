package server;
import java.io.*;
import java.net.*;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;

public class TestServer {
	
	public static final int min = 60000;
	public static final int sec = 1000;
	public static JFrame window;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		int portNumber = 43;
		String ip = InetAddress.getLocalHost().getHostAddress().toString();
		ServerSocket serverSocket = null;
		Socket clientSocket = null;
		PrintWriter out = null;
		InputStreamReader isr = null;
		BufferedReader in = null;
		DataOutputStream dos = null;
		
		try  {	
			
			serverSocket = new ServerSocket(portNumber); 
			portNumber =serverSocket.getLocalPort();
			System.out.println("Server opened : address : " + ip + " - port : " + portNumber);
			serverSocket.setSoTimeout(3*min);
			clientSocket = serverSocket.accept();
			System.out.println("Client connected : address : " + clientSocket.getInetAddress() + " - port : " + serverSocket.getLocalPort());
			
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			isr = new InputStreamReader(clientSocket.getInputStream());
			dos = new DataOutputStream(clientSocket.getOutputStream()); 
			in = new BufferedReader(isr);
			
			String inputLine, outputLine;
			TestProtocol tp = new TestProtocol(in, out); 
			
			out.println("Connection to : " + ip + " succeeded");
			
			
			while ((inputLine = in.readLine())!=null) {
				
				
				try {
					
					if (inputLine.equals("stop")) {break;}
					else {
						outputLine = tp.processInput(inputLine);
						if (outputLine.equals("stop")) {break;}
						out.println(outputLine);
						
					}
				}
				
				catch(SQLException e) {
					out.println(e);
				}
				
				catch (Exception e) {
					out.println("stop " + e);
					break;
				}
			}
			
			out.println("goodbye !");
			System.out.println("*** Terminated ***");
		}
		
		catch (SocketTimeoutException e) {
			System.out.println("***Timeout : " + (serverSocket.getSoTimeout()/sec) + " sec ***");
		}
		
		catch (Exception e){
			System.out.println("Exception caught when tryng to listen port "+ portNumber +
					" or listenning for a connection : " + e);
		}
		
		
		finally {
			if (serverSocket != null) serverSocket.close();
			if (clientSocket != null) clientSocket.close();
			if (isr != null) isr.close();
			if (out != null) out.close();
			if (in != null) in.close();
			if (dos != null) dos.close();
			System.out.println("closed - port : " + portNumber);
			
		}
		

	}

}
