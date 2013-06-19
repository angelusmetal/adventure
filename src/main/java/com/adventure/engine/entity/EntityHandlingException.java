package com.adventure.engine.entity;

public class EntityHandlingException extends Exception {

	private static final long serialVersionUID = 1L;

	public EntityHandlingException(String msg) {
		super(msg);
	}
	
	public EntityHandlingException(Throwable e) {
		super(e);
	}
}
