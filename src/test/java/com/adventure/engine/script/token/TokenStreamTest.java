package com.adventure.engine.script.token;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.adventure.engine.script.token.ScriptTokenizer;
import com.adventure.engine.script.token.Token;

import static org.junit.Assert.*;
import static com.adventure.engine.script.token.TokenType.*;

public class TokenStreamTest {

	ScriptTokenizer tokenizer = new ScriptTokenizer();
	TokenStream stream;
	List<Token> tokens;
	
	void getTokens() throws IOException {
		tokens = new LinkedList<Token>();
		while (stream.ready()) {
			Token token = stream.read();
			if (token != null) {
				tokens.add(token);
			}
		}
	}
	
	@Test public void testIdentifier() throws IOException {
		String input = "  one TWO.three2four  ";
		stream = new TokenStream(new ByteArrayInputStream(input.getBytes()));
		getTokens();
		
		assertEquals(6, tokens.size());
		
		assertEquals("one", tokens.get(0).getValue());
		assertEquals("TWO", tokens.get(1).getValue());
		assertEquals(".", tokens.get(2).getValue());
		assertEquals("three", tokens.get(3).getValue());
		assertEquals("2", tokens.get(4).getValue());
		assertEquals("four", tokens.get(5).getValue());
		
		assertEquals(IDENTIFIER, tokens.get(0).getType());
		assertEquals(IDENTIFIER, tokens.get(1).getType());
		assertEquals(DOT_OP, tokens.get(2).getType());
		assertEquals(IDENTIFIER, tokens.get(3).getType());
		assertEquals(NUMBER, tokens.get(4).getType());
		assertEquals(IDENTIFIER, tokens.get(5).getType());
		
	}
	
	@Test public void testNumber() throws IOException {
		String input = "  1 2.3a4  ";
		stream = new TokenStream(new ByteArrayInputStream(input.getBytes()));
		getTokens();
		
		assertEquals(6, tokens.size());
		
		assertEquals("1", tokens.get(0).getValue());
		assertEquals("2", tokens.get(1).getValue());
		assertEquals(".", tokens.get(2).getValue());
		assertEquals("3", tokens.get(3).getValue());
		assertEquals("a", tokens.get(4).getValue());
		assertEquals("4", tokens.get(5).getValue());
		
		assertEquals(NUMBER, tokens.get(0).getType());
		assertEquals(NUMBER, tokens.get(1).getType());
		assertEquals(DOT_OP, tokens.get(2).getType());
		assertEquals(NUMBER, tokens.get(3).getType());
		assertEquals(IDENTIFIER, tokens.get(4).getType());
		assertEquals(NUMBER, tokens.get(5).getType());
		
	}
	
	@Test public void testString() throws IOException {
		String input = "identifier \"Some \\\"quoted\\\" text \\n \\t \\r \\123 \" 123";
		stream = new TokenStream(new ByteArrayInputStream(input.getBytes()));
		getTokens();
		
		assertEquals(3, tokens.size());
		
		assertEquals("identifier", tokens.get(0).getValue());
		assertEquals("Some \"quoted\" text \n \t \r { ", tokens.get(1).getValue());
		assertEquals("123", tokens.get(2).getValue());
		
		assertEquals(IDENTIFIER, tokens.get(0).getType());
		assertEquals(STRING, tokens.get(1).getType());
		assertEquals(NUMBER, tokens.get(2).getType());
		
	}
	
	@Test public void testSpecialChars() throws IOException {
		String input = "first(second) third.fourth {fifth, sixth};";
		stream = new TokenStream(new ByteArrayInputStream(input.getBytes()));
		getTokens();
		
		assertEquals(13, tokens.size());
		
		assertEquals("first", tokens.get(0).getValue());
		assertEquals("(", tokens.get(1).getValue());
		assertEquals("second", tokens.get(2).getValue());
		assertEquals(")", tokens.get(3).getValue());
		assertEquals("third", tokens.get(4).getValue());
		assertEquals(".", tokens.get(5).getValue());
		assertEquals("fourth", tokens.get(6).getValue());
		assertEquals("{", tokens.get(7).getValue());
		assertEquals("fifth", tokens.get(8).getValue());
		assertEquals(",", tokens.get(9).getValue());
		assertEquals("sixth", tokens.get(10).getValue());
		assertEquals("}", tokens.get(11).getValue());
		assertEquals(";", tokens.get(12).getValue());
		
		assertEquals(IDENTIFIER, tokens.get(0).getType());
		assertEquals(OPEN_PARENTHESES, tokens.get(1).getType());
		assertEquals(IDENTIFIER, tokens.get(2).getType());
		assertEquals(CLOSE_PARENTHESES, tokens.get(3).getType());
		assertEquals(IDENTIFIER, tokens.get(4).getType());
		assertEquals(DOT_OP, tokens.get(5).getType());
		assertEquals(IDENTIFIER, tokens.get(6).getType());
		assertEquals(OPEN_BRACE, tokens.get(7).getType());
		assertEquals(IDENTIFIER, tokens.get(8).getType());
		assertEquals(COMMA_OP, tokens.get(9).getType());
		assertEquals(IDENTIFIER, tokens.get(10).getType());
		assertEquals(CLOSE_BRACE, tokens.get(11).getType());
		assertEquals(SEMICOLON, tokens.get(12).getType());
		
	}
	
	@Test public void testOperators() throws IOException {
		String input = "=a==b!c!=d>e>=f<g<=h+i-j*k/";
		stream = new TokenStream(new ByteArrayInputStream(input.getBytes()));
		getTokens();
		
		assertEquals(23, tokens.size());
		
		assertEquals("=", tokens.get(0).getValue());
		assertEquals("a", tokens.get(1).getValue());
		assertEquals("==", tokens.get(2).getValue());
		assertEquals("b", tokens.get(3).getValue());
		assertEquals("!", tokens.get(4).getValue());
		assertEquals("c", tokens.get(5).getValue());
		assertEquals("!=", tokens.get(6).getValue());
		assertEquals("d", tokens.get(7).getValue());
		assertEquals(">", tokens.get(8).getValue());
		assertEquals("e", tokens.get(9).getValue());
		assertEquals(">=", tokens.get(10).getValue());
		assertEquals("f", tokens.get(11).getValue());
		assertEquals("<", tokens.get(12).getValue());
		assertEquals("g", tokens.get(13).getValue());
		assertEquals("<=", tokens.get(14).getValue());
		assertEquals("h", tokens.get(15).getValue());
		assertEquals("+", tokens.get(16).getValue());
		assertEquals("i", tokens.get(17).getValue());
		assertEquals("-", tokens.get(18).getValue());
		assertEquals("j", tokens.get(19).getValue());
		assertEquals("*", tokens.get(20).getValue());
		assertEquals("k", tokens.get(21).getValue());
		assertEquals("/", tokens.get(22).getValue());
		
		assertEquals(ASSIGN_OP, tokens.get(0).getType());
		assertEquals(IDENTIFIER, tokens.get(1).getType());
		assertEquals(EQ_OP, tokens.get(2).getType());
		assertEquals(IDENTIFIER, tokens.get(3).getType());
		assertEquals(NOT_OP, tokens.get(4).getType());
		assertEquals(IDENTIFIER, tokens.get(5).getType());
		assertEquals(NEQ_OP, tokens.get(6).getType());
		assertEquals(IDENTIFIER, tokens.get(7).getType());
		assertEquals(GT_OP, tokens.get(8).getType());
		assertEquals(IDENTIFIER, tokens.get(9).getType());
		assertEquals(GET_OP, tokens.get(10).getType());
		assertEquals(IDENTIFIER, tokens.get(11).getType());
		assertEquals(LT_OP, tokens.get(12).getType());
		assertEquals(IDENTIFIER, tokens.get(13).getType());
		assertEquals(LET_OP, tokens.get(14).getType());
		assertEquals(IDENTIFIER, tokens.get(15).getType());
		assertEquals(PLUS_OP, tokens.get(16).getType());
		assertEquals(IDENTIFIER, tokens.get(17).getType());
		assertEquals(MINUS_OP, tokens.get(18).getType());
		assertEquals(IDENTIFIER, tokens.get(19).getType());
		assertEquals(TIMES_OP, tokens.get(20).getType());
		assertEquals(IDENTIFIER, tokens.get(21).getType());
		assertEquals(DIV_OP, tokens.get(22).getType());
		
	}
	
	@Test public void testComments() throws IOException {
		String input = "first // comment \n second // comment \n third // comment fourth";
		stream = new TokenStream(new ByteArrayInputStream(input.getBytes()));
		getTokens();
		
		assertEquals(3, tokens.size());
		
		assertEquals("first", tokens.get(0).getValue());
		assertEquals("second", tokens.get(1).getValue());
		assertEquals("third", tokens.get(2).getValue());
		
		assertEquals(IDENTIFIER, tokens.get(0).getType());
		assertEquals(IDENTIFIER, tokens.get(1).getType());
		assertEquals(IDENTIFIER, tokens.get(2).getType());
	}
	
	@Test public void testTokenizeFile() throws IOException {
		long start = System.nanoTime();
		stream = new TokenStream(new FileInputStream("sample3.fiction"));
		getTokens();
		long elapsed = System.nanoTime() - start;
		System.out.println("Elapse time: " + elapsed + " nanos");
		System.out.println(tokens.size() + " tokens...");
		System.out.println(tokens);
	}
}
