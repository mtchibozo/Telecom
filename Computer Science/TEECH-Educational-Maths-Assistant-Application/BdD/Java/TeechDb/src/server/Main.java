package server;

import java.util.Scanner;



public class Main {

	public static final int min = 60000;
	public static final int sec = 1000;
	

		  
	public static void main(String[] args) {
		
		int time;	
		Scanner sc = new Scanner(System.in);
		System.out.println("######### TEECH SEVER #########");	
		System.out.print("Set server timeout (number of minutes) : ");
		time = sc.nextInt();
		sc.close();
		System.out.println("");	
		ServerTeech st = new ServerTeech(time*min);
		st.start();
		StopServer stp = new StopServer(st);
		stp.start();
		try {
			st.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		finally {
			stp.close();
		}
		
		
		  
	}		
		
}


