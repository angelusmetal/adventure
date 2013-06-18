package com.adventure.engine.script.syntax;

public class Expression {

	final private String identifier;
	final private Value value;
	final private int lineNumber;
	
	public Expression(final String identifier, final Value value, final int line) {
		this.identifier = identifier;
		this.value = value;
		this.lineNumber = line;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public Value getValue() {
		return value;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	@Override
	public String toString() {
		return "[" + identifier + " = " + value.toString() + " (at line " + lineNumber + ")]";
	}
}
