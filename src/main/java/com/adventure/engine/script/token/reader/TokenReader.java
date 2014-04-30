package com.adventure.engine.script.token.reader;

import java.io.IOException;

import com.adventure.engine.script.token.LineColumnReader;
import com.adventure.engine.script.token.Token;

public interface TokenReader {

	public Token getToken(LineColumnReader reader, int c) throws IOException;
}
