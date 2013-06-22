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

	public String evaluate(String expression, Entity localEntity, GameContext context) throws EvaluationException {
		
		// A builder for the result and another for the to-be-replaced expression
		StringBuilder result = new StringBuilder();
		StringBuilder expr = new StringBuilder();
		
		// Scan expression string in one of two modes
		// Replace captures expression (prepended with @) and then evaluates them and adds them to the result
		// Non-replace is a pass-through to the result
		boolean replacing = false;
		for(int i = 0; i < expression.length(); ++i) {
			char charAt = expression.charAt(i);
			if (replacing) {
				if (charAt == ' ') {
					replacing = false;
					result.append(evaluateProperty(expr.toString(), localEntity, context).getValue().getAsString());
					expr = new StringBuilder();
				} else {
					expr.append(charAt);
				}
			} else {
				if (charAt == '@') {
					replacing = true; // start capturing expression name, but do not include @
				} else {
					result.append(charAt);
				}
			}
		}

		// If there's a pending replacement, do it
		if (expr.length() > 0) {
			result.append(evaluateProperty(expr.toString(), localEntity, context).getValue().getAsString());
		}
		
		return result.toString();
	}
	
	public Expression evaluateProperty(String propertyExpression, Entity localEntity, GameContext context) throws EvaluationException {
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
