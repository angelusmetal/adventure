package com.adventure.engine.script;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.adventure.engine.GameContext;
import com.adventure.engine.script.expression.ExpressionParserException;
import com.adventure.engine.script.expression.GameContextParser;
import com.adventure.engine.script.syntax.CompoundValue;
import com.adventure.engine.script.syntax.Expression;
import com.adventure.engine.script.syntax.SyntaxParser;
import com.adventure.engine.script.syntax.SyntaxParser.ScriptParsingException;

public class GameContextReader {

	SyntaxParser syntaxParser = new SyntaxParser();
	GameContextParser contextParser = new GameContextParser();
	
	/**
	 * Read an {@link InputStream} and create a {@link GameContext} from that.
	 * @param stream
	 * @return Initialized GameContext.
	 * @throws IOException If there is an IO error.
	 * @throws ScriptParsingException If script is malformed.
	 * @throws ExpressionParserException If syntax is invalid.
	 */
	public GameContext readFrom(InputStream stream) throws IOException, ScriptParsingException, ExpressionParserException {
		
		// Parse script
		List<Expression> expressions = syntaxParser.parse(stream);
		Expression root = new Expression("root", new CompoundValue(expressions), -1);
		
		// Parse root expression into a GameContext
		return contextParser.parse(root);
	}
	
}
