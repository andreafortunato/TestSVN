package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.bikersland.bean.EventBean;
import com.bikersland.db.EventDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.model.Event;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.Main;

public class HomepageControllerApp {
	
	public List<EventBean> getEventByCities(String departureCity, String destinationCity) throws InternalDBException 
	{
		List<Event> eventList = new ArrayList<>();
		try {
			eventList = EventDAO.getEventByCities(departureCity, destinationCity);
		} catch (SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getEventByCities", "HomepageControllerApp.java");
		}
		
		List<EventBean> eventBeanList = new ArrayList<>();
		
		for(Event event:eventList) {
			
			eventBeanList.add(new EventBean(event.getId(), event.getTitle(), event.getDescription(), event.getOwnerUsername(),
        			event.getDepartureCity(), event.getDestinationCity(), event.getDepartureDate(),
        			event.getReturnDate(), event.getImage(), event.getCreateTime(), event.getTags()));
		}
		
		return eventBeanList;
	}

	public List<EventBean> getEventByCitiesAndTags(String departureCity, String destinationCity,List<String> tagList) throws InternalDBException 
	{
		List<Event> eventList = new ArrayList<>();
		
		try {
			if(Main.getLocale() == Locale.ITALIAN) {
				eventList = EventDAO.getEventByCitiesAndTags(departureCity, destinationCity, tagList,"it");
			}
			else {
				eventList = EventDAO.getEventByCitiesAndTags(departureCity, destinationCity, tagList,"en");
			}
		}catch(SQLException sqle){
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getEventByCitiesAndTags", "HomepageControllerApp.java");
		}
		
		List<EventBean> eventBeanList = new ArrayList<>();
		
		for(Event event:eventList) {
			
			eventBeanList.add(new EventBean(event.getId(), event.getTitle(), event.getDescription(), event.getOwnerUsername(),
        			event.getDepartureCity(), event.getDestinationCity(), event.getDepartureDate(),
        			event.getReturnDate(), event.getImage(), event.getCreateTime(), event.getTags()));
		}
		
		return eventBeanList;
	}
}
