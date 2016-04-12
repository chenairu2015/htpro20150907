package com.ht2015.exception;

public class HTPropertiesException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HTPropertiesException() {
		super();
	}

	public HTPropertiesException(String info) {
		super(info);
	}

	public HTPropertiesException(Throwable throwable) {
		super(throwable);
	}

	public HTPropertiesException(String info, Throwable throwable) {
		super(info, throwable);
	}
}
