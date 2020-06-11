package python_caller;

import com.google.gson.Gson ;

import com.google.gson.* ;

import java.util.* ;

public class TestSerialisation 
{

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		final GsonBuilder builder = new GsonBuilder() ;
		final Gson gson = builder.create();
		
		/*
		 * int machin
		 * int truc
		 * ArrayList<Integer> liste
		 */
		
		final String json = "{\"truc\":1,\"machin\":2}";
		
		TestObject testObject = gson.fromJson(json, TestObject.class);
		
		int truc = testObject.getTruc();
		int machin = testObject.getMachin();
		ArrayList<Integer> liste = testObject.getListe();
		
		System.out.println(truc);
		System.out.println(machin);
		System.out.println(liste);

		
	}

}













