package application.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import application.MainApp;
import application.structures.Auditory;
import application.structures.Group;
import application.structures.Lesson;
import application.structures.Separation;
import application.structures.Subject;
import application.structures.TableStructure;
import application.structures.Teacher;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainViewController 
{
	@FXML
	TreeView<String> GroupsTree;
	@FXML
	Label InfoText;
	@FXML
	TableView<TableStructure> TableTime;
	@FXML
	TableColumn<TableStructure, String> NumberLesson;
	@FXML
	TableColumn<TableStructure, String> NumberAuditory;
	@FXML
	TableColumn<TableStructure, String> Subject;
	@FXML
	TableColumn<TableStructure, String> Teacher;
	@FXML
	TableColumn<TableStructure, String> Separation;
	@FXML
	DatePicker DatePicker;
	@FXML
	Button AddLesson;
	@FXML
	Button ChangeLesson;
	@FXML
	Button RemoveLesson;
	@FXML
	HBox HboxButton;
	
	Group selectGroup;
	Lesson selectLesson = null;
	
	private MainApp mainApp;
	
	public ArrayList<Group> groups = new ArrayList<Group>();
	public ArrayList<Lesson> lessons = new ArrayList<Lesson>();
	
	SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
	SimpleDateFormat formatSql = new SimpleDateFormat("yyyy-MM-dd");
	
	public MainViewController() {    }
  
	@FXML
	private void initialize()
	{
	      TableTime.setVisible(false);
	      DatePicker.setValue(LocalDate.now());
	      HboxButton.setVisible(false);
	      
	      NumberLesson.setCellValueFactory(cellData -> cellData.getValue().numberLessonProperty());
	      NumberAuditory.setCellValueFactory(cellData -> cellData.getValue().nameAuditoryProperty());
	      
	      Subject.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
	      Teacher.setCellValueFactory(cellData -> cellData.getValue().teacherProperty());
	      Separation.setCellValueFactory(cellData -> cellData.getValue().separationProperty());
	      
	      TableTime.getSelectionModel().selectedItemProperty().addListener(
	              (observable, oldValue, newValue) -> selectedItemTable(newValue));
	      
	      DatePicker.valueProperty().addListener(new ChangeListener<LocalDate>() 
	        {
	            @Override
	            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) 
	            {
					LocalDate localDate = newValue;
					Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
					Date date = Date.from(instant);
					TableTime.setPlaceholder(new Label("На " + format.format(date) + " расписания нету."));
					
					updateTable(formatSql.format(date));
					
					if (lessons.isEmpty())
					{
						ChangeLesson.setDisable(true);
						RemoveLesson.setDisable(true);
					}
	            }
	        });
	        
	      ChangeLesson.setOnAction(new EventHandler()
	      {
	    	  @Override
				public void handle(Event event)
				{
					boolean res = mainApp.showLessonEditDialog(selectLesson ,selectGroup, DatePicker.getValue(), true);
					
					if (res)
					{
						System.err.println("true");
						LocalDate localDate = DatePicker.getValue();
						Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
						Date date = Date.from(instant);
						
						updateTable(formatSql.format(date));
					}
					else
					{
						System.err.println("false");
					}
				}
	      });
	      
	      RemoveLesson.setOnAction(new EventHandler()
	      {
	    	  @Override
				public void handle(Event event)
				{
					boolean res = mainApp.serverApi.RemoveLesson(selectLesson.ID);
					
					if (res)
					{	
						System.err.println("true");
						LocalDate localDate = DatePicker.getValue();
						Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
						Date date = Date.from(instant);
						
						updateTable(formatSql.format(date));
					}
					else
					{
						System.err.println("false");
					}
				}
	      });
	      
	      AddLesson.setOnAction(new EventHandler() 
	        {
				@Override
				public void handle(Event event)
				{
					boolean res = mainApp.showLessonEditDialog(selectLesson ,selectGroup, DatePicker.getValue(), true);
					
					if (res)
					{
						System.err.println("true");
						LocalDate localDate = DatePicker.getValue();
						Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
						Date date = Date.from(instant);
						
						updateTable(formatSql.format(date));
					}
					else
					{
						System.err.println("false");
					}
				}
	        });
	}
	  
	public void selectedItemTable(TableStructure tableStructure)
	{
		ChangeLesson.setDisable(false);
		RemoveLesson.setDisable(false);
		
		for (int i=0; i<lessons.size(); i++)
		{
			if (tableStructure != null)
			{
				if (tableStructure.id == lessons.get(i).ID)
				{
					selectLesson = lessons.get(i);
					
					break;
				}
			}
			else
			{
				ChangeLesson.setDisable(true);
				RemoveLesson.setDisable(true);
			}
		}
	}
	
	public void setMainApp(MainApp mainApp) 
	{
        this.mainApp = mainApp;
        
        groups = mainApp.serverApi.getGroups();
        
        if (!groups.isEmpty())
        {
        	TreeItem<String> rootItem = new TreeItem<String>("Курсы");
          	rootItem.setExpanded(true);
        	
        	TreeItem<String> oneCourse = new TreeItem<String>("Первый курс");
        	TreeItem<String> twoCourse = new TreeItem<String>("Второй курс");
        	TreeItem<String> freeCourse = new TreeItem<String>("Третий курс");
        	TreeItem<String> fourCourse = new TreeItem<String>("Четвертый курс");
        	
        	for (int i=0; i<groups.size(); i++)
        	{
        		int diffYear = Calendar.getInstance().get(Calendar.YEAR) - groups.get(i).StartYear;
        		
        		TreeItem<String> group = new TreeItem<String>(groups.get(i).NameGroup);
        		
        		switch (diffYear)
	        	{
		        	case 0:
		        		oneCourse.getChildren().add(group);
		        		break;
		        	case 1:
		        		twoCourse.getChildren().add(group);
		        		break;
		        	case 2:
		        		freeCourse.getChildren().add(group);
		        		break;
		        	case 3:
		        		fourCourse.getChildren().add(group);
		        		break;
	        	}
        	}
        	
        	rootItem.getChildren().addAll(oneCourse, twoCourse, freeCourse, fourCourse);
        	
        	GroupsTree.setRoot(rootItem);
        	GroupsTree.setShowRoot(false);
        	GroupsTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() 
        	{
				@Override
				public void changed(ObservableValue observable, Object oldValue, Object newValue)
				{
					TableTime.getItems().clear();
					
					TreeItem<String> selectedItem = (TreeItem<String>) newValue;
					
					boolean result = false;
					
					for (int i=0; i<groups.size(); i++)
					{
						if (groups.get(i).NameGroup.equals(selectedItem.getValue()))
						{
							selectGroup = groups.get(i);
							result = true;
							break;
						}
					}
					
					if (result)
					{
						TableTime.setVisible(true);
						InfoText.setVisible(false);
						
						HboxButton.setVisible(true);
						ChangeLesson.setDisable(true);
						RemoveLesson.setDisable(true);
						
						LocalDate localDate = DatePicker.getValue();
						Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
						Date date = Date.from(instant);
						TableTime.setPlaceholder(new Label("На " + format.format(date) + " расписания нету."));
						
						updateTable(formatSql.format(date));
					}
					else
					{
						TableTime.setVisible(false);
						InfoText.setVisible(true);
						HboxButton.setVisible(false);
						
						ChangeLesson.setDisable(false);
						RemoveLesson.setDisable(false);
					}
				}
        	});
        }
	}
	
	public void updateTable(String date)
	{
		if (selectGroup != null)
		{
			selectLesson = null;
			
			lessons.clear();
			lessons = mainApp.serverApi.getLessons(selectGroup, date);
			ArrayList<TableStructure> tables = new ArrayList<TableStructure>();
			
			for (int i=0; i<lessons.size(); i++)
			{
				Auditory auditory = mainApp.serverApi.getCurrentAuditory(lessons.get(i).IdAuditory);
				Subject subject = mainApp.serverApi.getCurrentSubject(lessons.get(i).IdSubject);
				Teacher teacher = mainApp.serverApi.getCurrentTeacher(lessons.get(i).IdTeache);
				Separation separation = mainApp.serverApi.getCurrentSeparation(lessons.get(i).IdSeparation);
				
				TableStructure table = new TableStructure(lessons.get(i).ID, lessons.get(i).NumberLesson, auditory.NameAuditory, subject.NameSubject, 
						teacher.Surname + " " + teacher.Name + " " + teacher.Patronymic, separation.NameSeparation);
				
				tables.add(table);
			}
			
			ObservableList<TableStructure> list = FXCollections.observableArrayList(tables);
			TableTime.getItems().clear();
			TableTime.setItems(list);
		}
	}
}
