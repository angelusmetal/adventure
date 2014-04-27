package com.adventure.engine.script.tokenizer;

/**
 * Represents a token read from a source file.
 * @author Rodrigo Fernandez (angelusmetal@gmail.com)
 */
public class Token {
	
	// private final String sourceFile; // Just a thought
	private final TokenType type;
	private final String value;
	private final int line;
	private final int column;
	
	public Token (TokenType type, String value, int line, int column) {
		this.type = type;
		this.value = value;
		this.line = line;
		this.column = column;
	}

	/**
	 * Returns the corresponding {@link TokenType} for this token.
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Returns the string representation of this token. Operators will contain
	 * their symbols, numbers will contain their literals and Strings will
	 * contain their values (without the enclosing quotes and having their
	 * escaped characters already converted).
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the line number where this token occurred.
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Returns the column number where this token occurred.
	 */
	public int getColumn() {
		return column;
	}

	@Override
	public String toString() {
		return "(" + type + " '" + value + "' " + line
				+ ":" + column + ")";
	}
	
}
