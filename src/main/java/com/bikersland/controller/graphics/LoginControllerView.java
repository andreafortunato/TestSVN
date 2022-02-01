package com.bikersland.controller.graphics;

import com.bikersland.controller.application.LoginControllerApp;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.InvalidLoginException;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.utility.TimedAlert;
import com.bikersland.Main;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginControllerView 
{
	
	@FXML
    private TextField txtUser;
	
	@FXML
    private PasswordField txtPassword;
	
	@FXML
	private Button btnLogin;
	
	private ChangeListener<String> checkEnableBtnLogin = (obs, oldVal, newVal) -> {
		if(txtUser.getText().strip().length() == 0 || txtPassword.getText().strip().length() == 0) {
			btnLogin.setDisable(true);
			return;
		}
		
		btnLogin.setDisable(false);
	};
	 
	public void initialize() 
	{
		txtUser.textProperty().addListener(checkEnableBtnLogin);
		txtPassword.textProperty().addListener(checkEnableBtnLogin);
		
		/* OFFICAL COMMENT BLOCK */
		/*
		try {
			ConvertMethods.addTextLimiter(txtUser, 32);
			ConvertMethods.addTextLimiter(txtPassword, 64);
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
	private void login() 
	{
		try {
			LoginControllerApp loginControllerApp = new LoginControllerApp();
			loginControllerApp.askLogin(txtUser.getText().strip(), txtPassword.getText().strip());
			
			Main.setRoot("Homepage");
		} catch (InvalidLoginException e) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString("timedalert_login_error_title"),
					Main.getBundle().getString("timedalert_login_error_header"),
					Main.getBundle().getString("timedalert_login_error_content"), null);
			txtUser.setText("");
			txtPassword.setText("");
			txtUser.requestFocus();
		} catch (InternalDBException idbe) {
			TimedAlert.show(AlertType.ERROR,
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_INTERNAL_ERROR),
					Main.getBundle().getString(ConstantStrings.TIMEDALERT_SQL_EX_HEADER),
					idbe.getMessage(), Main.getLogFile());
		} 
	}
}
