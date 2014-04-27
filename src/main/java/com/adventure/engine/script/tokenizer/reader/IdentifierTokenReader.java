package com.adventure.engine.script.tokenizer.reader;

import com.adventure.engine.script.tokenizer.TokenType;

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
