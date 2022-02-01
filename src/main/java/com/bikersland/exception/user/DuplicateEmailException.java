package com.bikersland.exception.user;

public class DuplicateEmailException extends EmailException {

	private static final long serialVersionUID = 1L;
	
	public DuplicateEmailException()
	{
		super();
	}

	public DuplicateEmailException(String message) 
	{
		super(message);
	}

}
