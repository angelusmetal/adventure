package com.adventure.engine.script.token.reader;

import com.adventure.engine.script.token.TokenType;

/**
 * Reads identifier tokens. Identifier tokens are made of letters only.
 * @author Rodrigo Fernandez (angelusmetal@gmail.com)
 */
public class IdentifierTokenReader extends AbstractTokenReader {

	@Override
	protected TokenType getType() {
		return TokenType.IDENTIFIER;
	}

	@Override
	protected boolean belongs(int c) {
		return Character.isLetter(c);
	}

}
