package com.disney.studios.api.challenge.exceptions;

public class DisneyServiceException extends Exception {
	
	private int exceptionCode;
	
    public void setExceptionCode(int exceptionCode)
    {
    	this.exceptionCode = exceptionCode;
    }
    
    public int getExceptionCode()
    {
    	return this.exceptionCode;
    }

	public DisneyServiceException() {
		super();
	}

	/**
	 * @param message
	 */
	public DisneyServiceException(String message) {
		super(message);
	}

}
