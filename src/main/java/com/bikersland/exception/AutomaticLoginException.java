package com.bikersland.exception;

public class AutomaticLoginException extends InternalDBException {

	private static final long serialVersionUID = 1L;

	public AutomaticLoginException() 
	{
		super();
	}

	public AutomaticLoginException(String message) 
	{
		super(message);
	}

	public AutomaticLoginException(String message, Throwable e, String method, String file) 
	{
		super(message, e, method, file);
	}
	
}
