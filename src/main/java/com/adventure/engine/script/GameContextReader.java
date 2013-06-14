package com.adventure.engine.script;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.adventure.engine.Entity;
import com.adventure.engine.GameContext;
import com.adventure.engine.script.ScriptParser.ScriptParsingException;
import com.adventure.engine.script.grammar.Expression;
import com.adventure.engine.script.grammar.Value;

public class GameContextReader {

	/**
	 * Read an {@link InputStream} and create a {@link GameContext} from that.
	 * @param stream
	 * @return
	 * @throws IOException
	 * @throws ScriptParsingException
	 * @throws GameContextReaderException
	 */
	public GameContext readFrom(InputStream stream) throws IOException, ScriptParsingException, GameContextReaderException {
		GameContext context = new GameContext();
		
		ScriptParser parser = new ScriptParser();
		
		List<Expression> expressions = parser.parse(stream);
		
		// First, create all entities so the wiring can be done
		for (Expression expression : expressions) {
			context.createEntity(expression.getIdentifier());
		}
		
		// Now that they are all created, populate them
		for (Expression expression : expressions) {
			String identifier = expression.getIdentifier();
			Value value = expression.getValue();
			
			// Ensure entities are defined by compound expressions
			Entity entity = context.getEntity(identifier);
			if (!value.isCompound()) {
				throw new GameContextReaderException("Entity '" + expression.getIdentifier() + "' should be defined by a compound expression.");
			}
			List<Expression> entityExpressions = value.getNested();
			
			// Parse the entity's expressions
			parseEntity(context, entity, entityExpressions);
		}
		
		return context;
	}
	
	public void parseEntity(GameContext context, Entity entity, List<Expression> expressions) throws GameContextReaderException {
		for (Expression expression : expressions) {
			String identifier = expression.getIdentifier();
			Value value = expression.getValue();
			// Parse special properties
			if ("connections".equals(identifier)) {
				addConnections(context, entity, value);
			} else if ("pickable".equals(identifier)) {
				entity.setPickable(value.getAsString().equals("true"));
			} else if ("visible".equals(identifier)) {
				entity.setVisible(value.getAsString().equals("true"));
			} else if ("traversable".equals(identifier)) {
				entity.setTraversable(value.getAsString().equals("true"));
			} else {
				// Otherwise, add to the pool of user defined properties
				entity.setProperty(expression.getIdentifier(), expression.getValue());
			}
		}
	}

	public void addConnections(GameContext context, Entity entity, Value value)
			throws GameContextReaderException {
		if (!value.isCompound()) {
			throw new GameContextReaderException("'connections' should be a compound expression.");
		}
		List<Expression> connections = value.getNested();
		for (Expression connection : connections) {
			if (connection.getValue().isCompound()) {
				throw new GameContextReaderException("A connection cannot be a compound expression. It should be identifier: label 1 | label 2 | ...");
			}
			Entity destination = context.getEntity(connection.getIdentifier());
			List<String> aliases = connection.getValue().getAsList();
			for (String alias : aliases) {
				entity.addEntity(destination, alias);
			}
		}
	}

	public class GameContextReaderException extends Exception {

		private static final long serialVersionUID = 1L;
		public GameContextReaderException(String msg) {
			super("Game context definition error: " + msg);
		}
	}
}
