package TeechInterface;

import java.util.ArrayList;

public abstract class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {
			
			//CorrigeInterface.add(11,"Corrige allege", "Il faut bien maitriser la formule de Morgan", 10, "dataTest/corrensup.png", 2, "image");
			Exercice exo = new Exercice(11);
			//Corrige corr = (Corrige) exo.findCorrige();
			
			//ArrayList<Matiere> array = MatiereInterface.getAll();
			int id = MatiereInterface.add("Ensemble", "sup", 1);
			Matiere mat = new Matiere(id);
			exo.addMatiere(mat, 10, 3);
			System.out.println("done : " +mat.getNom());
			
			
		}
		
		catch(Exception e) {
			e.printStackTrace();}
	
	}
	
	

}
