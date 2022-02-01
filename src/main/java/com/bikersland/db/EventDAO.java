package com.bikersland.db;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.bikersland.db.queries.CRUDQueries;
import com.bikersland.db.queries.SimpleQueries;
import com.bikersland.exception.ImageConversionException;
import com.bikersland.exception.event.EventNotFoundException;
import com.bikersland.model.Event;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class EventDAO {
	
	private static final String ID_COL = "id";
	private static final String TITLE_COL = "title";
	private static final String DESCRIPTION_COL = "description";
	private static final String OWNER_USERNAME_COL = "owner_username";
	private static final String DEPARTURE_CITY_COL = "departure_city";
	private static final String DESTINATION_CITY_COL = "destination_city";
	private static final String DEPARTURE_DATE_COL = "departure_date";
	private static final String RETURN_DATE_COL = "return_date";
	private static final String IMAGE_COL = "image";
	private static final String CREATE_TIME_COL = "create_time";
	
	private EventDAO() {}
	
	public static Event createNewEvent(Event event) throws SQLException, ImageConversionException, EventNotFoundException {
		CallableStatement stmtCreateEvent = null;
		ResultSet createNewEventRS = null;
		
		try {
			stmtCreateEvent = DBConnection.getConnection().prepareCall("{CALL CreateEvent(?,?,?,?,?,?,?,?)}");
			createNewEventRS = CRUDQueries.createNewEventQuery(stmtCreateEvent, event);
			
			if(createNewEventRS.next()) {
				Image image;
				if(createNewEventRS.getBinaryStream(IMAGE_COL) != null) {
					try {
						BufferedImage img = ImageIO.read(createNewEventRS.getBinaryStream(IMAGE_COL));
						image = SwingFXUtils.toFXImage(img, null);
					} catch (IOException e) {
						/* Se si verifica un problema nella conversione dell'immagine, lancio 
						   un'eccezione e gestisco l'errore come se fosse dovuto dal Database */
						createNewEventRS.close();
						stmtCreateEvent.close();
						throw new ImageConversionException(e);
					}
				} else {
					image = null;
				}
				
				return new Event(createNewEventRS.getInt(ID_COL), createNewEventRS.getString(TITLE_COL), createNewEventRS.getString(DESCRIPTION_COL),
						createNewEventRS.getString(OWNER_USERNAME_COL), createNewEventRS.getString(DEPARTURE_CITY_COL),
						createNewEventRS.getString(DESTINATION_CITY_COL), createNewEventRS.getDate(DEPARTURE_DATE_COL),
						createNewEventRS.getDate(RETURN_DATE_COL), image, createNewEventRS.getDate(CREATE_TIME_COL), event.getTags());
			}
			
			throw new EventNotFoundException();
			
		} finally {
			if(createNewEventRS != null)
				createNewEventRS.close();
			
			if(stmtCreateEvent != null)
				stmtCreateEvent.close();
		}
	}
		
	public static List<Event> getEventByCities(String departureCity, String destinationCity) throws SQLException {
		List<Event> eventList = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        rs = SimpleQueries.getEventByCities(stmt, departureCity, destinationCity);
	        
	        while(rs.next()) {
				Image image;
				if(rs.getBinaryStream(IMAGE_COL) != null) {
		        	BufferedImage img;
					try {
						img = ImageIO.read(rs.getBinaryStream(IMAGE_COL));
					} catch (IOException ioe) {
						rs.close();
						stmt.close();
					
						throw new SQLException(ioe);
					}
		        	image = SwingFXUtils.toFXImage(img, null);
				} else {
					image = null;
				}
				eventList.add(new Event(rs.getInt(ID_COL), rs.getString(TITLE_COL), rs.getString(DESCRIPTION_COL), rs.getString(OWNER_USERNAME_COL),
	        			rs.getString(DEPARTURE_CITY_COL), rs.getString(DESTINATION_CITY_COL), rs.getDate(DEPARTURE_DATE_COL),
	        			rs.getDate(RETURN_DATE_COL), image, rs.getDate(CREATE_TIME_COL), EventTagDAO.getEventTags(rs.getInt(ID_COL))));
			}
	        
	        return eventList;
		} finally {
			if (stmt != null)
				stmt.close();
			
			if(rs != null) 
				rs.close();
		}
		
		
	}	
	
	public static List<Event> getEventByCitiesAndTags(String departureCity, String destinationCity, List<String> tagList, String language) throws SQLException{
		
		if(tagList.isEmpty()) 
			return getEventByCities(departureCity, destinationCity);

		List<Event> eventList = new ArrayList<>();
		
		ResultSet rs = null;
		Statement stmt = null;
		
		
		try {
			stmt = DBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        rs = SimpleQueries.getEventByCitiesAndTags(stmt, departureCity, destinationCity, tagList, language);
	        
	        while(rs.next()) {
				Image image;
				if(rs.getBinaryStream(IMAGE_COL) != null) {
		        	BufferedImage img;
					try {
						img = ImageIO.read(rs.getBinaryStream(IMAGE_COL));
					} catch (IOException ioe) {
						rs.close();
						stmt.close();
					
						throw new SQLException(ioe);
					} 
		        	image = SwingFXUtils.toFXImage(img, null);
				} else {
					image = null;
				}
				eventList.add(new Event(rs.getInt(ID_COL), rs.getString(TITLE_COL), rs.getString(DESCRIPTION_COL), rs.getString(OWNER_USERNAME_COL),
	        			rs.getString(DEPARTURE_CITY_COL), rs.getString(DESTINATION_CITY_COL), rs.getDate(DEPARTURE_DATE_COL),
	        			rs.getDate(RETURN_DATE_COL), image, rs.getDate(CREATE_TIME_COL), EventTagDAO.getEventTags(rs.getInt(ID_COL))));
			}
	        
	        return eventList;
		}finally {
			if (stmt != null)
				stmt.close();
			
			if(rs != null) 
				rs.close();
		}
	}
	
	public static Event getEventById(Integer eventId) throws SQLException, EventNotFoundException {	
		ResultSet rs = null;
		Statement stmt = null;
		
		try {
			stmt = DBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        rs = SimpleQueries.getEventById(stmt, eventId);
	        
	        if(rs.next()) {
				Image image;
				if(rs.getBinaryStream(IMAGE_COL) != null) {
		        	BufferedImage img;
					try {
						img = ImageIO.read(rs.getBinaryStream(IMAGE_COL));
					} catch (IOException ioe) {
						rs.close();
						stmt.close();
					
						throw new SQLException(ioe);
					} 
		        	image = SwingFXUtils.toFXImage(img, null);
				} else {
					image = null;
				}
				
				return new Event(rs.getInt(ID_COL), rs.getString(TITLE_COL), rs.getString(DESCRIPTION_COL), rs.getString(OWNER_USERNAME_COL),
	        			rs.getString(DEPARTURE_CITY_COL), rs.getString(DESTINATION_CITY_COL), rs.getDate(DEPARTURE_DATE_COL),
	        			rs.getDate(RETURN_DATE_COL), image, rs.getDate(CREATE_TIME_COL), EventTagDAO.getEventTags(rs.getInt(ID_COL)));
			} else {
				throw new EventNotFoundException();
			}
		} finally {
			if (stmt != null)
				stmt.close();
			
			if(rs != null) 
				rs.close();
		}
	}
}
