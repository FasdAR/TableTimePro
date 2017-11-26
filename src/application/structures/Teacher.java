package application.structures;

public class Teacher
{
	public Teacher(int ID, String Name, String Surname, String Patronymic, int IdCabinet)
	{
		this.ID = ID;
		this.Name = Name;
		this.Surname = Surname;
		this.Patronymic = Patronymic;
		this.IdCabinet = IdCabinet;
	}
	
	public int ID;
	public String Name;
	public String Surname;
	public String Patronymic;
	public int IdCabinet;
}
