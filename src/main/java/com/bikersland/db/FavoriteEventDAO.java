package com.bikersland.db;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.bikersland.db.queries.CRUDQueries;
import com.bikersland.db.queries.SimpleQueries;
import com.bikersland.model.Event;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class FavoriteEventDAO {
	
	private FavoriteEventDAO() {}
	
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
	
	
	public static Boolean isFavoriteEvent(Integer userId, Integer eventId) throws SQLException{
		Boolean isFavorite = false;

		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = SimpleQueries.isFavoriteEvent(stmt, userId, eventId);
			
			if(rs.next()) {
				isFavorite = true;
			}
			
			return isFavorite;
		} finally{
			if(rs != null) {
				rs.close();
			}
			
	        if (stmt != null)
	        	stmt.close();
		}
	}
	
	public static void addFavoriteEvent(Integer userId, Integer eventId) throws SQLException{
		
		Statement stmt = null;
		
		try {
			stmt = DBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
			CRUDQueries.addFavoriteEvent(stmt, userId, eventId);
		} finally {
			if (stmt != null)
	        	stmt.close();
		}
	}
	
	public static void removeFavoriteEvent(Integer userId, Integer eventId) throws SQLException{        
		Statement stmt = null;
       
		try {
			stmt = DBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	     
			CRUDQueries.removeFavoriteEvent(stmt, userId, eventId);
		} finally{
			if (stmt != null)
	        	stmt.close();
		}
	}
	
	public static List<Event> getFavoriteEventsByUser(Integer userId) throws SQLException{
		List<Event> eventList = new ArrayList<>();
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
			rs = SimpleQueries.getFavoriteEventsByUser(stmt, userId);
			
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
}
