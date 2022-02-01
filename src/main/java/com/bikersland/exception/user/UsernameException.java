package com.bikersland.exception.user;

public class UsernameException extends UserException {

	private static final long serialVersionUID = 1L;
	
	public UsernameException()
	{
		super();
	}

	public UsernameException(String message) 
	{
		super(message);
	}
	
}
