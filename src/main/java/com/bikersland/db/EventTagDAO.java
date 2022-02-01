package com.bikersland.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.bikersland.db.queries.CRUDQueries;
import com.bikersland.db.queries.SimpleQueries;
import com.bikersland.Main;

public class EventTagDAO {
	
	private EventTagDAO() {}
	
	public static void addEventTags(Integer eventId, List<Integer> tagList) throws SQLException {
        		
		Statement stmtAddEventTags = null;
		try {
			stmtAddEventTags = DBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
			CRUDQueries.addEventTagsQuery(stmtAddEventTags, eventId, tagList);
		} finally {
			if (stmtAddEventTags != null)
				stmtAddEventTags.close();
		}
	}
	
	public static List<String> getEventTags(Integer eventId) throws SQLException {
		if(Main.getLocale() == Locale.ITALIAN)
			return getEventTags(eventId, "it");
		else
			return getEventTags(eventId, "en");
	}
	
	private static List<String> getEventTags(Integer eventId, String language) throws SQLException {
		List<String> tagList = new ArrayList<>();
		
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = DBConnection.getConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = SimpleQueries.getEventTags(stmt, eventId, language);
			
			while(rs.next()) {
				tagList.add(rs.getString(language));
			}
			
			tagList.sort((s1, s2) -> s1.compareTo(s2));
			
			return tagList;
		} finally {
			if (stmt != null)
				stmt.close();
			
			if(rs != null) 
				rs.close();
		}
	}
}
