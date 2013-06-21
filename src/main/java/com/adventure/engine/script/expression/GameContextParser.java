package com.adventure.engine.script.expression;

import java.util.List;

import com.adventure.engine.GameContext;
import com.adventure.engine.entity.Entity;
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
		
		List<Expression> nested = getNested(expression);

		// Parse context
		for (Expression exp : nested) {
			String identifier = exp.getIdentifier();
			
			if ("@vocabulary".equals(identifier)) {
				context.setVocabulary(vocabularyParser.parse(exp));
			} else {
				entityParser.parse(exp);
			}
		}
		
		// Configure start
		Entity start = context.getEntity("@start");
		
		String startingLocation = start.getProperty("location").getValue().getAsString();
		context.setCurrentLocation(context.getEntity(startingLocation));

		sanityCheck(entityParser, startingLocation, context);
		
		return context;
	}

	private boolean sanityCheck(EntityParser entityParser, String startingLocation, GameContext context) {
		boolean hasErrors = false;
//		boolean hasWarnings = false;

		// Check for dangling connections
		List<String> dangling = entityParser.getDanglingConnections();
		if (dangling.size() > 0) {
			hasErrors = true;
			context.getConsole().error("Error: The following entities are being referenced, but never defined:");
			for (String entity : dangling) {
				context.getConsole().error("  - " + entity);
			}
		}
		
		// Check for orphan entities
		List<String> orphan = entityParser.getOrphanEntities();
		orphan.remove(startingLocation);
		orphan.remove("@start");
		if (orphan.size() > 0) {
//			hasWarnings = true;
			context.getConsole().error("Warning: The following entitites are being defined, but never referenced:");
			for (String entity : orphan) {
				context.getConsole().error("  - " + entity);
			}
		}
		
		return hasErrors;
	}

}
