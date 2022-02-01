package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.List;

import com.bikersland.db.TagDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.Main;

public class HeaderControllerApp {
	
	public List<String> getTags() throws InternalDBException
	{
		try {
			return TagDAO.getTags();
		}catch(SQLException sqle) {
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getTags", "HeaderControllerApp.java");
		}
	}
}
