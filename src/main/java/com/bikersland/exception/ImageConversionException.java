package com.bikersland.exception;

public class ImageConversionException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ImageConversionException() 
	{
		super();
	}

	public ImageConversionException(String message)
	{
		super(message);
	}

	public ImageConversionException(Throwable cause)
	{
		super(cause);
	}

}
