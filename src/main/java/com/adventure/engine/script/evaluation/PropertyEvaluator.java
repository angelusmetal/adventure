package com.adventure.engine.script.evaluation;

import org.apache.commons.lang3.StringUtils;

import com.adventure.engine.GameContext;
import com.adventure.engine.entity.Entity;
import com.adventure.engine.script.syntax.Expression;

/**
 * Evaluates a property String (either property or entity.property) and attempts
 * to resolve it and return the property value (an Expression itself).
 * 
 * @author Rodrigo Fernandez (rodrigo.fernandez@angelusmetal.com)
 */
public class PropertyEvaluator {

	public Expression evaluate(String propertyExpression, Entity localEntity, GameContext context) throws EvaluationException {
		String[] tokens = StringUtils.split(propertyExpression, '.');
		if (tokens.length == 0 || tokens.length > 2) {
			throw new EvaluationException(propertyExpression + " is not a valid property identifier.");
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
	
	public Entity getEntity(String propertyExpression, Entity localEntity, GameContext context) throws EvaluationException {
		String[] tokens = StringUtils.split(propertyExpression, '.');
		if (tokens.length == 0 || tokens.length > 2) {
			throw new EvaluationException(propertyExpression + " is not a valid property identifier.");
		} else if (tokens.length == 1) {
			return localEntity;
		} else {
			return context.getEntity(tokens[0]);
		}
	}
	
	public String getProperty(String propertyExpression) throws EvaluationException {
		String[] tokens = StringUtils.split(propertyExpression, '.');
		if (tokens.length == 0 || tokens.length > 2) {
			throw new EvaluationException(propertyExpression + " is not a valid property identifier.");
		} else if (tokens.length == 1) {
			return tokens[0];
		} else {
			return tokens[1];
		}
	}
}
