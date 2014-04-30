package com.adventure.engine.script.token.reader;

import java.io.IOException;

import com.adventure.engine.script.token.LineColumnReader;
import com.adventure.engine.script.token.Token;
import com.adventure.engine.script.token.TokenType;

/**
 * Provides basic token parsing where token appends characters until a non
 * matching character is found.
 * 
 * The criteria to determine whether a character belongs to the token, and the
 * resulting token type are both abstracted to derived classes.
 * 
 * @author Rodrigo Fernandez (angelusmetal@gmail.com)
 */
public abstract class AbstractTokenReader implements TokenReader {

	public Token getToken(LineColumnReader reader, int c) throws IOException {
		int line = reader.getLineNumber();
		int column = reader.getColumn();
		return new Token(getType(), readValue(reader, c), line, column);
	}
	
	public String readValue(LineColumnReader reader, int c) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append((char)c);
		reader.mark(1);
		
		while (reader.ready()) {
			c = reader.read();
			if (belongs(c)) {
				// Characters belonging to the token are appended
				reader.mark(1);
				sb.append((char)c);
			} else if (c == -1) {
				break; // EOF - No attempt to reset...
			} else {
				// Otherwise, rewind one char
				reader.reset();
				break;
			}
		}
		
		return sb.toString();
	}
	
	abstract protected TokenType getType();
	
	abstract protected boolean belongs(int c);
}
