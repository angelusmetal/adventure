package com.adventure.engine.script.syntax;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.adventure.engine.script.LineAwareBufferedReader;

public class SyntaxParser {

	public List<Expression> parse(final InputStream stream) throws IOException, ScriptParsingException {
		LineAwareBufferedReader reader = new LineAwareBufferedReader(new InputStreamReader(stream));
		return parseExpressionList(reader, 0);
	}
	
	List<Expression> parseExpressionList(LineAwareBufferedReader reader, int expectedLevel) throws ScriptParsingException, IOException {
		List<Expression> expressions = new ArrayList<Expression>();
		
		Expression expression = null;
		while (reader.ready()) {
			expression = parseExpression(reader, expectedLevel);
			if (expression != null) {
				expressions.add(expression);
			} else {
				break;
			}
		}
		
		return expressions;
	}

	Expression parseExpression(LineAwareBufferedReader reader, int expectedLevel) throws ScriptParsingException, IOException {
		skipBlankLines(reader);
		
		String line = reader.readLine();
		int currentLine = reader.getCurrentLine();
		
		if (line == null) {
			return null;
		}

		// Find indentation level
		int actualLevel = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == '\t') {
				actualLevel++;
			} else {
				break;
			}
		}
		
		// Check level
		if (actualLevel < expectedLevel) {
			// If line does not conform with expected level, put line back and stop parsing
			reader.putBack(line);
			return null;
		} else if (actualLevel > expectedLevel) {
			throw new ScriptParsingException(currentLine, "Wrong indentation");
		}
		
		String[] tokens = StringUtils.split(line.trim(),':');
		
		if (tokens.length == 0) {
			throw new ScriptParsingException(currentLine, "Expected identifier");
		} else if (tokens.length > 2) {
			throw new ScriptParsingException(currentLine, "Malformed expression. Too many ':' separators");
		}
		
		String identifier = tokens[0].trim();
		Value value;
		
		if (tokens.length == 1) {
			value = new CompoundValue(parseExpressionList(reader, expectedLevel + 1));
		} else {
			value = parseValue(tokens[1]);
		}
		
		return new Expression(identifier, value, currentLine);
	}
	
	Value parseValue(String simpleValue) {
		String[] tokens = StringUtils.split(simpleValue,'|');
		if (tokens.length == 1) {
			return new SimpleValue(tokens[0].trim());
		} else {
			return new ListValue(tokens);
		}
	}

	void skipBlankLines(LineAwareBufferedReader reader) throws IOException {
		String line = null;
		do {
			line = reader.readLine();
			if (line == null) {
				return;
			}
		} while (line.trim().isEmpty());
		reader.putBack(line);
	}
	
	public class ScriptParsingException extends Exception {

		private static final long serialVersionUID = 1L;
		public ScriptParsingException(int line, String msg) {
			super("Error at line "+ line + ": " + msg);
		}
	}
}
