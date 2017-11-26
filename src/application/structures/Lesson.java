package application.structures;

public class Lesson 
{
	public Lesson(int ID, int IdGroup, int IdAuditory, int NumberLesson, int IdSubject, int IdTeache, int IdSeperation, String Date)
	{
		this.ID = ID;
		this.IdGroup = IdGroup;
		this.IdAuditory = IdAuditory;
		this.NumberLesson = NumberLesson;
		this.IdSubject = IdSubject;
		this.IdTeache = IdTeache;
		this.IdSeparation = IdSeperation;
		this.Date = Date;
	}
	 
	public int ID;
	public int IdGroup;
	public int IdAuditory;
	public int NumberLesson;
	public int IdSubject;
	public int IdTeache;
	public int IdSeparation;
	public String Date;
}
