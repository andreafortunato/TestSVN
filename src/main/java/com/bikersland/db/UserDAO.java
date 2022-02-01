package com.bikersland.db;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;

import com.bikersland.db.queries.CRUDQueries;
import com.bikersland.db.queries.SimpleQueries;
import com.bikersland.exception.ImageConversionException;
import com.bikersland.exception.InvalidLoginException;
import com.bikersland.exception.user.DuplicateEmailException;
import com.bikersland.exception.user.DuplicateUsernameException;
import com.bikersland.exception.user.UserNotFoundException;
import com.bikersland.model.User;
import com.bikersland.Main;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class UserDAO {
	
	static final String ID_COL = "id";
	static final String NAME_COL = "name";
	static final String SURNAME_COL = "surname";
	static final String USERNAME_COL = "username";
	static final String EMAIL_COL = "email";
	static final String IMAGE_COL = "image";
	static final String CREATE_TIME_COL = "create_time";
	
	private UserDAO() {}
	
	public static void createNewUser(User user) throws DuplicateUsernameException, DuplicateEmailException, ImageConversionException, SQLException {	
		CallableStatement stmtCreateUser = null;
		try {
			stmtCreateUser = DBConnection.getConnection().prepareCall("{CALL CreateUser(?,?,?,?,?,?)}");
			CRUDQueries.createNewUserQuery(stmtCreateUser, user);
		} catch (SQLException ex) {
			int errorCode = ex.getErrorCode();
			if(errorCode == 1062) {
				/* Username o Email già esistenti */
				if(ex.getMessage().contains("username_UNIQUE")) {
					/* Username già presente */
					throw new DuplicateUsernameException(Main.getBundle().getString("ex_duplicate_username"));
				} else {
					/* Email già presente */
					throw new DuplicateEmailException(Main.getBundle().getString("ex_duplicate_email"));
				}
			}
			
			throw ex;
		} finally {
			if(stmtCreateUser != null)
				stmtCreateUser.close();
		}
	}
	
	public static User askLogin (String usernameOrEmail, String password) throws InvalidLoginException, SQLException {
		User user;
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = SimpleQueries.askLoginQuery(stmt, usernameOrEmail, password);
			
			if(rs.first()) {
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
				
				user = new User(rs.getInt(ID_COL), rs.getString(NAME_COL), rs.getString(SURNAME_COL), rs.getString(USERNAME_COL),
				rs.getString(EMAIL_COL), null, image, rs.getDate(CREATE_TIME_COL));
				
				return user;
			} else {
				throw new InvalidLoginException();
			}
		} finally {
			if(rs != null)
				rs.close();
			
			if (stmt != null)
				stmt.close();
		}
	}
	
	public static User getUserByUsername(String username) throws SQLException, ImageConversionException, UserNotFoundException {
		User user;
		
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			stmt = DBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        rs = SimpleQueries.getUserByUsernameQuery(stmt, username);
	        
	        if(rs.first()) {
	        	Image image;
				if(rs.getBinaryStream(IMAGE_COL) != null) {
		        	BufferedImage img;
					try {
						img = ImageIO.read(rs.getBinaryStream(IMAGE_COL));
					} catch (IOException e) {
						rs.close();
						stmt.close();
						throw new ImageConversionException(e);
					} 
		        	image = SwingFXUtils.toFXImage(img, null);
				} else {
					image = null;
				}
				
	        	user = new User(rs.getInt(ID_COL), rs.getString(NAME_COL), rs.getString(SURNAME_COL), rs.getString(USERNAME_COL),
	        			rs.getString(EMAIL_COL), null, image, rs.getDate(CREATE_TIME_COL));
	        	
	        	return user;
	        } else {
	        	throw new UserNotFoundException();
	        }
		} finally {
			if(rs != null)
				rs.close();
			
			if (stmt != null)
				stmt.close();
		}
	}
	
	public static void changeUserEmail(Integer userId, String userEmail) throws SQLException, UserNotFoundException, DuplicateEmailException {
		Statement stmt = null;
		
		int affectedRows;
		
		try {
			stmt = DBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			affectedRows = CRUDQueries.changeUserEmail(stmt, userId, userEmail);
	        
	        if(affectedRows != 1) {
	        	throw new UserNotFoundException();
	        }
		} catch (SQLException sqle) {
			int errorCode = sqle.getErrorCode();
			if(errorCode == 1062 && sqle.getMessage().contains("email_UNIQUE")) {
				/* Email già presente */
				throw new DuplicateEmailException(Main.getBundle().getString("ex_duplicate_email"));
			}
			
			throw sqle;
		} finally {
			if (stmt != null)
				stmt.close();
		}
	}
}
