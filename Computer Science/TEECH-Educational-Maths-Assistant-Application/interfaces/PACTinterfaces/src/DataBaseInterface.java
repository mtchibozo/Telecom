/* Remplir la signature d'une fonction en cas de besoin sur
 * la table correspondante
 * 
 * Regarder le contenu de la base de donnée pour savoir dans 
 * quelle interface votre fonction se trouve 
 * 
 * (cf Description.adoc sur gitlab DE LA BRANCHE BDD)
 * 
 * Exemples à voir dans UtilisateursInterface.java
 * 
 * Ne pas oublier de commenter votre signature pour que nous
 * puissions l'implémenter comme vous le souhaitez
 * 
 * Prévenir le groupe de n'importe quelle mise à jour
 * 
 * Module Base de Données et Intégration et Tests
 * 
 */

public interface DataBaseInterface {

	//Ajout d'un utilisateur dans la BdD
	public void add(UtilisateursInterface utilisateur);
	
	//Ajout d'une matiere dans la BdD
	public void add(MatieresInterface matiere);
	
	//Ajout d'un exercice dans la BdD
	public void add(ExercicesInterface exercice);
	
	//Ajout d'un corrige dans la BdD
	public void add(CorrigesInterface corrige);
	
	//Ajout d'un groupe dans la BdD
	public void add(GroupesInterface groupe);
		
	
}
