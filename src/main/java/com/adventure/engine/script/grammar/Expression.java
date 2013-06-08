package com.adventure.engine.script.grammar;

public class Expression {

	final private String identifier;
	final private Value value;
	
	public Expression(final String identifier, final Value value) {
		this.identifier = identifier;
		this.value = value;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public Value getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return "[" + identifier + " = " + value.toString() + "]";
	}
}
