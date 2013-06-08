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
		assertTrue(value instanceof SimpleValue);
		SimpleValue simpleValue = (SimpleValue) value;
		assertEquals("a value", simpleValue.getValue());
	}
	
	@Test public void testListValue() {
		Value value = parser.parseValue(" a list | of | values ");
		assertTrue(value instanceof ListValue);
		ListValue simpleValue = (ListValue) value;
		assertEquals(3, simpleValue.getValues().size());
		assertEquals("a list", simpleValue.getValues().get(0));
		assertEquals("of", simpleValue.getValues().get(1));
		assertEquals("values", simpleValue.getValues().get(2));
	}
	
	@Test public void testParseSimpleExpression() throws ScriptParsingException, IOException {
		when(reader.readLine()).thenReturn("identifier: value");
		Expression expression = parser.parseExpression(reader, 0);
		assertEquals("identifier", expression.getIdentifier());
		assertTrue(expression.getValue() instanceof SimpleValue);
		assertEquals("value", ((SimpleValue)expression.getValue()).getValue());
	}
	
	@Test public void testParseListExpression() throws ScriptParsingException, IOException {
		when(reader.readLine()).thenReturn("identifier: value1 | value 2 | value three ");
		Expression expression = parser.parseExpression(reader, 0);
		assertEquals("identifier", expression.getIdentifier());
		assertTrue(expression.getValue() instanceof ListValue);
		assertEquals(3, ((ListValue)expression.getValue()).getValues().size());
		assertEquals("value1", ((ListValue)expression.getValue()).getValues().get(0));
		assertEquals("value 2", ((ListValue)expression.getValue()).getValues().get(1));
		assertEquals("value three", ((ListValue)expression.getValue()).getValues().get(2));
	}
	
	@Test public void testParseComplexExpression() throws ScriptParsingException, IOException {
		String input = "identifier:\n\tid1: value1 \n\tid2: value2 | value3\n";
		reader = new LineAwareBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
		Expression expression = parser.parseExpression(reader, 0);
		assertEquals("identifier", expression.getIdentifier());
		assertTrue(expression.getValue() instanceof CompoundValue);
		assertEquals(2, ((CompoundValue)expression.getValue()).getExpressions().size());
		assertEquals("id1", ((CompoundValue)expression.getValue()).getExpressions().get(0).getIdentifier());
		assertTrue(((CompoundValue)expression.getValue()).getExpressions().get(0).getValue() instanceof SimpleValue);
		assertEquals("value1",((SimpleValue)((CompoundValue)expression.getValue()).getExpressions().get(0).getValue()).getValue());
		assertEquals("id2", ((CompoundValue)expression.getValue()).getExpressions().get(1).getIdentifier());
		assertTrue(((CompoundValue)expression.getValue()).getExpressions().get(1).getValue() instanceof ListValue);
		assertEquals(2,((ListValue)((CompoundValue)expression.getValue()).getExpressions().get(1).getValue()).getValues().size());
		assertEquals("value2",((ListValue)((CompoundValue)expression.getValue()).getExpressions().get(1).getValue()).getValues().get(0));
		assertEquals("value3",((ListValue)((CompoundValue)expression.getValue()).getExpressions().get(1).getValue()).getValues().get(1));
	}
	
	@Test (expected=ScriptParsingException.class)
	public void testParseExpressionNoIdentifier() throws ScriptParsingException, IOException {
		when(reader.readLine()).thenReturn(":");
		Expression expression = parser.parseExpression(reader, 0);
		assertEquals("identifier", expression.getIdentifier());
		assertTrue(expression.getValue() instanceof SimpleValue);
		assertEquals("value", ((SimpleValue)expression.getValue()).getValue());
	}
	
	@Test (expected=ScriptParsingException.class)
	public void testParseExpressionTooManySeparators() throws ScriptParsingException, IOException {
		when(reader.readLine()).thenReturn("1:2:3");
		Expression expression = parser.parseExpression(reader, 0);
		assertEquals("identifier", expression.getIdentifier());
		assertTrue(expression.getValue() instanceof SimpleValue);
		assertEquals("value", ((SimpleValue)expression.getValue()).getValue());
	}
	
	@Test public void testParse() throws ScriptParsingException, IOException {
		String input = "identifier:\n\tid1: value1 \n\tid2: value2 | value3\nidentifier2: inline";
		reader = new LineAwareBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
		List<Expression> expressions = parser.parse(new ByteArrayInputStream(input.getBytes()));
		
		assertEquals(2, expressions.size());
		Expression expression = expressions.get(0);
		assertEquals("identifier", expression.getIdentifier());
		assertTrue(expression.getValue() instanceof CompoundValue);
		assertEquals(2, ((CompoundValue)expression.getValue()).getExpressions().size());
		assertEquals("id1", ((CompoundValue)expression.getValue()).getExpressions().get(0).getIdentifier());
		assertTrue(((CompoundValue)expression.getValue()).getExpressions().get(0).getValue() instanceof SimpleValue);
		assertEquals("value1",((SimpleValue)((CompoundValue)expression.getValue()).getExpressions().get(0).getValue()).getValue());
		assertEquals("id2", ((CompoundValue)expression.getValue()).getExpressions().get(1).getIdentifier());
		assertTrue(((CompoundValue)expression.getValue()).getExpressions().get(1).getValue() instanceof ListValue);
		assertEquals(2,((ListValue)((CompoundValue)expression.getValue()).getExpressions().get(1).getValue()).getValues().size());
		assertEquals("value2",((ListValue)((CompoundValue)expression.getValue()).getExpressions().get(1).getValue()).getValues().get(0));
		assertEquals("value3",((ListValue)((CompoundValue)expression.getValue()).getExpressions().get(1).getValue()).getValues().get(1));
		
		expression = expressions.get(1);
		assertEquals("identifier2", expression.getIdentifier());
		assertTrue(expression.getValue() instanceof SimpleValue);
		assertEquals("inline", ((SimpleValue)expression.getValue()).getValue());
	}
	
	@Test (expected=ScriptParsingException.class)
	public void testParseInvalidIndent() throws ScriptParsingException, IOException {
		//                                                                   This is on level 3 (wrong indentation)
		String input = "identifier:\n\tid1: value1 \n\tid2: value2 | value3\n\t\t\tidentifier2: inline";
		reader = new LineAwareBufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes())));
		parser.parse(new ByteArrayInputStream(input.getBytes()));
	}
	
}
