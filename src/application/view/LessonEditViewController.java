package application.view;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import application.MainApp;
import application.structures.Group;
import application.structures.Lesson;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import application.structures.Subject;
import application.structures.Teacher;
import application.structures.Separation;
import application.structures.Auditory;;

public class LessonEditViewController
{
	@FXML
    private ComboBox NumberLesson;
    @FXML
    private ComboBox Subject;
    @FXML
    private ComboBox Teacher;
    @FXML
    private ComboBox Separation;
    @FXML
    private ComboBox Auditory;
    @FXML
    private Button Button;
    
    private Stage dialogStage;
    private Group group;
    private Lesson lesson;
    
    boolean okClicked;
    
    MainApp mainApp;
    
    ArrayList<Subject> subjects;
    ArrayList<Teacher> teachers;
    ArrayList<Separation> separations;
    ArrayList<Auditory> auditoryes;
    
    LocalDate date;
    
    boolean isAdd = false;
    
    public LessonEditViewController()
    {
    	
    }

    @FXML
    private void initialize() 
    {
    	Button.setOnAction(new EventHandler() 
        {
			@Override
			public void handle(Event event)
			{
				if (isAdd)
				{
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Instant instant = Instant.from(date.atStartOfDay(ZoneId.systemDefault()));
					Date date = Date.from(instant);
					
					if (lesson == null)
					{
						okClicked = mainApp.serverApi.AddLesson(group, NumberLesson.getSelectionModel().getSelectedIndex()+1, 
								auditoryes.get(Auditory.getSelectionModel().getSelectedIndex()), 
								subjects.get(Subject.getSelectionModel().getSelectedIndex()), 
								teachers.get(Teacher.getSelectionModel().getSelectedIndex()),
								separations.get(Separation.getSelectionModel().getSelectedIndex()), format.format(date));
					}
					else
					{
						okClicked = mainApp.serverApi.UpdateLesson(lesson.ID, group, NumberLesson.getSelectionModel().getSelectedIndex()+1, 
								auditoryes.get(Auditory.getSelectionModel().getSelectedIndex()), 
								subjects.get(Subject.getSelectionModel().getSelectedIndex()), 
								teachers.get(Teacher.getSelectionModel().getSelectedIndex()),
								separations.get(Separation.getSelectionModel().getSelectedIndex()), format.format(date));
					}
					
					dialogStage.close();
				}
			}
        });
    }
    
    public void setMainApp(MainApp mainApp) 
	{
    	this.mainApp = mainApp;
    	
    	NumberLesson.getItems().addAll("1 урок","2 урок","3 урок", "4 урок", "5 урок", "6 урок");
    	
    	subjects = mainApp.serverApi.getSubjects();
    	teachers = mainApp.serverApi.getTeachers();
    	separations = mainApp.serverApi.getSeparations();
    	auditoryes = mainApp.serverApi.getAuditoryes();
    	
    	int posLesson = 0;
		int posSubject = 0;
		int posTeacher = 0;
		int posSeparation = 0;
		int posAuditory = 0;
    	
    	for (int i=0; i<subjects.size(); i++)
    	{
    		Subject.getItems().add(subjects.get(i).NameSubject);
    	}
    	
    	for (int i=0; i<teachers.size(); i++)
    	{
    		Teacher.getItems().add(teachers.get(i).Surname + " " + teachers.get(i).Name + " " + teachers.get(i).Patronymic);
    	}
    	
    	for (int i=0; i<separations.size(); i++)
    	{
    		Separation.getItems().add(separations.get(i).NameSeparation);
    	}
    	
    	for (int i=0; i<auditoryes.size(); i++)
    	{
    		Auditory.getItems().add(auditoryes.get(i).NameAuditory);
    	}
    	
    	if (lesson != null)
    	{
    		posLesson = lesson.NumberLesson - 1;
    		Button.setText("Изменить");
    		
    		for (int i=0; i<subjects.size(); i++)
        	{
        		if (subjects.get(i).ID == lesson.IdSubject)
        		{
        			posSubject = i;
        			
        			break;
        		}
        	}
        	
        	for (int i=0; i<teachers.size(); i++)
        	{
        		if (teachers.get(i).ID == lesson.IdTeache)
        		{
        			posTeacher = i;
        			
        			break;
        		}
        	}
        	
        	for (int i=0; i<separations.size(); i++)
        	{
        		if (separations.get(i).ID == lesson.IdSeparation)
        		{
        			posSeparation = i;
        			
        			break;
        		}
        	}
        	
        	for (int i=0; i<auditoryes.size(); i++)
        	{
        		if (auditoryes.get(i).ID == lesson.IdAuditory)
        		{
        			posAuditory = i;
        			
        			break;
        		}
        	}
    	}
    	
    	NumberLesson.getSelectionModel().select(posLesson);
    	Subject.getSelectionModel().select(posSubject);
    	Teacher.getSelectionModel().select(posTeacher);
    	Separation.getSelectionModel().select(posSeparation);
    	Auditory.getSelectionModel().select(posAuditory);
	}

    public void setAdd(boolean isAdd)
    {
    	this.isAdd = isAdd;
    }
    
    public void setDialogStage(Stage dialogStage) 
    {
        this.dialogStage = dialogStage;
    }
    
    public void setDate(LocalDate date)
    {
    	this.date = date;
    }

    public void setGroup(Group group) 
    {
        this.group = group;
    }
    
    public void setLesson(Lesson lesson)
    {
    	this.lesson = lesson;
    }
    
    public boolean isOkClicked() 
    {
        return okClicked;
    }
}
