package com.bikersland.controller.graphics;

import java.util.Locale;

import com.bikersland.controller.application.HeaderControllerApp;
import com.bikersland.exception.InternalDBException;
import com.bikersland.singleton.LoginSingleton;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.utility.InstantTooltip;
import com.bikersland.utility.TimedAlert;
import com.bikersland.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class HeaderControllerView 
{
	
	@FXML
    private ImageView btnLanguage;
	
	@FXML
	private Button btnNewEvent;

    @FXML
    private HBox hbNotLoggedIn;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnLogin;

    @FXML
    private HBox hbLoggedIn;

    @FXML
    private Button btnProfile;

    @FXML
    private Button btnLogout;

    @FXML
    private ImageView imgLogo;
    
    public void initialize() 
    {
    	if(LoginSingleton.getLoginInstance().getUser() != null) {
    		hbNotLoggedIn.setVisible(false);
    		hbLoggedIn.setVisible(true);
    		btnNewEvent.setVisible(true);
    	} else {
    		hbNotLoggedIn.setVisible(true);
    		hbLoggedIn.setVisible(false);
    		btnNewEvent.setVisible(false);
    	}
    	
    	
    	InstantTooltip flagTooltip = new InstantTooltip(Main.getBundle().getString("change_lang"));
    	Tooltip.install(btnLanguage, flagTooltip);
    	
    	if(Main.getLocale() == Locale.ITALIAN)
    		btnLanguage.setImage(new Image(Main.class.getResource("img/italy.png").toString()));
    	else
    		btnLanguage.setImage(new Image(Main.class.getResource("img/usa.png").toString()));
    	
    }
    
    @FXML
    private void login() 
    {
    	Main.setRoot("Login");
    }
    
    @FXML
    private void register()
    {
    	Main.setRoot("Register");
    }
    
    @FXML
    private void goToHomepage()
    {
    	goHomepage();
    }
    
    @FXML
    private void goToProfile()
    {
    	Main.setRoot("Profile");
    }
    
    @FXML
    private void logout()
    {
    	LoginSingleton.logout();
    	goHomepage();
    }
    
    @FXML
    private void newEvent() 
    {
    	Main.setRoot("NewEvent");
    }
    
    @FXML
    private void changeLanguage() 
    {
    	if(Main.getLocale() == Locale.ITALIAN) {
    		Main.setLocale(Locale.ENGLISH);
    		btnLanguage.setImage(new Image(Main.class.getResource("img/usa.png").toString()));
    	} else {
    		Main.setLocale(Locale.ITALIAN);
    		btnLanguage.setImage(new Image(Main.class.getResource("img/italy.png").toString()));
    	}
    	
    	try {
    		HeaderControllerApp headerControllerApp = new HeaderControllerApp();
			Main.setTags(headerControllerApp.getTags());
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
			
		}
    	goHomepage();
    }
    
    private void goHomepage() 
    {
    	Main.setRoot("Homepage");
    }
}
