package com.bikersland.exception.event;

public class EventNotFoundException extends EventException {

	private static final long serialVersionUID = 1L;
	
	public EventNotFoundException() 
	{
		super();
	}

	public EventNotFoundException(String message)
	{
		super(message);
	}

}
