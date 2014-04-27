package com.adventure.engine.script.tokenizer.reader;

import java.io.IOException;

import com.adventure.engine.script.tokenizer.LineColumnReader;
import com.adventure.engine.script.tokenizer.Token;

public interface TokenReader {

	public Token getToken(LineColumnReader reader, int c) throws IOException;
}
