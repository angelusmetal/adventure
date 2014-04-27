package com.adventure.engine.script.tokenizer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import com.adventure.engine.script.tokenizer.reader.AbstractTokenReader;
import com.adventure.engine.script.tokenizer.reader.StringTokenReader;
import com.adventure.engine.script.tokenizer.reader.TokenReader;

public class ScriptTokenizer {

	TokenReader identifierReader;
	TokenReader numberReader;
	TokenReader whitespaceReader;
	TokenReader lineReader;
	TokenReader stringReader;
	
	public ScriptTokenizer() {
		
		identifierReader = new AbstractTokenReader() {
			@Override protected TokenType getType() { return TokenType.IDENTIFIER; }
			@Override protected boolean belongs(int c) { return Character.isLetter(c); }
		};
		
		numberReader = new AbstractTokenReader() {
			@Override protected TokenType getType() { return TokenType.NUMBER; }
			@Override protected boolean belongs(int c) { return Character.isDigit(c); }
		};
		
		whitespaceReader = new AbstractTokenReader() {
			@Override protected TokenType getType() { return null;	}
			@Override protected boolean belongs(int c) { return Character.isWhitespace(c); }
		};
		
		lineReader = new AbstractTokenReader() {
			@Override protected TokenType getType() { return null;	}
			@Override protected boolean belongs(int c) { return c != '\n'; }
		};
		
		stringReader = new StringTokenReader();
	}
	
	public List<Token> tokenize(final InputStream stream) throws IOException {

		LineColumnReader reader = new LineColumnReader(new InputStreamReader(stream));
		
		try {
			List<Token> tokens = new LinkedList<Token>();
			
			while (reader.ready()) {
				int c = reader.read();
				
				if (Character.isWhitespace(c)) {
					whitespaceReader.getToken(reader, c);
				} else if (Character.isLetter(c)) {
					tokens.add(identifierReader.getToken(reader, c));
				} else if (Character.isDigit(c)) {
					tokens.add(numberReader.getToken(reader, c));
				} else if (c == '"') {
					tokens.add(stringReader.getToken(reader, c));
				} else {
					readSymbol(tokens, reader, c);
				}
			}
			
			return tokens;
			
		} finally {
			reader.close();
		}
	}
	
	private void readSymbol(List<Token> tokens, LineColumnReader reader, int c) throws IOException {
		if (c == '(') {
			tokens.add(charToken(TokenType.OPEN_PARENTHESES, reader, c));
		} else if (c == ')') {
			tokens.add(charToken(TokenType.CLOSE_PARENTHESES, reader, c));
		} else if (c == '{') {
			tokens.add(charToken(TokenType.OPEN_BRACE, reader, c));
		} else if (c == '}') {
			tokens.add(charToken(TokenType.CLOSE_BRACE, reader, c));
		} else if (c == ';') {
			tokens.add(charToken(TokenType.SEMICOLON, reader, c));
		} else if (c == ',') {
			tokens.add(charToken(TokenType.COMMA_OP, reader, c));
		} else if (c == '.') {
			tokens.add(charToken(TokenType.DOT_OP, reader, c));
		} else if (c == '+') {
			tokens.add(charToken(TokenType.PLUS_OP, reader, c));
		} else if (c == '-') {
			tokens.add(charToken(TokenType.MINUS_OP, reader, c));
		} else if (c == '*') {
			tokens.add(charToken(TokenType.TIMES_OP, reader, c));
		} else if (c == '/') {
			if (isNextChar(reader, '/')) {
				lineReader.getToken(reader, c);
			} else {
				tokens.add(charToken(TokenType.DIV_OP, reader, c));
			}
		} else if (c == '!') {
			if (isNextChar(reader, '=')) {
				tokens.add(new Token(TokenType.NEQ_OP, "!=", reader.getLineNumber(), reader.getColumn()));
			} else {
				tokens.add(charToken(TokenType.NOT_OP, reader, c));
			}
		} else if (c == '>') {
			if (isNextChar(reader, '=')) {
				tokens.add(new Token(TokenType.GET_OP, ">=", reader.getLineNumber(), reader.getColumn()));
			} else {
				tokens.add(charToken(TokenType.GT_OP, reader, c));
			}
		} else if (c == '<') {
			if (isNextChar(reader, '=')) {
				tokens.add(new Token(TokenType.LET_OP, "<=", reader.getLineNumber(), reader.getColumn()));
			} else {
				tokens.add(charToken(TokenType.LT_OP, reader, c));
			}
		} else if (c == '=') {
			if (isNextChar(reader, '=')) {
				tokens.add(new Token(TokenType.EQ_OP, "==", reader.getLineNumber(), reader.getColumn()));
			} else {
				tokens.add(charToken(TokenType.ASSIGN_OP, reader, c));
			}
		}
	}
	
	private boolean isNextChar(LineColumnReader reader, int c) throws IOException {
		if (reader.ready()) {
			reader.mark(1);
			int c2 = reader.read();
			if (c2 == c) {
				return true;
			} else {
				reader.reset();
				return false;
			}
		} else {
			return false;
		}
	}

	private Token charToken(TokenType type, LineColumnReader reader, int c) {
		return new Token(type, new String(new char[] {(char)c}), reader.getLineNumber(), reader.getColumn());
	}
	
	
	
}
