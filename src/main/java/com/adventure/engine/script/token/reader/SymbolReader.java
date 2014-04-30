package com.adventure.engine.script.token.reader;

import java.io.IOException;

import com.adventure.engine.script.token.LineColumnReader;
import com.adventure.engine.script.token.Token;
import com.adventure.engine.script.token.TokenType;

/**
 * Reads tokens matching to known symbols.
 * @author Rodrigo Fernandez (angelusmetal@gmail.com)
 */
public class SymbolReader implements TokenReader {

	TokenReader lineReader = new AbstractTokenReader() {
		@Override protected TokenType getType() { return null;	}
		@Override protected boolean belongs(int c) { return c != '\n'; }
	};

	@Override public Token getToken(LineColumnReader reader, int c) throws IOException {
		int line = reader.getLineNumber();
		int column = reader.getColumn();
		
		if (c == '(') {
			return charToken(TokenType.OPEN_PARENTHESES, reader, c, line, column);
		} else if (c == ')') {
			return charToken(TokenType.CLOSE_PARENTHESES, reader, c, line, column);
		} else if (c == '{') {
			return charToken(TokenType.OPEN_BRACE, reader, c, line, column);
		} else if (c == '}') {
			return charToken(TokenType.CLOSE_BRACE, reader, c, line, column);
		} else if (c == ';') {
			return charToken(TokenType.SEMICOLON, reader, c, line, column);
		} else if (c == ',') {
			return charToken(TokenType.COMMA_OP, reader, c, line, column);
		} else if (c == '.') {
			return charToken(TokenType.DOT_OP, reader, c, line, column);
		} else if (c == '+') {
			return charToken(TokenType.PLUS_OP, reader, c, line, column);
		} else if (c == '-') {
			return charToken(TokenType.MINUS_OP, reader, c, line, column);
		} else if (c == '*') {
			return charToken(TokenType.TIMES_OP, reader, c, line, column);
		} else if (c == '/') {
			if (isNextChar(reader, '/')) {
				lineReader.getToken(reader, c);
			} else {
				return charToken(TokenType.DIV_OP, reader, c, line, column);
			}
		} else if (c == '!') {
			if (isNextChar(reader, '=')) {
				return new Token(TokenType.NEQ_OP, "!=", line, column);
			} else {
				return charToken(TokenType.NOT_OP, reader, c, line, column);
			}
		} else if (c == '>') {
			if (isNextChar(reader, '=')) {
				return new Token(TokenType.GET_OP, ">=", line, column);
			} else {
				return charToken(TokenType.GT_OP, reader, c, line, column);
			}
		} else if (c == '<') {
			if (isNextChar(reader, '=')) {
				return new Token(TokenType.LET_OP, "<=", line, column);
			} else {
				return charToken(TokenType.LT_OP, reader, c, line, column);
			}
		} else if (c == '=') {
			if (isNextChar(reader, '=')) {
				return new Token(TokenType.EQ_OP, "==", line, column);
			} else {
				return charToken(TokenType.ASSIGN_OP, reader, c, line, column);
			}
		}
		return null;
	}

	/**
	 * Verifies whether the next character matches the expected one. If it does,
	 * it is consumed from the stream. Otherwise, the stream is reset.
	 */
	private boolean isNextChar(LineColumnReader reader, int expected) throws IOException {
		if (reader.ready()) {
			reader.mark(1);
			int actual = reader.read();
			if (actual == expected) {
				return true;
			} else {
				reader.reset();
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * Create a token where value is a String constructed from a single char.
	 */
	private Token charToken(TokenType type, LineColumnReader reader, int c, int line, int column) {
		return new Token(type, new String(new char[] {(char)c}), line, column);
	}
	
}
