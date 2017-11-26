package application.structures;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class TableStructure 
{
	public TableStructure(int id, int numberLesson, String nameAuditory, String subject, String teacher, String separation)
	{
		this.id = id;
		
		this.numberLesson = new SimpleStringProperty(String.valueOf(numberLesson));
		this.nameAuditory = new SimpleStringProperty(nameAuditory);
		this.subject = new SimpleStringProperty(subject);
		this.teacher = new SimpleStringProperty(teacher);
		this.separation = new SimpleStringProperty(separation);
	}
	
	public int id;
	
	public StringProperty numberLesson;
	public StringProperty  nameAuditory;
	public StringProperty  subject;
	public StringProperty  teacher;
	public StringProperty  separation;
	
	 public StringProperty numberLessonProperty()
	 {
	        return numberLesson;
     }
	 
	 public StringProperty nameAuditoryProperty()
	 {
	        return nameAuditory;
	 }
	 
	 public StringProperty subjectProperty()
	 {
	        return subject;
	 }
	 
	 public StringProperty teacherProperty()
	 {
	        return teacher;
	 }
	 
	 public StringProperty separationProperty()
	 {
	        return separation;
	 }
}
