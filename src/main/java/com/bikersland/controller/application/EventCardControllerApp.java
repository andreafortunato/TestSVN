package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.List;

import com.bikersland.db.FavoriteEventDAO;
import com.bikersland.db.ParticipationDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.exception.NoEventParticipantsException;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.Main;

import javafx.scene.image.Image;

public class EventCardControllerApp {
	
	public List<String> getEventParticipants(Integer eventId) throws InternalDBException, NoEventParticipantsException 
	{
		try {
			return ParticipationDAO.getParticipantsByEventId(eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getEventParticipants", ConstantStrings.EVENTCARDCONTROLLERAPP_JAVA);
		}
	}
	
	public Image getDefaultEventImage() 
	{
		return new Image(Main.class.getResourceAsStream("img/background.jpg"));
	}
	
	public Boolean isFavoriteEvent(Integer userId, Integer eventId) throws InternalDBException 
	{
		try {
			return FavoriteEventDAO.isFavoriteEvent(userId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "isFavoriteEvent", ConstantStrings.EVENTCARDCONTROLLERAPP_JAVA);
		}
	}

	public Boolean isJoinedEvent(Integer loggedUserId, Integer eventId) throws InternalDBException 
	{
		try {
			return ParticipationDAO.isJoinedEvent(loggedUserId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "isJoinedEvent", ConstantStrings.EVENTCARDCONTROLLERAPP_JAVA);
		}
	}

	public void removeFavoriteEvent(Integer loggedUserId, Integer eventId) throws InternalDBException 
	{
		try {
			FavoriteEventDAO.removeFavoriteEvent(loggedUserId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "removeFavoriteEvent", ConstantStrings.EVENTCARDCONTROLLERAPP_JAVA);
		}
	}

	public void addFavoriteEvent(Integer loggedUserId, Integer eventId) throws InternalDBException 
	{
		try {
			FavoriteEventDAO.addFavoriteEvent(loggedUserId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "addFavoriteEvent", ConstantStrings.EVENTCARDCONTROLLERAPP_JAVA);
		}
	}

	public void addUserParticipation(Integer userId, Integer eventId) throws InternalDBException 
	{
		try {
			ParticipationDAO.addUserParticipation(userId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "addUserParticipation", ConstantStrings.EVENTCARDCONTROLLERAPP_JAVA);
		}
	}
	
	public void removeUserParticipation(Integer userId, Integer eventId) throws InternalDBException 
	{
		try {
			ParticipationDAO.removeUserParticipation(userId, eventId);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "removeUserParticipation", ConstantStrings.EVENTCARDCONTROLLERAPP_JAVA);
		}
	}
}
