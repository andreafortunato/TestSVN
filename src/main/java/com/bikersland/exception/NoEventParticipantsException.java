package com.bikersland.exception;

public class NoEventParticipantsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NoEventParticipantsException()
	{
		super();
	}

	public NoEventParticipantsException(String message)
	{
		super(message);
	}

}
