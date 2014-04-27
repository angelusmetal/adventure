package com.adventure.engine.script.tokenizer.reader;

import java.io.IOException;

import com.adventure.engine.script.tokenizer.LineColumnReader;
import com.adventure.engine.script.tokenizer.Token;
import com.adventure.engine.script.tokenizer.TokenType;

public class StringTokenReader implements TokenReader {

	private NumberTokenReader numberReader = new NumberTokenReader();
	
	public Token getToken(LineColumnReader reader, int c) throws IOException {
		int line = reader.getLineNumber();
		int column = reader.getColumn();
		StringBuilder sb = new StringBuilder();
		
		while (reader.ready()) {
			c = reader.read();
			if (c == '"') {
				// Found the closing quotes - they are neither included on the
				// token, nor reset on the stream
				break;
			} else if (c == '\\') {
				sb.append((char) readEscapedSequence(reader));
			} else {
				// Add character to string
				sb.append((char)c);
			}
		}
		
		// Return a token of the appropriate type
		return new Token(TokenType.STRING, sb.toString(), line, column);
	}

	private int readEscapedSequence(LineColumnReader reader) throws IOException {
		if (reader.ready()) {
			int c = reader.read();
			if (Character.isDigit(c)) {
				// Read character number value and create a character with that
				return Integer.parseInt(numberReader.readValue(reader, c));
			} else if (c == 'n') {
				return '\n';
			} else if (c == 'r') {
				return '\r';
			} else if (c == 't') {
				return '\t';
			} else {
				return c;
			}
		} else {
			return 0;
		}
	}
	
}
