package com.bikersland.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
	private DBConnection() {}
	
	private static final String USER = "bikersland";
    private static final String PASS = "bikersland";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bikersland";
    private static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    
	private static Connection connection;
	
	public static Connection getConnection() throws SQLException {
		if(connection == null) {
			try {
				Class.forName(DRIVER_CLASS_NAME);
				connection = DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (SQLException | ClassNotFoundException e) {				
				throw new SQLException(e);
			}
		}
		
		if(connection.isClosed()) {
			try {
				connection = DriverManager.getConnection(DB_URL, USER, PASS);
			} catch (SQLException sqle) {				
				throw new SQLException(sqle);
			}
		}
		
		return connection;		
	}
}
