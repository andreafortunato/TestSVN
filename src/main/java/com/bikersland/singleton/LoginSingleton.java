package com.bikersland.singleton;

import com.bikersland.model.User;

public class LoginSingleton {
	private static LoginSingleton loginInstance = null;
	
	private User user = null;
	
	private LoginSingleton() {}
	
	public static LoginSingleton getLoginInstance() 
	{
		if(LoginSingleton.loginInstance == null)
			LoginSingleton.loginInstance = new LoginSingleton();
		
		return loginInstance;
	}
	
	public Boolean isLogged()
	{
		return getUser() != null;
	}
	
	public User getUser()
	{
		return getLoginInstance().user;
	}
	
	public void setUser(User loggedUser) 
	{
		getLoginInstance().user = loggedUser;
	}
	
	public static void logout() 
	{
		getLoginInstance().user = null;
	}
}