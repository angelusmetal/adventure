package com.adventure.engine.script.token;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import com.adventure.engine.script.token.reader.AbstractTokenReader;
import com.adventure.engine.script.token.reader.IdentifierTokenReader;
import com.adventure.engine.script.token.reader.NumberTokenReader;
import com.adventure.engine.script.token.reader.StringTokenReader;
import com.adventure.engine.script.token.reader.SymbolReader;
import com.adventure.engine.script.token.reader.TokenReader;

public class ScriptTokenizer {

	TokenReader identifierReader = new IdentifierTokenReader();
	TokenReader numberReader = new NumberTokenReader();
	AbstractTokenReader whitespaceReader;
	TokenReader stringReader = new StringTokenReader();
	TokenReader symbolReader = new SymbolReader();
	
	public ScriptTokenizer() {
		
		whitespaceReader = new AbstractTokenReader() {
			@Override protected TokenType getType() { return null;	}
			@Override protected boolean belongs(int c) { return Character.isWhitespace(c); }
		};
		
	}
	
	public List<Token> tokenize(final InputStream stream) throws IOException {

		LineColumnReader reader = new LineColumnReader(new InputStreamReader(stream));
		
		List<Token> tokens = new LinkedList<Token>();
		try {
			
			while (reader.ready()) {
				int c = reader.read();
				
				if (Character.isWhitespace(c)) {
					whitespaceReader.readValue(reader, c);
				} else if (Character.isLetter(c)) {
					tokens.add(identifierReader.getToken(reader, c));
				} else if (Character.isDigit(c)) {
					tokens.add(numberReader.getToken(reader, c));
				} else if (c == '"') {
					tokens.add(stringReader.getToken(reader, c));
				} else {
					Token symbol = symbolReader.getToken(reader, c);
					if (symbol != null) {
						tokens.add(symbol);
					}
				}
			}
			
			return tokens;
		
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			reader.close();
		}
	}
	
}
