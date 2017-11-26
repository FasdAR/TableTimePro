package application.functionals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.structures.Auditory;
import application.structures.Group;
import application.structures.Lesson;
import application.structures.Separation;
import application.structures.Subject;
import application.structures.Teacher;

public class ServerApi
{
	static Connection conn = null;
	
	public ServerApi() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		conn = DriverManager.getConnection(Config.connectionUrl);
	}
	
	public ArrayList<Group> getGroups() 
	{
		String selectSql = "SELECT * FROM dbo.Groups"; 
		
		ArrayList<Group> groups = new ArrayList<Group>();
		
		try
		{
			PreparedStatement st;
			st = conn.prepareStatement(selectSql);
			ResultSet result = st.executeQuery();
			
			while (result.next())
	        {
	        	Group group = new Group(result.getInt("ID"), result.getString("NameGroup"), result.getInt("StartYear"));
	        	groups.add(group);
	        }
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return groups;
	}
	
	public ArrayList<Auditory> getAuditoryes() 
	{
		String selectSql = "SELECT * FROM dbo.Auditoryes"; 
		
		ArrayList<Auditory> auditoryes = new ArrayList<Auditory>();
		
		try
		{
			PreparedStatement st;
			st = conn.prepareStatement(selectSql);
			ResultSet result = st.executeQuery();
			
			while (result.next())
	        {
				Auditory auditory = new Auditory(result.getInt("ID"), result.getString("NameAuditory"));
				auditoryes.add(auditory);
	        }
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return auditoryes;
	}

	public ArrayList<Subject> getSubjects() 
	{
		String selectSql = "SELECT * FROM dbo.Subjects"; 
		
		ArrayList<Subject> subjects = new ArrayList<Subject>();
		
		try
		{
			PreparedStatement st;
			st = conn.prepareStatement(selectSql);
			ResultSet result = st.executeQuery();
			
			while (result.next())
	        {
				Subject subject = new Subject(result.getInt("ID"), result.getString("NameSubject"),
						result.getInt("IdTeacher"), result.getInt("IdAuditory"));
				subjects.add(subject);
	        }
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return subjects;
	}
	
	public ArrayList<Teacher> getTeachers() 
	{
		String selectSql = "SELECT * FROM dbo.Teachers"; 
		
		ArrayList<Teacher> teachers = new ArrayList<Teacher>();
		
		try
		{
			PreparedStatement st;
			st = conn.prepareStatement(selectSql);
			ResultSet result = st.executeQuery();
			
			while (result.next())
	        {
				Teacher teacher = new Teacher(result.getInt("ID"), result.getString("Name"),
						result.getString("Surname"), result.getString("Patronymic"), result.getInt("IdCabinet"));
				teachers.add(teacher);
	        }
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return teachers;
	}
	
	public ArrayList<Separation> getSeparations() 
	{
		String selectSql = "SELECT * FROM dbo.Separations"; 
		
		ArrayList<Separation> separations = new ArrayList<Separation>();
		
		try
		{
			PreparedStatement st;
			st = conn.prepareStatement(selectSql);
			ResultSet result = st.executeQuery();
			
			while (result.next())
	        {
				Separation separation = new Separation(result.getInt("ID"), result.getString("NameSeparation"));
				separations.add(separation);
	        }
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return separations;
	}
	
	public boolean AddLesson(Group group,int numberLesson, Auditory auditory, Subject subject, Teacher teacher, Separation separation, String date)
	{		
		try
		{
			String selectSql = "INSERT INTO dbo.Lessons(NumberLesson, IdGroup, IdAuditory, IdSubject, IdTeache, IdSeparation, Date) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)"; 
			
			PreparedStatement st = conn.prepareStatement(selectSql);
			st.setInt(1, numberLesson);
			st.setInt(2, group.ID);
			st.setInt(3, auditory.ID);
			st.setInt(4, subject.ID);
			st.setInt(5, teacher.ID);
			st.setInt(6, separation.ID);
			st.setString(7, date);
			st.execute();
			
			return true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	public boolean UpdateLesson(int ID, Group group,int numberLesson, Auditory auditory, Subject subject, Teacher teacher, Separation separation, String date)
	{		
		try
		{
			String selectSql = "UPDATE dbo.Lessons SET NumberLesson = ?, IdGroup = ?, IdAuditory = ?, IdSubject = ?, IdTeache = ?, IdSeparation = ?, Date = ? WHERE ID = ?"; 
			
			PreparedStatement st = conn.prepareStatement(selectSql);
			st.setInt(1, numberLesson);
			st.setInt(2, group.ID);
			st.setInt(3, auditory.ID);
			st.setInt(4, subject.ID);
			st.setInt(5, teacher.ID);
			st.setInt(6, separation.ID);
			st.setString(7, date);
			st.setInt(8, ID);
			st.execute();
			
			return true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
			return false;
		}
	}

	public boolean RemoveLesson (int ID)
	{
		try
		{
			String selectSql = "DELETE FROM dbo.Lessons WHERE ID = ?"; 
			
			PreparedStatement st = conn.prepareStatement(selectSql);
			st.setInt(1, ID);
			st.execute();
			
			return true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
			System.err.println(e.getMessage());
			return false;
		}
	}
	
	public ArrayList<Lesson> getLessons(Group group, String Date)
	{
		String selectSql = "SELECT * FROM dbo.Lessons WHERE IdGroup = ? AND Date = ?"; 
		
		ArrayList<Lesson> lessons = new ArrayList<Lesson>();
		
		try
		{
			PreparedStatement st;
			st = conn.prepareStatement(selectSql);
			st.setInt(1, group.ID);
			st.setString(2, Date);
			ResultSet result = st.executeQuery();
			
			while (result.next())
	        {
				Lesson lesson = new Lesson(result.getInt("ID"), result.getInt("IdGroup"), result.getInt("IdAuditory"),
						result.getInt("NumberLesson"), result.getInt("IdSubject"), result.getInt("IdTeache"),
						result.getInt("IdSeparation"), Date);
				lessons.add(lesson);
	        }
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return lessons;
	}
	
	public Subject getCurrentSubject(int id)
	{
		String selectSql = "SELECT * FROM dbo.Subjects WHERE ID = ?"; 
		
		Subject subject = null;
		
		try
		{
			PreparedStatement st;
			st = conn.prepareStatement(selectSql);
			st.setInt(1, id);
			ResultSet result = st.executeQuery();
			
			if (result.next())
	        {
				subject = new Subject(result.getInt("ID"), result.getString("NameSubject"),
						result.getInt("IdTeacher"), result.getInt("IdAuditory"));
	        }
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return subject;
	}
	
	public Teacher getCurrentTeacher(int id)
	{
		String selectSql = "SELECT * FROM dbo.Teachers WHERE ID = ?"; 
		
		Teacher teacher = null;
		
		try
		{
			PreparedStatement st;
			st = conn.prepareStatement(selectSql);
			st.setInt(1, id);
			ResultSet result = st.executeQuery();
			
			if (result.next())
	        {
				teacher = new Teacher(result.getInt("ID"), result.getString("Name"),
						result.getString("Surname"), result.getString("Patronymic"), result.getInt("IdCabinet"));
	        }
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return teacher;
	}
	
	public Auditory getCurrentAuditory(int id)
	{
		String selectSql = "SELECT * FROM dbo.Auditoryes WHERE ID = ?"; 
		
		Auditory auditory = null;
		
		try
		{
			PreparedStatement st;
			st = conn.prepareStatement(selectSql);
			st.setInt(1, id);
			ResultSet result = st.executeQuery();
			
			if (result.next())
	        {
				auditory = new Auditory(result.getInt("ID"), result.getString("NameAuditory"));
	        }
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return auditory;
	}

	public Separation getCurrentSeparation(int id)
	{
		String selectSql = "SELECT * FROM dbo.Separations WHERE ID = ?"; 
		
		Separation separation = null;
		
		try
		{
			PreparedStatement st;
			st = conn.prepareStatement(selectSql);
			st.setInt(1, id);
			ResultSet result = st.executeQuery();
			
			if (result.next())
	        {
				separation = new Separation(result.getInt("ID"), result.getString("NameSeparation"));
	        }
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return separation;
	}
	
}
