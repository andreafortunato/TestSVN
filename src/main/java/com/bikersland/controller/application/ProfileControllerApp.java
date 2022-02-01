package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bikersland.bean.EventBean;
import com.bikersland.bean.UserBean;
import com.bikersland.db.FavoriteEventDAO;
import com.bikersland.db.ParticipationDAO;
import com.bikersland.db.UserDAO;
import com.bikersland.exception.ImageConversionException;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.user.DuplicateEmailException;
import com.bikersland.exception.user.UserNotFoundException;
import com.bikersland.model.Event;
import com.bikersland.model.User;
import com.bikersland.singleton.LoginSingleton;
import com.bikersland.utility.ConstantStrings;

import javafx.scene.image.Image;

import com.bikersland.Main;

public class ProfileControllerApp {
	
	public List<EventBean> getJoinedEventsByUser(Integer userId) throws InternalDBException
	{
		List<Event> eventList = new ArrayList<>();
		try {
			eventList = ParticipationDAO.getJoinedEventsByUser(userId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getJoinedEventsByUser", ConstantStrings.PROFILECONTROLLERAPP_JAVA);
		}
		
		List<EventBean> eventBeanList = new ArrayList<>();
		
		for(Event event:eventList) {
			
			eventBeanList.add(new EventBean(event.getId(), event.getTitle(), event.getDescription(), event.getOwnerUsername(),
        			event.getDepartureCity(), event.getDestinationCity(), event.getDepartureDate(),
        			event.getReturnDate(), event.getImage(), event.getCreateTime(), event.getTags()));
		}
		
		return eventBeanList;
		
	}

	public List<EventBean> getFavoriteEventsByUser(Integer userId) throws InternalDBException 
	{
		List<Event> eventList = new ArrayList<>();
		try {
			eventList = FavoriteEventDAO.getFavoriteEventsByUser(userId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getFavoriteEventsByUser", ConstantStrings.PROFILECONTROLLERAPP_JAVA);
		}
		
		List<EventBean> eventBeanList = new ArrayList<>();
		
		for(Event event:eventList) {
			
			eventBeanList.add(new EventBean(event.getId(), event.getTitle(), event.getDescription(), event.getOwnerUsername(),
        			event.getDepartureCity(), event.getDestinationCity(), event.getDepartureDate(),
        			event.getReturnDate(), event.getImage(), event.getCreateTime(), event.getTags()));
		}
		
		return eventBeanList;
	}

	public UserBean getLoggedUser()
	{
		User user = LoginSingleton.getLoginInstance().getUser();
		
		return new UserBean(user.getId(), user.getName(), user.getSurname(), user.getUsername(),
				user.getEmail(), null, user.getImage(), user.getCreateTime());
	}
	
	public UserBean getUserByUsername(String username) throws InternalDBException
	{
		User user;
		try {
			user = UserDAO.getUserByUsername(username);
		} catch (SQLException | ImageConversionException | UserNotFoundException ex) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), ex, "getUserByUsername", ConstantStrings.PROFILECONTROLLERAPP_JAVA);
		}
		
		return new UserBean(user.getId(), user.getName(), user.getSurname(), user.getUsername(),
				user.getEmail(), null, user.getImage(), user.getCreateTime());
	}
	
	public Image getDefaultUserImage()
	{
		return new Image(Main.class.getResourceAsStream("img/profile_image.png"), 100, 100, true, true);
	}
	
	public void changeUserEmail(Integer userId, String userEmail) throws InternalDBException, DuplicateEmailException
	{
		try {
			UserDAO.changeUserEmail(userId, userEmail);
			
			if(LoginSingleton.getLoginInstance().getUser().getId().equals(userId))
				LoginSingleton.getLoginInstance().getUser().setEmail(userEmail);
		} catch (SQLException | UserNotFoundException ex) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), ex, "changeUserEmail", ConstantStrings.PROFILECONTROLLERAPP_JAVA);
		}
	}

}
