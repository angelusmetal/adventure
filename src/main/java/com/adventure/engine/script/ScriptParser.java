package com.adventure.engine.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class ScriptParser {

	public List<Expression> parse(final InputStream stream) throws IOException, ScriptParsingException {
		List<Expression> expressions = new ArrayList<Expression>();
		
		LineAwareBufferedReader reader = new LineAwareBufferedReader(new InputStreamReader(stream));

		Expression expression = null;
		while ((expression = parseExpression(reader, 0)) != null) {
			expressions.add(expression);
		}
		
		return expressions;
	}

	Expression parseExpression(LineAwareBufferedReader reader, int level) throws ScriptParsingException, IOException {
		String line = skipBlankLines(reader);
		
		String[] tokens = StringUtils.split(line,':');
		
		if (tokens.length == 0) {
			throw new ScriptParsingException(reader.getCurrentLine(), "Expected identifier");
		} else if (tokens.length > 2) {
			throw new ScriptParsingException(reader.getCurrentLine(), "Malformed expression. Too many ':' separators");
		}
		
		String identifier = tokens[0];
		Value value;
		
		if (tokens.length == 1) {
			// Expect value to be in the following lines
			//Value value = parseCompoundValue(reader, level + 1);
			value = new SimpleValue(""); // TODO cambiar
		} else {
			value = parseValue(tokens[1]);
		}
		
		return new Expression(identifier, value);
	}
	
	Value parseValue(String simpleValue) {
		String[] tokens = StringUtils.split(simpleValue,'|');
		if (tokens.length == 1) {
			return new SimpleValue(tokens[0].trim());
		} else {
			return new ListValue(tokens);
		}
	}

	String skipBlankLines(LineAwareBufferedReader reader) throws IOException {
		String line = null;
		do {
			line = reader.readLine();
			if (line == null) {
				return null;
			}
		} while (!line.trim().isEmpty());
		return line;
	}
	
	public class ScriptParsingException extends Exception {

		private static final long serialVersionUID = 1L;
		public ScriptParsingException(int line, String msg) {
			super("Error at line "+ line + ": " + msg);
		}
	}
}
