package com.bikersland.controller.graphics;

import com.bikersland.bean.EventBean;
import com.bikersland.controller.application.EventDetailsControllerApp;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.NoEventParticipantsException;
import com.bikersland.singleton.LoginSingleton;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.utility.ConvertMethods;
import com.bikersland.utility.TimedAlert;
import com.bikersland.Main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class EventDetailsControllerView 
{
	
	@FXML
    private BorderPane pnlMain;

    @FXML
    private ImageView imgBackground;

    @FXML
    private Label lblTitle;
    
    @FXML
    private Label lblDescription;

    @FXML
    private Label lblDepartureDate;

    @FXML
    private Label lblReturnDate;

    @FXML
    private Label lblDepartureCity;

    @FXML
    private Label lblDestinationCity;

    @FXML
    private Label lblParticipants;
    
    @FXML
    private Label lblCreateTime;

    @FXML
    private Label lblOwnerUsername;
    
    @FXML
    private ListView<String> lvTags;
    
    @FXML
    private ListView<String> lvParticipants;
    
    @FXML
    private Button btnJoin;
    
    private EventBean eventBean;
    
    private boolean isJoined;
    
    private Integer loggedUserId;
    private String loggedUserUsername;
    
    public EventDetailsControllerView(EventBean eventBean) 
    {
		this.eventBean = eventBean;
	}
	
	public void initialize() 
	{
		if (LoginSingleton.getLoginInstance().getUser() != null) {
			loggedUserId = LoginSingleton.getLoginInstance().getUser().getId();
			loggedUserUsername = LoginSingleton.getLoginInstance().getUser().getUsername();
		} else {
			loggedUserId = -1;
		}
		
		lblTitle.setText(eventBean.getTitle());
		lblDepartureDate.setText(ConvertMethods.dateToLocalFormat(eventBean.getDepartureDate()));
		lblReturnDate.setText(ConvertMethods.dateToLocalFormat(eventBean.getReturnDate()));
		lblDepartureCity.setText(eventBean.getDepartureCity());
		lblDestinationCity.setText(eventBean.getDestinationCity());
		lblDescription.setText(eventBean.getDescription());
		lvTags.setItems(FXCollections.observableArrayList(eventBean.getTags()));
		if(lvTags.getItems().isEmpty())
			lvTags.setItems(FXCollections.observableArrayList(Main.getBundle().getString("no_tags")));
		lvTags.setOnMousePressed(e -> lvTags.getSelectionModel().clearSelection());
		lblCreateTime.setText(ConvertMethods.dateToLocalFormat(eventBean.getCreateTime()));
		lblOwnerUsername.setText(eventBean.getOwnerUsername());
		
		try {
			EventDetailsControllerApp eventDetailsControllerApp = new EventDetailsControllerApp();
			lvParticipants.setItems(FXCollections.observableArrayList(eventDetailsControllerApp.getEventParticipants(eventBean.getId())));
			lblParticipants.setText(String.valueOf(lvParticipants.getItems().size()));
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
			
			Main.setRoot("Homepage");
		} catch (NoEventParticipantsException nepe) {
			lvParticipants.setItems(FXCollections.observableArrayList(Main.getBundle().getString(ConstantStrings.NO_PARTICIPANTS)));
			lblParticipants.setText("0");
		}
		
		Image image = eventBean.getImage();
		if(image == null) {
			EventDetailsControllerApp eventDetailsControllerApp = new EventDetailsControllerApp();
			image = eventDetailsControllerApp.getDefaultEventImage();
		}
		
		imgBackground.setImage(image);
		
		if(image.getWidth() < image.getHeight()) {
	    	imgBackground.fitHeightProperty().bind(pnlMain.heightProperty());
	    	imgBackground.setFitWidth(0.0);
		} else {
	    	imgBackground.fitWidthProperty().bind(pnlMain.widthProperty());
	    	imgBackground.setFitHeight(0.0);
		}
    	imgBackground.setPreserveRatio(true);
    	
    	if(loggedUserId == -1) {
    		btnJoin.setVisible(false);
    	} else {
			try {
				EventDetailsControllerApp eventDetailsControllerApp = new EventDetailsControllerApp();
				setIsJoined(eventDetailsControllerApp.userJoinedEvent(loggedUserId, eventBean.getId()));
			} catch (InternalDBException idbe) {
				TimedAlert.show(AlertType.ERROR,
						Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
						Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
						idbe.getMessage(), Main.getLogFile());
				
				Main.setRoot("Homepage");
			}
    	}
	}
	
	@FXML
    private void joinEvent() 
	{
		try {
			EventDetailsControllerApp eventDetailsControllerApp = new EventDetailsControllerApp();
			if(isJoined) {
	    		eventDetailsControllerApp.removeUserParticipation(loggedUserId, eventBean.getId());
	    		lblParticipants.setText(String.valueOf(Integer.valueOf(lblParticipants.getText())-1));
	    		setIsJoined(false);
	    	} else {
	    		eventDetailsControllerApp.addUserParticipation(loggedUserId, eventBean.getId());
	    		lblParticipants.setText(String.valueOf(Integer.valueOf(lblParticipants.getText())+1));
	    		setIsJoined(true);
	    	}
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
		}
    }
	
	private void setIsJoined(boolean isJoined)
	{
		this.isJoined = isJoined;
		if(isJoined) {
			btnJoin.setText(Main.getBundle().getString("remove_participation"));
			
			if(!lvParticipants.getItems().contains(loggedUserUsername)) {
				ObservableList<String> participants = lvParticipants.getItems();
				if(participants.size() == 1 && participants.get(0).equals(Main.getBundle().getString(ConstantStrings.NO_PARTICIPANTS))) {
					participants.clear();
				}
				participants.add(loggedUserUsername);
				FXCollections.sort(participants);
				lvParticipants.setItems(participants);
				
			}
		}
		else {
			btnJoin.setText(Main.getBundle().getString("join"));
			lvParticipants.getItems().remove(loggedUserUsername);
			if(lvParticipants.getItems().isEmpty())
				lvParticipants.setItems(FXCollections.observableArrayList(Main.getBundle().getString(ConstantStrings.NO_PARTICIPANTS)));
			
		}
	}
}
