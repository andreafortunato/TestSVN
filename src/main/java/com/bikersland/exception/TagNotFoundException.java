package com.bikersland.exception;

public class TagNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TagNotFoundException()
	{
		super();
	}

	public TagNotFoundException(String message)
	{
		super(message);
	}

}
