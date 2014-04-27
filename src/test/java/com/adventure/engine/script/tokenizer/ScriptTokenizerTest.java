package com.adventure.engine.script.tokenizer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.adventure.engine.script.tokenizer.ScriptTokenizer;
import com.adventure.engine.script.tokenizer.Token;

public class ScriptTokenizerTest {

	ScriptTokenizer tokenizer = new ScriptTokenizer();
	
	@Test public void test() throws IOException {
		String input = "one (TWO) {three} \n a.b = 4; // this is a comment \n var name = \"Robb \\n \\\"behaded\\\" Stark\"";
		List<Token> tokens = tokenizer.tokenize(new ByteArrayInputStream(input.getBytes()));
		System.out.println(tokens);
	}
}
