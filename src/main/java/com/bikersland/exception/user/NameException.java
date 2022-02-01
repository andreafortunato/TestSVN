package com.bikersland.exception.user;

public class NameException extends UserException {

	private static final long serialVersionUID = 1L;
	
	public NameException() 
	{
		super();
	}

	public NameException(String message) 
	{
		super(message);
	}
	
}
