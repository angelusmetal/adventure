package com.adventure.engine.script.grammar;

public class SimpleValue implements Value {

	final String value;
	
	public SimpleValue(final String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
