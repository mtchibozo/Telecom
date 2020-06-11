package log;

import java.io.* ;

public class Log 
{
	
	public static void completeLog(String information) throws IOException
	{
		// Version 2
		
		FileOutputStream fos = null ;
		BufferedOutputStream bos = null ;
		DataOutputStream dos = null ;
			
		try {
			
			fos = new FileOutputStream("log1.txt");
			bos = new BufferedOutputStream(fos);
			dos = new DataOutputStream(bos) ;
			
			dos.writeChars(information) ;
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dos.close();
			} catch (Exception e) {}
			
		}
		
		// Version 1
		
		/*
		PrintWriter pw = null ;
		
		try {
			
			pw = new PrintWriter("test1.txt");
			
			pw.print(information);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pw.close();
			} catch(Exception e) {}
		}
		*/
	}

}
