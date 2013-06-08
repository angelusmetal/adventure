package com.adventure.engine.script;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.adventure.engine.script.ScriptParser.ScriptParsingException;
import com.adventure.engine.script.grammar.CompoundValue;
import com.adventure.engine.script.grammar.Expression;
import com.adventure.engine.script.grammar.ListValue;
import com.adventure.engine.script.grammar.SimpleValue;
import com.adventure.engine.script.grammar.Value;

public class ScriptParserTest {

	
	ScriptParser parser = new ScriptParser();
	@Mock LineAwareBufferedReader reader;
	
	@Before public void setUp() {
		initMocks(this);
	}
	
	@Test public void testSimpleValue() {
		Value value = parser.parseValue(" a value ");
		
		assertTrue(value.isSimple());
		assertEquals("a value", value.getAsString());
	}
	
	@Test public void testListValue() {
		Value value = parser.parseValue(" a list | of | values ");
		
		assertTrue(value .isList());
		assertEquals(3, value.getAsList().size());
		assertEquals("a list", value.getAsList().get(0));
		assertEquals("of", value.getAsList().get(1));
		assertEquals("values", value.getAsList().get(2));
	}
	
	@Test public void testParseSimpleExpression() throws ScriptParsingException, IOException {
		when(reader.readLine()).thenReturn("identifier: value");
		Expression expression = parser.parseExpression(reader, 0);
		
		String identifier = expression.getIdentifier();
		Value value = expression.getValue();
		
		assertEquals("identifier", identifier);
		assertTrue(value.isSimple());
		assertEquals("value", value.getAsString());
	}
	
	@Test public void testParseListExpression() throws ScriptParsingException, IOException {
		when(reader.readLine()).thenReturn("identifier: value1 | value 2 | value three ");
		Expression expression = parser.parseExpression(reader, 0);
		
		String identifier = expression.getIdentifier();
		Value value = expression.getValue();
		
		assertEquals("identifier", identifier);
		assertTrue(value.isList());
		assertEquals(3, value.getAsList().size());
		assertEquals("value1", value.getAsList().get(0));
		assertEquals("value 2", value.getAsList().get(1));
		assertEquals("value three", value.getAsList().get(2));
	}
	
	@Test public void testParseComplexExpression() throws ScriptParsingException, IOException {
		String input = "identifier:\n\tid1: value1 \n\tid2: value2 | value3\n";
		reader = new LineAwareBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
		Expression expression = parser.parseExpression(reader, 0);
		
		String identifier = expression.getIdentifier();
		Value value = expression.getValue();
		
		assertEquals("identifier", identifier);
		assertTrue(value.isCompound());
		assertEquals(2, value.getNested().size());
		assertEquals("id1", value.getNested().get(0).getIdentifier());
		assertTrue(value.getNested().get(0).getValue().isSimple());
		assertEquals("value1",value.getNested().get(0).getValue().getAsString());
		assertEquals("id2", value.getNested().get(1).getIdentifier());
		assertTrue(value.getNested().get(1).getValue().isList());
		assertEquals(2,value.getNested().get(1).getValue().getAsList().size());
		assertEquals("value2",value.getNested().get(1).getValue().getAsList().get(0));
		assertEquals("value3",value.getNested().get(1).getValue().getAsList().get(1));
	}
	
	@Test (expected=ScriptParsingException.class)
	public void testParseExpressionNoIdentifier() throws ScriptParsingException, IOException {
		when(reader.readLine()).thenReturn(":");
		Expression expression = parser.parseExpression(reader, 0);
		
		String identifier = expression.getIdentifier();
		Value value = expression.getValue();
		
		assertEquals("identifier", identifier);
		assertTrue(value.isSimple());
		assertEquals("value", value.getAsList());
	}
	
	@Test (expected=ScriptParsingException.class)
	public void testParseExpressionTooManySeparators() throws ScriptParsingException, IOException {
		when(reader.readLine()).thenReturn("1:2:3");
		Expression expression = parser.parseExpression(reader, 0);
		
		String identifier = expression.getIdentifier();
		Value value = expression.getValue();
		
		assertEquals("identifier", identifier);
		assertTrue(value.isSimple());
		assertEquals("value", value.getAsString());
	}
	
	@Test public void testParse() throws ScriptParsingException, IOException {
		String input = "identifier:\n\tid1: value1 \n\tid2: value2 | value3\nidentifier2: inline";
		reader = new LineAwareBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
		List<Expression> expressions = parser.parse(new ByteArrayInputStream(input.getBytes()));
		
		assertEquals(2, expressions.size());
		Expression expression = expressions.get(0);
		String identifier = expression.getIdentifier();
		Value value = expression.getValue();
		
		
		assertEquals("identifier", identifier);
		assertTrue(value.isCompound());
		assertEquals(2, value.getNested().size());
		assertEquals("id1", value.getNested().get(0).getIdentifier());
		assertTrue(value.getNested().get(0).getValue().isSimple());
		assertEquals("value1",value.getNested().get(0).getValue().getAsString());
		assertEquals("id2", value.getNested().get(1).getIdentifier());
		assertTrue(value.getNested().get(1).getValue().isList());
		assertEquals(2,value.getNested().get(1).getValue().getAsList().size());
		assertEquals("value2",value.getNested().get(1).getValue().getAsList().get(0));
		assertEquals("value3",value.getNested().get(1).getValue().getAsList().get(1));
		
		expression = expressions.get(1);
		identifier = expression.getIdentifier();
		value = expression.getValue();
		
		assertEquals("identifier2", identifier);
		assertTrue(value.isSimple());
		assertEquals("inline", value.getAsString());
	}
	
	@Test (expected=ScriptParsingException.class)
	public void testParseInvalidIndent() throws ScriptParsingException, IOException {
		//                                                                   This is on level 3 (wrong indentation)
		String input = "identifier:\n\tid1: value1 \n\tid2: value2 | value3\n\t\t\tidentifier2: inline";
		reader = new LineAwareBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
		parser.parse(new ByteArrayInputStream(input.getBytes()));
	}
	
}
