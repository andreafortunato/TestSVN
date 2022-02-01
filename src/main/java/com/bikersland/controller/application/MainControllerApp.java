package com.bikersland.controller.application;

import java.sql.SQLException;
import java.util.List;

import com.bikersland.db.CityDAO;
import com.bikersland.db.TagDAO;
import com.bikersland.exception.InternalDBException;
import com.bikersland.utility.ConstantStrings;
import com.bikersland.Main;

public class MainControllerApp {
	
	public List<String> getTags() throws InternalDBException 
	{
			try {
				return TagDAO.getTags();
			} catch (SQLException sqle) {
				throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getTags", "MainControllerApp.java");
			}
		}
	
	public List<String> getCities() throws InternalDBException 
	{
		try {
			return CityDAO.getCities();
		} catch (SQLException sqle){
			throw new InternalDBException(Main.getBundle().getString(ConstantStrings.EX_INTERNAL_DB_ERROR), sqle, "getCities", "MainControllerApp.java");
		}
	}
}
