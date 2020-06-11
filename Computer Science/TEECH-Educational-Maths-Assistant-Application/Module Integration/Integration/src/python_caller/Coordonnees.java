package python_caller;

public class Coordonnees {

	  private final int abscisse;
	  private final int ordonnee;

	  public Coordonnees(final int abscisse, final int ordonnee) {
	    super();
	    this.abscisse = abscisse;
	    this.ordonnee = ordonnee;
	  }

	  public int getAbscisse() {
	    return this.abscisse;
	  }

	  public int getOrdonnee() {
	    return this.ordonnee;
	  }
	  /*
	  @Override
	  public String toString() 
	  {
	    return "Coordonnees [abscisse=" + this.abscisse + ", ordonnee=" + this.ordonnee + "]";
	  }*/
}