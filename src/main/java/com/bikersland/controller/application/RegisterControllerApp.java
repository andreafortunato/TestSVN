package com.bikersland.controller.application;

import java.sql.SQLException;

import com.bikersland.bean.UserBean;
import com.bikersland.db.UserDAO;
import com.bikersland.exception.AutomaticLoginException;
import com.bikersland.exception.ImageConversionException;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.user.DuplicateEmailException;
import com.bikersland.exception.user.DuplicateUsernameException;
import com.bikersland.exception.user.UserNotFoundException;
import com.bikersland.model.User;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.Main;

public class RegisterControllerApp {
	
	public void register(UserBean userBean) throws DuplicateUsernameException, DuplicateEmailException, InternalDBException
	{
		User newUser = new User();
		newUser.setName(userBean.getName());
		newUser.setSurname(userBean.getSurname());
		newUser.setUsername(userBean.getUsername());
		newUser.setEmail(userBean.getEmail());
		newUser.setPassword(userBean.getPassword());
		newUser.setImage(userBean.getImage());
		
		try {
			UserDAO.createNewUser(newUser);
		} catch (SQLException | ImageConversionException e) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), e, "register", "RegisterControllerApp.java");
		}
		
		try {
			User loggedUser = UserDAO.getUserByUsername(newUser.getUsername());
			LoginControllerApp loginControllerApp = new LoginControllerApp();
			loginControllerApp.loginJustRegisteredUser(loggedUser);
		} catch (SQLException | ImageConversionException | UserNotFoundException e) {
			throw new AutomaticLoginException(Main.getBundle().getString("ex_failed_autologin"), e, "register", "RegisterControllerApp.java");
		}
	}
}
