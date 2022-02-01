package com.bikersland.db.queries;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import javax.imageio.ImageIO;

import com.bikersland.exception.ImageConversionException;
import com.bikersland.model.Event;
import com.bikersland.model.User;

import javafx.embed.swing.SwingFXUtils;

public class CRUDQueries {
	
	private CRUDQueries() {}
	
	public static int createNewUserQuery(CallableStatement stmtCreateUser, User user) throws SQLException, ImageConversionException 
	{		
		stmtCreateUser.setString(1, user.getName());
		stmtCreateUser.setString(2, user.getSurname());
		stmtCreateUser.setString(3, user.getUsername());
		stmtCreateUser.setString(4, user.getEmail());
		stmtCreateUser.setString(5, user.getPassword());
		
		if(user.getImage() != null) {
			BufferedImage buffImg = SwingFXUtils.fromFXImage(user.getImage(), null);
			ByteArrayOutputStream bts = new ByteArrayOutputStream();
			try {
				ImageIO.write(buffImg,"png", bts);
			} catch (IOException e) {
				/* Se si verifica un problema nella conversione dell'immagine, lancio 
				   un'eccezione e gestisco l'errore come se fosse dovuto dal Database */
				throw new ImageConversionException(e);
			}
			InputStream blobImage = new ByteArrayInputStream(bts.toByteArray());
			
			stmtCreateUser.setBlob(6, blobImage);
		} else {
			stmtCreateUser.setNull(6, Types.BLOB);
		}
		
		return stmtCreateUser.executeUpdate();
	}
	
	public static ResultSet createNewEventQuery(CallableStatement stmtCreateEvent, Event event) throws SQLException, ImageConversionException 
	{		
		stmtCreateEvent.setString(1, event.getTitle());
		stmtCreateEvent.setString(2, event.getDescription());
		stmtCreateEvent.setString(3, event.getOwnerUsername());
		stmtCreateEvent.setString(4, event.getDepartureCity());
		stmtCreateEvent.setString(5, event.getDestinationCity());
		stmtCreateEvent.setDate(6, event.getDepartureDate());
		stmtCreateEvent.setDate(7, event.getReturnDate());
		
		if(event.getImage() != null) {
			BufferedImage buffImg = SwingFXUtils.fromFXImage(event.getImage(), null);
			ByteArrayOutputStream bts = new ByteArrayOutputStream();
			try {
				ImageIO.write(buffImg,"png", bts);
			} catch (IOException e) {
				/* Se si verifica un problema nella conversione dell'immagine, lancio 
				   un'eccezione e gestisco l'errore come se fosse dovuto dal Database */
				throw new ImageConversionException(e);
			}
			InputStream blobImage = new ByteArrayInputStream(bts.toByteArray());
			
			stmtCreateEvent.setBlob(8, blobImage);
		} else {
			stmtCreateEvent.setNull(8, Types.BLOB);
		}
		
		return stmtCreateEvent.executeQuery();
	}
	
	public static int addEventTagsQuery(Statement stmtAddEventTags, Integer eventId, List<Integer> eventTagIdList) throws SQLException 
	{
		StringBuilder queryBuilder = new StringBuilder("INSERT INTO event_tag VALUES ");
        for(Integer tagId: eventTagIdList)
        	queryBuilder.append("(" + eventId + ", '" + tagId + "'), ");
        
        String query = queryBuilder.toString();
        query = query.substring(0, query.length()-2) + ";";
                
		return stmtAddEventTags.executeUpdate(query);
	}
	
	public static int addUserParticipationQuery(Statement stmt, Integer userId, Integer eventId) throws SQLException
	{
		String query = "INSERT INTO participation VALUES(" + userId + ", " + eventId + ");";
		return stmt.executeUpdate(query);
	}
	
	public static int removeUserParticipationQuery(Statement stmt, Integer userId, Integer eventId) throws SQLException
	{
		String query = "DELETE FROM participation WHERE user_id=" + userId + " AND event_id=" + eventId + ";";
		return stmt.executeUpdate(query);
	}
	
	public static int removeFavoriteEvent(Statement stmt, Integer userId, Integer eventId) throws SQLException
	{
		String query = "DELETE FROM favorite_event WHERE user_id=" + userId + " AND event_id=" + eventId + ";";
		
		return stmt.executeUpdate(query);
	}
	
	public static int addFavoriteEvent(Statement stmt, Integer userId, Integer eventId) throws SQLException
	{
		String query = "INSERT INTO favorite_event VALUES(" + userId + ", " + eventId + ");";
		
		return stmt.executeUpdate(query);
	}
	
	public static int changeUserEmail(Statement stmt, Integer userId, String userEmail) throws SQLException 
	{
		String query = "UPDATE user SET email='" + userEmail + "' WHERE id=" + userId + ";";
		
		return stmt.executeUpdate(query);
	}
}
