package com.ht.freeframe.exception;

public class DruidDataException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4944280164753568160L;

	public DruidDataException() {
		super();
	}

	public DruidDataException(String info) {
		super(info);
	}

	public DruidDataException(Throwable throwable) {
		super(throwable);
	}

	public DruidDataException(String info, Throwable throwable) {
		super(info, throwable);
	}
}
