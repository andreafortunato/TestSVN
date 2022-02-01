package com.bikersland;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bikersland.bean.EventBean;
import com.bikersland.controller.application.MainControllerApp;
import com.bikersland.controller.graphics.EventDetailsControllerView;
import com.bikersland.db.DBConnection;
import com.bikersland.exception.InternalDBException;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.utility.TimedAlert;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * JavaFX App
 */
public class Main extends Application
{
	
	private static final String TIMEDALERT_INTERNAL_ERROR = ConstantStrings.TIMEDALERT_INTERNAL_ERROR;
	private static final String TIMEDALERT_SYSTEM_ERROR_HEADER = "timedalert_system_error_header";
	private static final String TIMEDALERT_SYSTEM_ERROR_CONTENT = "timedalert_system_error_content";
	
	private static String logFile = System.getProperty("user.home") + "\\BikersLand.log";

	private static List<String> cities = null;
	private static List<String> tags = null;

    private static Locale locale = Locale.ENGLISH;
    private static ResourceBundle bundle = ResourceBundle.getBundle("com.bikersland.languages.locale", locale);
    
    private static Scene scene;
    
    @Override
    public void start(Stage stage)
    {
    	/* File di log per gli errori. Percorso di esempio: C:\Users\NomeUtente\BikersLand.log */
    	FileHandler fh = null;
		try {
			fh = new FileHandler(logFile, true);
			Logger.getGlobal().addHandler(fh);
		} catch (SecurityException | IOException e) {
			TimedAlert.show(AlertType.ERROR,
					Main.bundle.getString(TIMEDALERT_INTERNAL_ERROR),
					Main.bundle.getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					e.getMessage(), Main.getLogFile());
			
			System.exit(-1);
		}
		
		refreshBundle();
    	
    	try {
    		MainControllerApp mainControllerApp = new MainControllerApp();
			setTags(mainControllerApp.getTags());
			setCities(mainControllerApp.getCities());
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.bundle.getString(TIMEDALERT_INTERNAL_ERROR),
					Main.bundle.getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
			
			System.exit(-1);
		}
    	
        initScene();
        
        stage.setScene(scene);
        
        stage.show();
        
        stage.setMinWidth(stage.getWidth());
        stage.setMinHeight(stage.getHeight());
        
        stage.setOnCloseRequest(event -> {
        	try {
				DBConnection.getConnection().close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });
    }
    
    private static void initScene()
    {
    	
    	try {
    		Main.scene = new Scene(loadFXML("Homepage"), 1253, 810);
		} catch (IOException ioe) {
			Logger.getGlobal().log(Level.SEVERE, "Catched IOException in setRoot(String) method, inside App.java", ioe);
			
			TimedAlert.show(AlertType.ERROR,
					Main.bundle.getString(TIMEDALERT_INTERNAL_ERROR),
					Main.bundle.getString(TIMEDALERT_SYSTEM_ERROR_HEADER),
					Main.bundle.getString(TIMEDALERT_SYSTEM_ERROR_CONTENT), logFile);
			
			System.exit(-1);
			
		}
    }

    public static void setRoot(String fxml) 
    {
        try {
			scene.setRoot(loadFXML(fxml));
		} catch (IOException ioe) {
			Logger.getGlobal().log(Level.SEVERE, "Catched IOException in setRoot(String) method, inside App.java", ioe);
			
			TimedAlert.show(AlertType.ERROR,
					Main.bundle.getString(TIMEDALERT_INTERNAL_ERROR),
					Main.bundle.getString(TIMEDALERT_SYSTEM_ERROR_HEADER),
					Main.bundle.getString(TIMEDALERT_SYSTEM_ERROR_CONTENT), logFile);
			
			System.exit(-1);
		}
    }
    
    public static void setRoot(String fxml, EventBean eventBean) 
    {
        try {
			scene.setRoot(loadFXML(fxml, eventBean));
		} catch (IOException ioe) {
			Logger.getGlobal().log(Level.SEVERE, "Catched IOException in setRoot(String, Event) method, inside App.java", ioe);
			
			TimedAlert.show(AlertType.ERROR,
					Main.bundle.getString(TIMEDALERT_INTERNAL_ERROR),
					Main.bundle.getString(TIMEDALERT_SYSTEM_ERROR_HEADER),
					Main.bundle.getString(TIMEDALERT_SYSTEM_ERROR_CONTENT), logFile);
			
			System.exit(-1);
		}
    }

    private static Parent loadFXML(String fxml) throws IOException
    {
    	refreshBundle();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"), bundle);
        fxmlLoader.setClassLoader(Main.class.getClassLoader());
        return fxmlLoader.load();
    }
    
    private static Parent loadFXML(String fxml, EventBean eventBean) throws IOException 
    {
    	refreshBundle();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml + ".fxml"), bundle);
        fxmlLoader.setController(new EventDetailsControllerView(eventBean));
        return fxmlLoader.load();
    }

    public static void main(String[] args) 
    {
        launch();
    }
    
    public static ResourceBundle getBundle() 
    {
    	return bundle;
    }
    
    public static void refreshBundle() 
    {
    	Main.bundle = ResourceBundle.getBundle("com.bikersland.languages.locale", locale);
    }
    
    public static List<String> getCities() 
    {
		return cities;
	}

	public static void setCities(List<String> cities) 
	{
		Main.cities = cities;
	}

	public static List<String> getTags() 
	{
		return tags;
	}

	public static void setTags(List<String> tags) 
	{
		Main.tags = tags;
	}

	public static Locale getLocale() 
	{
		return locale;
	}

	public static void setLocale(Locale locale) 
	{
		Main.locale = locale;
	}
	
	public static String getLogFile() 
	{
		return logFile;
	}
	
	public static Window getCurrentWindow() 
	{
		return scene.getWindow();
	}
	
}