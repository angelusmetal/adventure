package com.adventure.engine.script.evaluation;

import org.apache.commons.lang3.StringUtils;

import com.adventure.engine.Entity;
import com.adventure.engine.GameContext;
import com.adventure.engine.script.syntax.Expression;

/**
 * Evaluates a property String (either property or entity.property) and attempts
 * to resolve it and return the property value (an Expression itself).
 * 
 * @author Rodrigo Fernandez (rodrigo.fernandez@angelusmetal.com)
 */
public class PropertyEvaluator {

	public Expression evaluate(String property, Entity localEntity, GameContext context) throws EvaluationException {
		String[] tokens = StringUtils.split(property, '.');
		if (tokens.length == 0 || tokens.length > 2) {
			throw new EvaluationException(property + " is not a valid property identifier.");
		} else if (tokens.length == 1) {
			return localEntity.getProperty(tokens[0]);
		} else {
			Entity entity = context.getEntity(tokens[0]);
			if (entity != null) {
				return entity.getProperty(tokens[1]);
			} else {
				return null;
			}
		}
	}
}
