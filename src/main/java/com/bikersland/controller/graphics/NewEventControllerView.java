package com.bikersland.controller.graphics;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.SearchableComboBox;

import com.bikersland.bean.EventBean;
import com.bikersland.controller.application.NewEventControllerApp;
import com.bikersland.exception.ImageFileException;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.event.TitleException;
import com.bikersland.singleton.LoginSingleton;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.utility.CustomDateCell;
import com.bikersland.utility.InstantTooltip;
import com.bikersland.utility.TimedAlert;
import com.bikersland.Main;

import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

public class NewEventControllerView
{
	
	@FXML
    private CheckComboBox<String> comboTags;

    @FXML
    private SearchableComboBox<String> comboDepartureCity;

    @FXML
    private SearchableComboBox<String> comboDestinationCity;

    @FXML
    private DatePicker pickerDepartureDate;

    @FXML
    private DatePicker pickerReturnDate;

    @FXML
    private TextArea txtDescription;

    @FXML
    private Label lblCharacters;
    
    @FXML
    private TextField txtTitle;
    
    @FXML
    private Label lblImageName;
    
    @FXML
    private HBox hbImageSelected;
    
    private InstantTooltip previewEventImageTooltip = new InstantTooltip();
    
    private File imageFile = null;
    
    private EventBean eventBean = new EventBean();
    
    private static final int MAX_DESCRIPTION_CHARACTERS = 250;
    
    public void initialize()
    {
    	comboDepartureCity.getItems().addAll(Main.getCities());
    	comboDestinationCity.getItems().addAll(Main.getCities());
    	comboTags.getItems().addAll(Main.getTags());
    	
    	/* Disabilito le date precedenti a quella odierna */
    	pickerDepartureDate.setDayCellFactory(picker -> new CustomDateCell(LocalDateTime.now().toLocalDate()));
    	
    	lblImageName.setTooltip(previewEventImageTooltip);
    	
    	txtDescription.textProperty().addListener((obs, oldVal, newVal) -> {
    		if(newVal.length() > MAX_DESCRIPTION_CHARACTERS) {
    			txtDescription.setText(oldVal);
    			return;
    		}
    		
    		lblCharacters.setText(MAX_DESCRIPTION_CHARACTERS-newVal.length() + " " + Main.getBundle().getString("remaining_characters"));
    	});
    }
    
    @FXML
    private void enablePickerReturnDate()
    {
    	LocalDate departureDate = pickerDepartureDate.getValue();
    	pickerReturnDate.setValue(departureDate);
    	
    	pickerReturnDate.setDayCellFactory(picker -> new CustomDateCell(departureDate));
    	
    	if(pickerReturnDate.isDisable())
    		pickerReturnDate.setDisable(false);
    }

    @FXML
    private void uploadImage() 
    {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("Open Resource File");
    	fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.jpeg", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
            );
    	
    	File choosedFile = fileChooser.showOpenDialog(lblCharacters.getParent().getScene().getWindow());
    	
    	if(choosedFile != null) {
	    	try {
				eventBean.setImage(choosedFile);
				
				this.imageFile = choosedFile;
				
				previewEventImageTooltip.setGraphic(new ImageView(new Image(this.imageFile.toURI().toString(), 300, 300, true, true)));
    			lblImageName.setText(this.imageFile.getName());
    			hbImageSelected.setVisible(true);
			} catch (ImageFileException e) {
				TimedAlert.show(AlertType.ERROR,
						Main.getBundle().getString("timedalert_image_error_title"),
						Main.getBundle().getString("timedalert_image_error_header"),
						e.getMessage(), null);
			}
    	} else {
    		Image nullImage = null;
    		eventBean.setImage(nullImage);
    	}
    }
    
    @FXML
    private void removeImage() 
    {
    	this.imageFile = null;
    	hbImageSelected.setVisible(false);
    	
    	Image nullImage = null;
		eventBean.setImage(nullImage);
    }
    
    
    @FXML
    private void createEvent() 
    {    	
    	String emptyField = "";
    	boolean isEmpty = false;
    	if(txtTitle.getText().strip().length() == 0) {
    		emptyField = Main.getBundle().getString("title");
    		isEmpty = true;
    	}
    	else if(comboDepartureCity.getValue() == null){
    		emptyField = Main.getBundle().getString("departure_city_2");
    		isEmpty = true;
    	}
    	else if(comboDestinationCity.getValue() == null){
    		emptyField = Main.getBundle().getString("destination_city_2");
    		isEmpty = true;
    	}
    	else if(pickerDepartureDate.getValue() == null){
    		emptyField = Main.getBundle().getString("departure_date_2");
    		isEmpty = true;
    	}
    	else if(pickerReturnDate.getValue() == null){
    		emptyField = Main.getBundle().getString("return_date_2");
    		isEmpty = true;
    	}
    	
    	if(isEmpty) {
    		TimedAlert.show(AlertType.ERROR,
    				Main.getBundle().getString("timedalert_new_event_empty_error_title"),
    				Main.getBundle().getString("timedalert_new_event_empty_error_header"),
    				Main.getBundle().getString("timedalert_new_event_empty_error_content"), emptyField);
    		return;
    	}
    	
    	try {
    		eventBean.setTitle(txtTitle.getText().strip());
        	eventBean.setDescription(txtDescription.getText());
        	eventBean.setOwnerUsername(LoginSingleton.getLoginInstance().getUser().getUsername());
        	eventBean.setDepartureCity(comboDepartureCity.getValue());
        	eventBean.setDestinationCity(comboDestinationCity.getValue());
        	eventBean.setDepartureDate(Date.valueOf(pickerDepartureDate.getValue()));
        	eventBean.setReturnDate(Date.valueOf(pickerReturnDate.getValue()));
        	eventBean.setTags(comboTags.getCheckModel().getCheckedItems());
        	
        	NewEventControllerApp newEventControllerApp = new NewEventControllerApp();
        	EventBean createdEventBean = newEventControllerApp.createNewEvent(eventBean);
        	
    		TimedAlert.show(AlertType.INFORMATION,
    				Main.getBundle().getString("success"),
					Main.getBundle().getString("timedalert_new_event_success_header"),
					Main.getBundle().getString("timedalert_new_event_success_content"), createdEventBean.getTitle());
    		
    		Main.setRoot("EventDetails", createdEventBean);
		} catch (TitleException te) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.ERROR),
					Main.getBundle().getString("timedalert_event_title_error"),
					te.getMessage(), null);
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
		}
    }

}
