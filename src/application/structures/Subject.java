package application.structures;

public class Subject
{
	public Subject(int ID, String NameSubject, int IdTeacher, int IdAuditory)
	{
		this.ID = ID;
		this.NameSubject = NameSubject;
		this.IdTeacher = IdTeacher;
		this.IdAuditory = IdAuditory;
	}
	
	public int ID;
	public String NameSubject;
	public int IdTeacher;
	public int IdAuditory;
}
