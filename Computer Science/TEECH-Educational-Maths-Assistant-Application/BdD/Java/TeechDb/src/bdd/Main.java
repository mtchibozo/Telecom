package bdd;

import tableEntities.*;


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		try {
			System.out.println("done");
			DatabaseConnection.close();
		}
		
		catch (Exception e) {
			e.printStackTrace();
			System.err.println(e);
		}
		
		finally {
		}
		
		

	}

}
