package com.bikersland.controller.graphics;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.bikersland.bean.UserBean;
import com.bikersland.controller.application.RegisterControllerApp;
import com.bikersland.exception.AutomaticLoginException;
import com.bikersland.exception.ImageFileException;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.user.DuplicateEmailException;
import com.bikersland.exception.user.DuplicateUsernameException;
import com.bikersland.exception.user.EmailException;
import com.bikersland.exception.user.NameException;
import com.bikersland.exception.user.PasswordException;
import com.bikersland.exception.user.SurnameException;
import com.bikersland.exception.user.UsernameException;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.utility.InstantTooltip;
import com.bikersland.utility.TimedAlert;
import com.bikersland.Main;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

public class RegisterControllerView
{

	@FXML
    private TextField txtName;

    @FXML
    private TextField txtSurname;

    @FXML
    private TextField txtUsername;

    @FXML
    private Button btnImage;

    @FXML
    private HBox hbImageSelected;

    @FXML
    private Label lblImageName;

    @FXML
    private TextField txtEmail1;

    @FXML
    private TextField txtEmail2;

    @FXML
    private PasswordField txtPassword1;

    @FXML
    private PasswordField txtPassword2;

    @FXML
    private Button btnRegister;
    
    private static final String TEXT_FIELD_ERROR = "text-field-error";
    
    private boolean validEmail = false;
    
    private InstantTooltip previewProfileImageTooltip = new InstantTooltip();
    private File imageFile = null;
    
    private UserBean userBean = new UserBean();
    
    private ChangeListener<String> checkEnableBtnRegister = (obs, oldVal, newVal) -> {
		List<TextField> textFields = Arrays.asList(txtName, txtSurname, txtUsername, txtEmail1, txtEmail2, txtPassword1, txtPassword2);
		
		for(TextField tf : textFields) {
			if(tf.getText().strip().length() == 0) {
				btnRegister.setDisable(true);
				return;
			}
		}
		
		if(!validEmail) {
			btnRegister.setDisable(true);
			return;
		}
		
		btnRegister.setDisable(false);
	};
	
	private ChangeListener<String> emailListener = (obs, oldVal, newVal) -> {
		try {
			/* L'email 'txtEmail1' deve essere valida, mentre l'email 'txtEmail2' deve essere uguale alla prima */
			userBean.setEmail(txtEmail1.getText());
			
			txtEmail1.getStyleClass().removeIf(style -> style.equals(TEXT_FIELD_ERROR));
			if(!txtEmail1.getText().strip().equals(txtEmail2.getText().strip())) {
				txtEmail2.getStyleClass().add(TEXT_FIELD_ERROR);
				validEmail = false;
			} else {
				txtEmail2.getStyleClass().removeIf(style -> style.equals(TEXT_FIELD_ERROR));
				validEmail = true;
			}
		} catch (EmailException e) {
			txtEmail1.getStyleClass().add(TEXT_FIELD_ERROR);
			validEmail = false;
		}
	};
	
	public void initialize() 
	{
		txtEmail1.textProperty().addListener(emailListener);
		txtEmail2.textProperty().addListener(emailListener);
		
		List<TextField> textFields = Arrays.asList(txtName, txtSurname, txtUsername, txtEmail1, txtEmail2, txtPassword1, txtPassword2);
		for(TextField tf : textFields)
			tf.textProperty().addListener(checkEnableBtnRegister);
		
		lblImageName.setTooltip(previewProfileImageTooltip);
		
		/* OFFICAL COMMENT BLOCK */
		/*
		try {
			ConvertMethods.addTextLimiter(txtName, 32);
			ConvertMethods.addTextLimiter(txtSurname, 32);
			ConvertMethods.addTextLimiter(txtUsername, 16);
			ConvertMethods.addTextLimiter(txtEmail1, 128);
			ConvertMethods.addTextLimiter(txtEmail2, 128);
			ConvertMethods.addTextLimiter(txtPassword1, 64);
			ConvertMethods.addTextLimiter(txtPassword2, 64);
		} catch (IOException e) {
			Logger.getGlobal().log(Level.SEVERE, "Catched IOException in initialize() method, inside LoginControllerView.java", e);
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString("timedalert_system_error_header"),
					Main.getBundle().getString("timedalert_system_error_content"), Main.getLogFile());
		}
		*/
	}
	
	@FXML
	private void register() 
	{
		if(!txtPassword1.getText().equals(txtPassword2.getText())) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_REGISTER_ERROR_TITLE),
					Main.getBundle().getString("timedalert_register_password_error_header"),
					Main.getBundle().getString("timedalert_register_password_error_content"), null);
						
			txtPassword2.setText("");
			txtPassword2.requestFocus();
			return;
		}
		
		try {
			userBean.setName(txtName.getText().strip());
			userBean.setSurname(txtSurname.getText().strip());
			userBean.setUsername(txtUsername.getText().strip());
			userBean.setPassword(txtPassword1.getText().strip());
			
			RegisterControllerApp registerControllerApp = new RegisterControllerApp();
			registerControllerApp.register(userBean);
			
			Main.setRoot("Homepage");
		} catch (DuplicateUsernameException due) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_REGISTER_ERROR_TITLE),
					Main.getBundle().getString("timedalert_register_error_username_header"),
					due.getMessage(), userBean.getUsername());
			txtUsername.setText("");
			txtUsername.requestFocus();
		} catch (DuplicateEmailException dee) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_REGISTER_ERROR_TITLE),
					Main.getBundle().getString("timedalert_register_error_email_header"),
					dee.getMessage(), userBean.getEmail());
			txtEmail1.setText("");
			txtEmail2.setText("");
			txtEmail1.requestFocus();
		} catch (NameException ne) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.ERROR),
					Main.getBundle().getString("timedalert_register_name_error"),
					ne.getMessage(), null);
		} catch (SurnameException se) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.ERROR),
					Main.getBundle().getString("timedalert_register_surname_error"),
					se.getMessage(), null);
		} catch (UsernameException ue) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.ERROR),
					Main.getBundle().getString("timedalert_register_username_error"),
					ue.getMessage(), null);
		} catch (PasswordException pe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.ERROR),
					Main.getBundle().getString("timedalert_register_password_error"),
					pe.getMessage(), null);
		} catch (AutomaticLoginException ale) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					ale.getMessage(), Main.getLogFile());
			
			Main.setRoot("Login");
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
		}
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
    	
    	File choosedFile = fileChooser.showOpenDialog(txtName.getParent().getScene().getWindow());
    	if(choosedFile != null) {
	    	try {
				userBean.setImage(choosedFile);
				
				this.imageFile = choosedFile;
				
				Image profileImage = new Image(this.imageFile.toURI().toString(), 100, 100, true, true);
				ImageView profileImagePreview = new ImageView(profileImage);
				Circle imgCircle = new Circle(50);
			    imgCircle.setCenterX(profileImage.getWidth()*0.5);
			    imgCircle.setCenterY(profileImage.getHeight()*0.5);
			    profileImagePreview.setClip(imgCircle);
				
				previewProfileImageTooltip.setGraphic(profileImagePreview);
				lblImageName.setText(this.imageFile.getName());
				btnImage.setVisible(false);
				hbImageSelected.setVisible(true);
			} catch (ImageFileException e) {
				TimedAlert.show(AlertType.ERROR,
						Main.getBundle().getString("timedalert_image_error_title"),
						Main.getBundle().getString("timedalert_image_error_header"),
						e.getMessage(), null);
			}
    	} else {
    		Image nullImage = null;
    		userBean.setImage(nullImage);
    	}
    }
    
    @FXML
    private void removeImage()
    {
    	this.imageFile = null;
    	hbImageSelected.setVisible(false);
    	btnImage.setVisible(true);
    	
    	Image nullImage = null;
		userBean.setImage(nullImage);
    }
}
