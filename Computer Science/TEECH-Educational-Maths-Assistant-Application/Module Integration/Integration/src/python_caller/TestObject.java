package python_caller;

import java.util.* ;

public class TestObject 
{
	
	private int machin ;
	private int truc ;
	private ArrayList<Integer> liste ;
	
	public TestObject(int machin, int truc)
	{
		this.machin = machin ;
		this.truc = truc ;
		this.liste = new ArrayList<Integer>() ;
		int i = 0 ;
		for (i=0 ; i < 10 ; i++)
		{
			liste.add(i);
		}
	}
	
	
	public int getMachin()
	{
		return machin ;
	}
	
	public int getTruc()
	{
		return truc ;
	}
	
	public ArrayList<Integer> getListe()
	{
		return liste ;
	}
		

}
