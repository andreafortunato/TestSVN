package com.bikersland.exception.user;

public class EmailException extends UserException{

	private static final long serialVersionUID = 1L;
	
	public EmailException() 
	{
		super();
	}

	public EmailException(String message) 
	{
		super(message);
	}

}
