package com.bikersland.controller.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.bikersland.bean.EventBean;
import com.bikersland.bean.UserBean;
import com.bikersland.controller.application.ProfileControllerApp;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.user.DuplicateEmailException;
import com.bikersland.exception.user.EmailException;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.utility.ConvertMethods;
import com.bikersland.utility.CustomGridPane;
import com.bikersland.utility.InstantTooltip;
import com.bikersland.utility.TimedAlert;
import com.bikersland.Main;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class ProfileControllerView 
{
	
	@FXML
    private ImageView imgProfileImage;
	
	@FXML
    private Label lblProfileOf;

    @FXML
    private Label lblName;

    @FXML
    private Label lblSurname;

    @FXML
    private Label lblEmail;

    @FXML
    private VBox vbViaggi;

    @FXML
    private CustomGridPane gpJoinedEvents;

    @FXML
    private CustomGridPane gpFavoriteEvents;
    
    private int viaggioBoxWidth = 420;
    
    private UserBean userBean;
	
	public void initialize() 
	{
		List<EventBean> joinedEventBeanList;
	    List<Node> joinedEventNodeList;
	    List<EventBean> favoriteEventBeanList;
	    List<Node> favoriteEventNodeList;
	    
	    ProfileControllerApp profileControllerApp = new ProfileControllerApp();
	    userBean = profileControllerApp.getLoggedUser();
	    
	    if(Main.getLocale() == Locale.ENGLISH) {
	    	lblProfileOf.setText(userBean.getUsername() + Main.getBundle().getString("profile_of"));
	    } else {
	    	lblProfileOf.setText(Main.getBundle().getString("profile_of") + userBean.getUsername());
	    }
	    
		lblName.setText(userBean.getName());
		lblSurname.setText(userBean.getSurname());
		lblEmail.setText(userBean.getEmail());
		
		InstantTooltip changeEmailTooltip = new InstantTooltip();
		changeEmailTooltip.setText(Main.getBundle().getString("change_email_tooltip"));
		lblEmail.setTooltip(changeEmailTooltip);
		
		ContextMenu contextMenu = new ContextMenu();
		 
        MenuItem item1 = new MenuItem();
        item1.setText(Main.getBundle().getString("change_email"));
        item1.setStyle(null);
        item1.setOnAction(event -> changeEmail());
 
        contextMenu.getItems().add(item1);
        lblEmail.setContextMenu(contextMenu);
		
		Platform.runLater(() -> 
			vbViaggi.prefWidthProperty().bind(vbViaggi.getParent().getScene().getWindow().widthProperty())
		);
		
		Image image = userBean.getImage();
		Circle imgCircle = new Circle(50);
		if(image == null) {
			image = profileControllerApp.getDefaultUserImage();
		} else {
			double w = 0;
            double h = 0;

            double ratioX = imgProfileImage.getFitWidth() / image.getWidth();
            double ratioY = imgProfileImage.getFitHeight() / image.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = image.getWidth() * reducCoeff;
            h = image.getHeight() * reducCoeff;

            imgProfileImage.setX((imgProfileImage.getFitWidth() - w) / 2);
            imgProfileImage.setY((imgProfileImage.getFitHeight() - h) / 2);
		}
		
		imgCircle.setCenterX(50);
	    imgCircle.setCenterY(50);
	    imgProfileImage.setImage(image);
		imgProfileImage.setClip(imgCircle);
		
		List<Integer> gridPaneColumns = new ArrayList<>();
		gridPaneColumns.add((((Double)Main.getCurrentWindow().getWidth()).intValue()-16-(getNumViaggi())*20)/420);
		
		try {
			favoriteEventBeanList = profileControllerApp.getFavoriteEventsByUser(userBean.getId());
			joinedEventBeanList = profileControllerApp.getJoinedEventsByUser(userBean.getId());
			
			joinedEventNodeList = ConvertMethods.eventsToNodeList(joinedEventBeanList);
			gpJoinedEvents.populateGrid(joinedEventNodeList, gridPaneColumns.get(0));
			
			favoriteEventNodeList = ConvertMethods.eventsToNodeList(favoriteEventBeanList);
			gpFavoriteEvents.populateGrid(favoriteEventNodeList, gridPaneColumns.get(0));			
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
			
			Main.setRoot("Homepage");
			return;
		}
		Platform.runLater(() -> 
			vbViaggi.getParent().getScene().getWindow().widthProperty().addListener((obs, oldVal, newVal) -> {
	        	
	        	int o = oldVal.intValue()-16-getNumViaggi()*20;
	        	int n = newVal.intValue()-16-getNumViaggi()*20;
	        	            	
	        	if(o/viaggioBoxWidth != n/viaggioBoxWidth)
	        		gridPaneColumns.set(0, n/viaggioBoxWidth);
	        	
					try {
						gpFavoriteEvents.populateGrid(favoriteEventNodeList, gridPaneColumns.get(0));
						gpJoinedEvents.populateGrid(joinedEventNodeList, gridPaneColumns.get(0));
					} catch (Exception e) {
						e.printStackTrace();
					}
	        	
	        })
		);
	}
	
	public int getNumViaggi() 
	{
    	return gpJoinedEvents.getColumnCount();
    }
	
	private void changeEmail()
	{
		TextInputDialog changeEmailDialog = new TextInputDialog(userBean.getEmail());
		changeEmailDialog.getDialogPane().getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
		changeEmailDialog.getDialogPane().getStyleClass().add("inputDialog");
		changeEmailDialog.setTitle(Main.getBundle().getString("alert_change_email_title"));
		changeEmailDialog.setHeaderText(Main.getBundle().getString("alert_change_email_header"));
		changeEmailDialog.setContentText(Main.getBundle().getString("alert_change_email_content"));
		changeEmailDialog.getDialogPane().setPrefWidth(400);

		Optional<String> result = changeEmailDialog.showAndWait();
		UserBean tmpUserBean = new UserBean();
		if (result.isPresent()){
		    try {
		    	tmpUserBean.setEmail(result.get().strip());
		    	
		    	ProfileControllerApp profileControllerApp = new ProfileControllerApp();
				profileControllerApp.changeUserEmail(userBean.getId(), result.get().strip());
				
				userBean.setEmail(result.get().strip());
				lblEmail.setText(userBean.getEmail());
			} catch (DuplicateEmailException dee) {
				TimedAlert.show(AlertType.ERROR,
						Main.getBundle().getString("timedalert_change_email_error_title"),
						Main.getBundle().getString("timedalert_change_email_error_header"),
						dee.getMessage(), userBean.getEmail());
			} catch (EmailException ee) {
				TimedAlert.show(AlertType.ERROR,
						Main.getBundle().getString("timedalert_change_email_error_title"),
						Main.getBundle().getString("timedalert_change_email_error_header"),
						Main.getBundle().getString("timedalert_change_email_error_content"),
						null);
			} catch (InternalDBException idbe) {
				TimedAlert.show(AlertType.ERROR,
						Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
						Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
						idbe.getMessage(), Main.getLogFile());
			}
		}
	}
}
