package com.bikersland.exception.user;

public class DuplicateUsernameException extends UsernameException {

	private static final long serialVersionUID = 1L;
	
	public DuplicateUsernameException() 
	{
		super();
	}

	public DuplicateUsernameException(String message) 
	{
		super(message);
	}

}
