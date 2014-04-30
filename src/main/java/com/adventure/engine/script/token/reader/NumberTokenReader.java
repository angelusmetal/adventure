package com.adventure.engine.script.token.reader;

import com.adventure.engine.script.token.TokenType;

/**
 * Reads number tokens.
 * @author Rodrigo Fernandez (angelusmetal@gmail.com)
 */
public class NumberTokenReader extends AbstractTokenReader {

	@Override
	protected TokenType getType() {
		return TokenType.NUMBER;
	}

	@Override
	protected boolean belongs(int c) {
		return Character.isDigit(c);
	}

}
