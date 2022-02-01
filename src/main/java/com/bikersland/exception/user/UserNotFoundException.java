package com.bikersland.exception.user;

public class UserNotFoundException extends UserException {

	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException() 
	{
		super();
	}

	public UserNotFoundException(String message) 
	{
		super(message);
	}

}
