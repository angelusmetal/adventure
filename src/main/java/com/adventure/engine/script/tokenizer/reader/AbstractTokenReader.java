package com.adventure.engine.script.tokenizer.reader;

import java.io.IOException;

import com.adventure.engine.script.tokenizer.LineColumnReader;
import com.adventure.engine.script.tokenizer.Token;
import com.adventure.engine.script.tokenizer.TokenType;

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
			} else {
				// Otherwise, rewind one char
				reader.reset();
				break;
			}
		}
		
		// Return a token of the appropriate type
		return sb.toString();
	}
	
	abstract protected TokenType getType();
	
	abstract protected boolean belongs(int c);
}
