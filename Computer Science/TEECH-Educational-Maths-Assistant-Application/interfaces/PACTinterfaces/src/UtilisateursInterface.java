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

public interface UtilisateursInterface {

	//Appartenance à un groupe ou non
	public boolean isMember(GroupesInterface groupe);
	
	//Membre ou admin du groupe concerné 
	public String getStatut(GroupesInterface groupe);
	
	//Niveau de l'élève dans la matière concernée à la date la plus récente
	public int getAisance(MatieresInterface matiere);
	
}
