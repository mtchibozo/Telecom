
import bdd.DatabaseConnection;
import tableEntities.*;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			DatabaseConnection.open();
			Utilisateur user = new Utilisateur(3);
			Matiere mat = new Matiere(3);
			user.setAisance(mat,8);
			System.out.println("ok");
			int ais = user.getAisance(mat);
			System.out.println("aisance :"+ais);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("");
		DatabaseConnection.close();
	}

}
