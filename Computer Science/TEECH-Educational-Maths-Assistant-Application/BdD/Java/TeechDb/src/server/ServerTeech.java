package server;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

import bdd.DatabaseConnection;



public class ServerTeech extends Thread {
	
	public static final int min = 60000;
	public static final int sec = 1000;
	private int timeout = 3*min;
	private ServerSocket serverSocket = null;
	
	
	
	public ServerTeech(int timeout) {
		this.timeout= timeout;
	}
	
	
	public void run() {
		
		int portNumber = 8080;
		String ip = "0.0.0.0";
		Socket clientSocket = null;
		PrintWriter out = null;
		InputStreamReader isr = null;
		BufferedReader in = null;
		DataOutputStream dos = null;
		
		try  {	
			
			ip = InetAddress.getLocalHost().getHostAddress().toString();
			serverSocket = new ServerSocket(portNumber); 
			portNumber =serverSocket.getLocalPort();
			System.out.println("Server opened : address : " + ip + " - port : " + portNumber + "\n");
			serverSocket.setSoTimeout(timeout);
			DatabaseConnection.open();
			String inputLine, outputLine;
			
			while (true) {
				
				
				
				try {
					clientSocket = serverSocket.accept();
				}
				
				catch (SocketException e){
					System.out.println("***Server Manually Interrupted***");
					break;
				}
				
				isr = new InputStreamReader(clientSocket.getInputStream());
				dos = new DataOutputStream(clientSocket.getOutputStream()); 
				in = new BufferedReader(isr);
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				System.out.println("Client connected : address : " + clientSocket.getInetAddress() + " - port : " + serverSocket.getLocalPort());
				
				inputLine = in.readLine();
				
				System.out.println("Client [ip :"+clientSocket.getInetAddress()+"] request : " + inputLine );
				
				try {
					if(inputLine != null) {
						outputLine = TeechProtocol.processInput(inputLine);
						out.println(outputLine);
						System.out.println("   -> request successed \n");
						//System.out.println("Response: " + outputLine + "\n");
					}
				
				}
				
				catch (SQLException e) {
					out.println("NOK");
					System.out.println("   -> request failed : " + e);
				}
				
				catch (Exception e) {
					out.println("NOK");
					System.out.println("   -> request failed : " + e);
					e.printStackTrace();
				}
				
				finally {
					
					clientSocket.close();
					
				}
			}
			
			
		}
		
		
		
		catch (SocketTimeoutException e) {
			System.out.println("***Timeout : " + (timeout/sec) + " sec ***");
		}
		
		
		
		catch (Exception e){
			e.printStackTrace();
			System.out.println("Exception caught when tryng to listen port "+ portNumber +
					" or listenning for a connection : " + e);
		}
		
		
		finally {
			
			try {
			
				if (serverSocket != null) serverSocket.close();
				if (clientSocket != null) clientSocket.close();
				if (isr != null) isr.close();
				if (out != null) out.close();
				if (in != null) in.close();
				if (dos != null) dos.close();
				DatabaseConnection.close();
				System.out.println("closed - port : " + portNumber);
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("*** Terminated ***");
			
		}
		
		

	}
	
	
	public ServerSocket getServerSocket() {
		return serverSocket;
	};
	


}