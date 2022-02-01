package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.List;

import com.bikersland.db.EventDAO;
import com.bikersland.db.ParticipationDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.NoEventParticipantsException;
import com.bikersland.exception.event.EventNotFoundException;
import com.bikersland.model.Event;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.Main;
import com.bikersland.bean.EventBean;

import javafx.scene.image.Image;

public class EventDetailsControllerApp {
	
	public List<String> getEventParticipants(Integer eventId) throws InternalDBException, NoEventParticipantsException 
	{
		try {
			return ParticipationDAO.getParticipantsByEventId(eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getEventParticipants", ConstantStrings.EVENTDETAILSCONTROLLERAPP_JAVA);
		}
	}

	public Image getDefaultEventImage()
	{
		return new Image(Main.class.getResourceAsStream("img/background.jpg"));
	}

	public Boolean userJoinedEvent(Integer userId, Integer eventId) throws InternalDBException 
	{
		try {
			return ParticipationDAO.isJoinedEvent(userId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "userJoinedEvent", ConstantStrings.EVENTDETAILSCONTROLLERAPP_JAVA);
		} 
	}
	
	public void addUserParticipation(Integer userId, Integer eventId) throws InternalDBException 
	{
		try {
			ParticipationDAO.addUserParticipation(userId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "addUserParticipation", ConstantStrings.EVENTDETAILSCONTROLLERAPP_JAVA);
		}
	}
	
	public void removeUserParticipation(Integer userId, Integer eventId) throws InternalDBException 
	{
		try {
			ParticipationDAO.removeUserParticipation(userId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "removeUserParticipation", ConstantStrings.EVENTDETAILSCONTROLLERAPP_JAVA);
		}
	}
	
	public EventBean getEventById(Integer eventId) throws InternalDBException 
	{
		try {
			Event event = EventDAO.getEventById(eventId);
			
			return new EventBean(event.getId(), event.getTitle(), event.getDescription(),
					event.getOwnerUsername(), event.getDepartureCity(), event.getDestinationCity(),
					event.getDepartureDate(), event.getReturnDate(), event.getImage(), event.getCreateTime(), event.getTags());
		} catch (SQLException | EventNotFoundException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getEventById", ConstantStrings.EVENTDETAILSCONTROLLERAPP_JAVA);
		}
	}
}
