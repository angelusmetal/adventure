package com.adventure.engine.script.evaluation;

import java.util.List;

import com.adventure.engine.GameContext;
import com.adventure.engine.entity.Entity;
import com.adventure.engine.script.syntax.Expression;

public class CodeEvaluator {
	
	private PropertyEvaluator propertyEvaluator = new PropertyEvaluator();

	public void evaluate(Expression code, Entity localEntity, GameContext context) throws EvaluationException {
		
		// Code must be a compound expression
		if (!code.getValue().isCompound()) {
			throw new EvaluationException("Handler code expression should be compound" + code);
		}
		
		// Evaluate each nested expression sequentially
		List<Expression> nested = code.getValue().getNested();
		for (Expression exp : nested) {
			// Special meaning expressions
			if ("display".equals(exp.getIdentifier())) {
				context.display(exp.getValue().getAsString()); // TODO Missing replacement of @annotated expressions
			} else if ("exit".equals(exp.getIdentifier())) {
				context.display(exp.getValue().getAsString()); // TODO Missing replacement of @annotated expressions
				System.exit(0);
			} 
			// Normal expression (for now, only assignments
			else {
				// For now, only property assignments
				Entity entity = propertyEvaluator.getEntity(exp.getIdentifier(), localEntity, context);
				String property = propertyEvaluator.getProperty(exp.getIdentifier());
				entity.setProperty(property, exp);
			}
		}
	}
}
