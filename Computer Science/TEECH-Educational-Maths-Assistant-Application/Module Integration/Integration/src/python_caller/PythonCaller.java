package python_caller;

import java.io.* ;
import com.google.gson.* ;


public class PythonCaller 
{
	
	public PythonCaller()
	{
		
	}

	public void pythonCaller() throws IOException 
	{
		
		// TODO Auto-generated method stub
		// set up the command and parameter
		//String pythonScriptPath = "/Users/jeanvassoyan/programme_communication_python_java.py";
		//String pythonScriptPath = "/Users/jeanvassoyan/Documents/Projets pédagogiques/PACT/workspace - PACT/Python/Test_com_python_java.py";
		//String pythonScriptPath = "/Users/jeanvassoyan/Test_com_python_java.py" ;
		String pythonScriptPath = "/Users/jeanvassoyan/Test_com_python_java.py" ;
		String[] cmd = new String[2];
		cmd[0] = "python"; // check version of installed python: python -V
		cmd[1] = pythonScriptPath;
		 
		// create runtime to execute external command
		Runtime rt = Runtime.getRuntime();
		Process pr = rt.exec(cmd);
		 
		// retrieve output from python script
		BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
		String line = "";
		String line2 = bfr.readLine();
		System.out.println(line2);
		while((line = bfr.readLine()) != null) 
		{
		// display each output line form python script
			System.out.println("En voilà une");
			System.out.println(line);
		}
		System.out.println("j ai fini");
		
		
	}
	
	
	
		
		

}


