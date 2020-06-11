package interfaceIA;


import python_caller2.*;


public class PythonCallerObject 
{
	
	int studentId ;
	
	public PythonCallerObject(int studentId)
	{
		this.studentId = studentId ;
	}
	
	
	public void refreshStudentDataWithPython()
	{
		
		Student student = new Student(studentId) ;
	}
	

}
