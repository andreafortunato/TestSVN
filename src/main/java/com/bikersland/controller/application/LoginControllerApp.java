package com.bikersland.controller.application;

import java.sql.SQLException;

import com.bikersland.db.UserDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.InvalidLoginException;
import com.bikersland.model.User;
import com.bikersland.singleton.LoginSingleton;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.Main;
import com.bikersland.bean.UserBean;

public class LoginControllerApp {
	
	public void askLogin(String usernameEmail, String password) throws InvalidLoginException, InternalDBException 
	{
		User loggedUser;
		
		try {
			loggedUser = UserDAO.askLogin(usernameEmail, password);
			LoginSingleton.getLoginInstance().setUser(loggedUser);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "askLogin", "LoginControllerApp.java");
		}
	}
	
	public UserBean askLoginWeb(String usernameEmail, String password) throws InvalidLoginException, InternalDBException 
	{
		User user;
		try {
			user = UserDAO.askLogin(usernameEmail, password);
			
			return new UserBean(user.getId(), user.getName(), user.getSurname(), user.getUsername(), user.getEmail(), null, user.getImage(), user.getCreateTime());
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "askLogin", "LoginControllerApp.java");
		}
	}
	
	public void loginJustRegisteredUser(User user) 
	{
		LoginSingleton.getLoginInstance().setUser(user);
	}
}
