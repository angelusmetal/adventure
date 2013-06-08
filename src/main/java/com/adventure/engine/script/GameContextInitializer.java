package com.adventure.engine.script;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.adventure.engine.GameContext;
import com.adventure.engine.script.ScriptParser.ScriptParsingException;
import com.adventure.engine.script.grammar.Expression;

public class GameContextInitializer {

	public GameContext initialize(InputStream stream) throws IOException, ScriptParsingException {
		GameContext context = new GameContext();
		
		ScriptParser parser = new ScriptParser();
		
		List<Expression> expressions = parser.parse(stream);
		
		// First, create all entities so the wiring can be done
		for (Expression expression : expressions) {
			context.createEntity(expression.getIdentifier());
		}
		
		return context;
	}
}
