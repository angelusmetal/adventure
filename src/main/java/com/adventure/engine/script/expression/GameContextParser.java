package com.adventure.engine.script.expression;

import java.util.List;

import com.adventure.engine.GameContext;
import com.adventure.engine.script.syntax.Expression;

/**
 * Parses
 * @author Rodrigo Fernández (angelusmetal@gmail.com)
 *
 */
public class GameContextParser extends ExpressionParser<GameContext> {

	VocabularyParser vocabularyParser = new VocabularyParser();
	
	@Override
	public GameContext parse(Expression expression) throws ExpressionParserException {
		GameContext context = new GameContext();
		// The entity parser needs to know about the context, so it is created
		// here...
		EntityParser entityParser = new EntityParser(context);
		
		// vocabulary definition should be compound
		List<Expression> nested = getNested(expression);
		
		for (Expression exp : nested) {
			String identifier = exp.getIdentifier();
			
			if ("@vocabulary".equals(identifier)) {
				context.setVocabulary(vocabularyParser.parse(exp));
			} else {
				entityParser.parse(exp);
			}
		}
		
		return context;
	}

}
