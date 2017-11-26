package application;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import application.functionals.Config;
import application.functionals.ServerApi;
import application.structures.Auditory;
import application.structures.Group;
import application.structures.Lesson;
import application.structures.Separation;
import application.structures.Subject;
import application.structures.Teacher;
import application.view.LessonEditViewController;
import application.view.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application
{
	private Stage primaryStage;
	private AnchorPane rootLayout;
	
	public ServerApi serverApi;

	@Override
	public void start(Stage primaryStage) 
	{
		try 
		{
			serverApi = new ServerApi();
		} 
		catch (ClassNotFoundException | SQLException e) 
		{
			e.printStackTrace();
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Подключение к базе данных");
			alert.setHeaderText("Не удалось подключиться к базе данных");
			alert.setContentText(e.getMessage());

			alert.showAndWait();
		}
		
		this.primaryStage = primaryStage;
	    this.primaryStage.setTitle("TableTimePro");
	    this.primaryStage.setResizable(false);
	
	    initRootLayout();
	}
	
	public void initRootLayout() 
	{
    	try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/MainView.fxml"));
            rootLayout = (AnchorPane) loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            MainViewController controller = loader.getController();
            controller.setMainApp(this);
        } 
        catch (IOException e)
    	{
            e.printStackTrace();
        }
	}

   	public Stage getPrimaryStage()
   	{
       return primaryStage;
   	}  
   
	public static void main(String[] args) 
	{
		launch(args);
	}

	public boolean showLessonEditDialog(Lesson lesson, Group group, LocalDate date, boolean isAdd)
	{
	    try 
	    {
	        FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(MainApp.class.getResource("view/LessonEditView.fxml"));
	        AnchorPane page = (AnchorPane) loader.load();

	        Stage dialogStage = new Stage();
	        if (group == null)
	        {
	        	dialogStage.setTitle("Добавление урока");
	        }
	        else
	        {
	        	dialogStage.setTitle("Редактирование урока");
	        }
	        
	        dialogStage.initModality(Modality.WINDOW_MODAL);
	        dialogStage.initOwner(primaryStage);
	        dialogStage.setResizable(false);
	        Scene scene = new Scene(page);
	        dialogStage.setScene(scene);

	        LessonEditViewController controller = loader.getController();
	        controller.setDialogStage(dialogStage);
	        controller.setAdd(isAdd);
	        controller.setGroup(group);
	        controller.setLesson(lesson);
	        controller.setMainApp(this);
	        controller.setDate(date);

	        dialogStage.showAndWait();

	        return controller.isOkClicked();
	    } 
	    catch (IOException e)
	    {
	        e.printStackTrace();
	        return false;
	    }
	}
}
