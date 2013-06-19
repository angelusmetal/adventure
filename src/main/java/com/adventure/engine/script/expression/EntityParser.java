package com.adventure.engine.script.expression;

import gnu.trove.set.hash.THashSet;

import java.util.ArrayList;
import java.util.List;

import com.adventure.engine.GameContext;
import com.adventure.engine.entity.Entity;
import com.adventure.engine.script.syntax.Expression;

/**
 * Parses an entity expression.
 * 
 * @author Rodrigo Fernández (angelusmetal@gmail.com)
 *
 */
public class EntityParser extends ExpressionParser<Entity> {

	final GameContext context;
	
	// These sets keep track of entities that have been referenced and those
	// that have been populated, so we can figure out if there are referenced
	// entity ids that never get populated (and we can throw an error)
	
	final THashSet<String> referenced = new THashSet<String>();
	final THashSet<String> populated = new THashSet<String>();
	
	public EntityParser(GameContext context) {
		this.context = context;
	}
	
	@Override
	public Entity parse(Expression expression) throws ExpressionParserException {

		// Get or create entity
		String entityId = expression.getIdentifier();
		
		Entity entity = context.getOrCreateEntity(entityId);
		entity.setName(entityId);
		
		// entity definition should be compound
		List<Expression> nested = getNested(expression);
		
		for (Expression exp : nested) {
			String identifier = exp.getIdentifier();
			// Parse special properties
			if ("connections".equals(identifier)) {
				addConnections(entity, exp);
			} else {
				// Otherwise, add to the pool of user defined properties
				entity.setProperty(exp.getIdentifier(), exp);
			}
		}
		
		return entity;
	}

	/**
	 * Adds connections to other entities. If the entities do not yet exist,
	 * they are created and added so they can be populated later.
	 * @param entity
	 * @param expression
	 * @throws ExpressionParserException
	 */
	public void addConnections(Entity entity, Expression expression) throws ExpressionParserException {
		
		// connections should be compound
		List<Expression> connections = getNested(expression);
		
		for (Expression connection : connections) {
			if (connection.getValue().isCompound()) {
				throw new ExpressionParserException("A connection cannot be a compound expression. It should be identifier: label 1 | label 2 | ...", expression);
			}
			
			// Get or create connected entity
			Entity destination = context.getOrCreateEntity(connection.getIdentifier());
			
			// Reference that entity with all the aliases in the list
			List<String> aliases = connection.getValue().getAsList();
			for (String alias : aliases) {
				entity.addEntity(destination, alias);
			}
		}
	}
	
	/**
	 * Find out all the entity ids that are referenced within other entities,
	 * but that are never defined.
	 * @return A list with all the dangling connections, which might be empty.
	 */
	public List<String> getDanglingConnections() {
		List<String> dangling = new ArrayList<String>();
		for (String ref : referenced) {
			if (!populated.contains(ref)) {
				dangling.add(ref);
			}
		}
		return dangling;
	}

}
