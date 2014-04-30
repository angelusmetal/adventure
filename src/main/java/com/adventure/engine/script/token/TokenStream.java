package com.adventure.engine.script.token;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.adventure.engine.script.token.reader.AbstractTokenReader;
import com.adventure.engine.script.token.reader.IdentifierTokenReader;
import com.adventure.engine.script.token.reader.NumberTokenReader;
import com.adventure.engine.script.token.reader.StringTokenReader;
import com.adventure.engine.script.token.reader.SymbolReader;
import com.adventure.engine.script.token.reader.TokenReader;

public class TokenStream {

	TokenReader identifierReader = new IdentifierTokenReader();
	TokenReader numberReader = new NumberTokenReader();
	TokenReader stringReader = new StringTokenReader();
	TokenReader symbolReader = new SymbolReader();
	AbstractTokenReader whitespaceReader = new AbstractTokenReader() {
		@Override protected TokenType getType() { return null;	}
		@Override protected boolean belongs(int c) { return Character.isWhitespace(c); }
	};
	
	LineColumnReader reader;
	
	public TokenStream(final InputStream stream) {
		reader = new LineColumnReader(new InputStreamReader(stream));
	}
	
	public boolean ready() throws IOException {
		return reader.ready();
	}
	
	public Token read() throws IOException {
		try {
			
			while (reader.ready()) {
				int c = reader.read();
				
				if (Character.isWhitespace(c)) {
					whitespaceReader.readValue(reader, c);
				} else if (Character.isLetter(c)) {
					return identifierReader.getToken(reader, c);
				} else if (Character.isDigit(c)) {
					return numberReader.getToken(reader, c);
				} else if (c == '"') {
					return stringReader.getToken(reader, c);
				} else {
					return symbolReader.getToken(reader, c);
				}
			}
			return null;
			
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	public void close() throws IOException {
		reader.close();
	}
	
}
