package com.adventure.engine.script;

import org.junit.Test;
import static org.junit.Assert.*;

public class ScriptParserTest {

	ScriptParser parser = new ScriptParser();
	
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
	
	@Test public void testParseExpression() {
	}
	
}
