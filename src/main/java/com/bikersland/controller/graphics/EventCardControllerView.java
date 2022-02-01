package com.bikersland.controller.graphics;

import java.util.List;

import com.bikersland.bean.EventBean;
import com.bikersland.controller.application.EventCardControllerApp;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.NoEventParticipantsException;
import com.bikersland.singleton.LoginSingleton;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.utility.ConvertMethods;
import com.bikersland.utility.InstantTooltip;
import com.bikersland.utility.SpriteAnimation;
import com.bikersland.utility.TimedAlert;
import com.bikersland.Main;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class EventCardControllerView 
{
	
	@FXML
    private StackPane spMain;
	
	@FXML
    private ImageView imgViaggio;

    @FXML
    private ImageView imgStar;

    @FXML
    private Label lblTitle;
    
    @FXML
    private HBox hbEventData;

    @FXML
    private Label lblDepartureCity;

    @FXML
    private Label lblDestinationCity;

    @FXML
    private Label lblTags;

    @FXML
    private Label lblDepartureDate;

    @FXML
    private Label lblReturnDate;

    @FXML
    private Label lblOwnerUsername;
    
    @FXML
    private Button btnJoin;
    
    @FXML
    private Button btnParticipants;
	
	private static final int COLUMNS = 7;
	private static final int COUNT = 7;
	private static final int OFFSET_X = 0;
	private static final int OFFSET_Y = 0;
	private static final int WIDTH_HEIGHT = 128;
	private Animation animation = null;

    private EventBean eventBean;
    private boolean isFavorite = false;
    private boolean isJoined = false;
    
    private InstantTooltip favoriteTooltip = new InstantTooltip("");
    
    private Integer loggedUserId = null;
    
    public EventCardControllerView(EventBean eventBean) 
    {
		this.eventBean = eventBean;
	}
    
    public EventCardControllerView(EventBean eventBean, Boolean isFavorite) 
    {
		this.eventBean = eventBean;
		this.setIsFavorite(isFavorite);
	}
    
    public void initialize() 
    {
    	if (LoginSingleton.getLoginInstance().getUser() != null) {
			loggedUserId = LoginSingleton.getLoginInstance().getUser().getId();
			
		} else {
			loggedUserId = -1;
		}
    	
    	Tooltip.install(imgStar, favoriteTooltip);
    	
    	Rectangle imgRound = new Rectangle(400, 170);
    	imgRound.setArcHeight(10);
    	imgRound.setArcWidth(10);
    	
    	imgViaggio.setClip(imgRound);
    	
    	Image image = eventBean.getImage();
    	if(image == null) {
    		EventCardControllerApp eventCardControllerApp = new EventCardControllerApp();
    		image = eventCardControllerApp.getDefaultEventImage();
    	}
    	
    	imgViaggio.setImage(image);
    	Double ratio = image.getWidth()/image.getHeight();
    	if(image.getWidth() > image.getHeight()) {
    		imgViaggio.setFitWidth(400);
    		imgViaggio.setFitHeight(0.0);    		
    		VBox.setMargin(hbEventData, new Insets(-(400/ratio - 170), 0, 0, 0));
		} else {
			imgViaggio.setFitHeight(170);
			imgViaggio.setFitWidth(0.0);
		}
    	imgViaggio.setPreserveRatio(true);
    	
    	InstantTooltip imgTooltip = new InstantTooltip(Main.getBundle().getString("see_event_details"));
    	Tooltip.install(imgViaggio, imgTooltip);
    	
    	lblTitle.setText(eventBean.getTitle());
    	lblDepartureCity.setText(eventBean.getDepartureCity());
    	lblDestinationCity.setText(eventBean.getDestinationCity());
    	if(eventBean.getTags().isEmpty())
    		lblTags.setText("No tags!");
    	else {
    	    lblTags.setText(String.join(", ", eventBean.getTags()));
    	    InstantTooltip tp = new InstantTooltip(lblTags.getText());
    		lblTags.setTooltip(tp);
    	}
    	lblDepartureDate.setText(ConvertMethods.dateToLocalFormat(eventBean.getDepartureDate()));
    	lblReturnDate.setText(ConvertMethods.dateToLocalFormat(eventBean.getReturnDate()));
    	lblOwnerUsername.setText(eventBean.getOwnerUsername());
    	
    	Platform.runLater(() -> {
    		AnchorPane.setLeftAnchor(lblTitle, (400-lblTitle.getWidth())/2);
        	AnchorPane.setRightAnchor(lblTitle, (400-lblTitle.getWidth())/2);
    	});
    	  
    	
		
		List<String> participants;
		InstantTooltip participantsTooltip = null;
		
		try {
			EventCardControllerApp eventCardControllerApp = new EventCardControllerApp();
			participants = eventCardControllerApp.getEventParticipants(eventBean.getId());
			
			btnParticipants.setText(String.valueOf(participants.size()));
			StringBuilder participantList = new StringBuilder();
			for(String participant: participants)
				participantList.append(participant + "\n");
			
			participantsTooltip = new InstantTooltip(Main.getBundle().getString("participants") + ":\n\n" + participantList.toString());
			
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
			
			goHomepage();
		}catch(NoEventParticipantsException nepe) {
			participantsTooltip = new InstantTooltip(Main.getBundle().getString(ConstantStrings.NO_PARTICIPANTS));
			btnParticipants.setText("0");
		}
		
		btnParticipants.setTooltip(participantsTooltip);
		
		
		if(loggedUserId == -1) {
			imgStar.setVisible(false);
			btnJoin.setVisible(false);
		}
		else {
			try {
				EventCardControllerApp eventCardControllerApp = new EventCardControllerApp();
				setIsFavorite(eventCardControllerApp.isFavoriteEvent(loggedUserId, eventBean.getId()));
				
				setIsJoined(eventCardControllerApp.isJoinedEvent(loggedUserId, eventBean.getId()));
			} catch (InternalDBException idbe) {
				TimedAlert.show(AlertType.ERROR,
						Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
						Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
						idbe.getMessage(), Main.getLogFile());
				
				goHomepage();
			}
		}
    }

    @FXML
    void selectStar()
    {
    	
    	try {
    		EventCardControllerApp eventCardControllerApp = new EventCardControllerApp();
    		if(isFavorite) {
        		eventCardControllerApp.removeFavoriteEvent(loggedUserId, eventBean.getId());
        		
        		setIsFavorite(false);
        	} else {  
        		eventCardControllerApp.addFavoriteEvent(loggedUserId, eventBean.getId()); 
                
                setIsFavorite(true);
        	}
    	}catch(InternalDBException idbe) {
    		TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
			
    		goHomepage();
    	}
    	
    }
    
    @FXML
    private void goToEventDetails()
    {
    	Main.setRoot("EventDetails", eventBean);
    }
    
    @FXML
    private void joinEvent() 
    {
    	try {
    		EventCardControllerApp eventCardControllerApp = new EventCardControllerApp();
			if(isJoined) 
	    		eventCardControllerApp.removeUserParticipation(loggedUserId, eventBean.getId());
	    		
	    	else 
	    		eventCardControllerApp.addUserParticipation(loggedUserId, eventBean.getId());
	    
			
			setParticipantsText();
			setIsJoined(!isJoined);
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
		}
    }

	public void setIsFavorite(boolean isFavorite) 
	{
		this.isFavorite = isFavorite;
		if(isFavorite) {
			animation = new SpriteAnimation(
                    imgStar,
                    Duration.millis(200.0),
                    COUNT, COLUMNS,
                    OFFSET_X, OFFSET_Y,
                    WIDTH_HEIGHT);

            animation.setCycleCount(1);
            animation.play();
            
            favoriteTooltip.setText(Main.getBundle().getString("remove_from_favorites"));
		}
		else {
			if(animation != null)
    			animation.stop();
			imgStar.setViewport(new Rectangle2D(0, 0, WIDTH_HEIGHT, WIDTH_HEIGHT));
			
			favoriteTooltip.setText(Main.getBundle().getString("add_to_favorites"));
		}
	}
	
	private void setIsJoined(boolean isJoined) 
	{
		this.isJoined = isJoined;
		if(isJoined) {
			btnJoin.setText(Main.getBundle().getString("remove_participation"));
		}
		else {
			btnJoin.setText(Main.getBundle().getString("join"));
		}
	}
	
	private void setParticipantsText() 
	{
		List<String> participants;
		InstantTooltip participantsTooltip = null;
		
		try {
			EventCardControllerApp eventCardControllerApp = new EventCardControllerApp();
			participants = eventCardControllerApp.getEventParticipants(eventBean.getId());
			
			btnParticipants.setText(String.valueOf(participants.size()));
			StringBuilder participantList = new StringBuilder();
			for(String participant: participants)
				participantList.append(participant + "\n");
			
			participantsTooltip = new InstantTooltip(Main.getBundle().getString("participants") + ":\n\n" + participantList.toString());
			
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
			
			goHomepage();
		}catch(NoEventParticipantsException nepe) {
			participantsTooltip = new InstantTooltip(Main.getBundle().getString(ConstantStrings.NO_PARTICIPANTS));
			btnParticipants.setText("0");
		}
		
		btnParticipants.setTooltip(participantsTooltip);
	}
	
	private void goHomepage() 
	{
		Main.setRoot("Homepage");
	}
}
