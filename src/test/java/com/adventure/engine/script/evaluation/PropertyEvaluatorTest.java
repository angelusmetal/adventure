package com.adventure.engine.script.evaluation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.adventure.engine.GameContext;
import com.adventure.engine.entity.Entity;
import com.adventure.engine.script.syntax.Expression;
import com.adventure.engine.script.syntax.SimpleValue;

public class PropertyEvaluatorTest {

	PropertyEvaluator evaluator = new PropertyEvaluator();
	
	@Mock Entity entity;
	@Mock GameContext context;
	Expression exp = new Expression("expression", new SimpleValue("value"), 1);
	
	@Before public void setUp() {
		initMocks(this);
		when(entity.getProperty("property")).thenReturn(exp);
		when(context.getEntity("entity")).thenReturn(entity);
	}
	
	@Test public void testLocalProperty() throws EvaluationException {
		Expression property = evaluator.evaluateProperty("property", entity, context);
		
		assertEquals(exp, property);
		verify(entity).getProperty("property");
		verify(context, never()).getEntity(anyString());
	}
	
	@Test public void testLocalPropertyNonExisting() throws EvaluationException {
		Expression property = evaluator.evaluateProperty("wrong", entity, context);
		
		assertNull(property);
		verify(entity).getProperty("wrong");
		verify(context, never()).getEntity(anyString());
	}
	
	@Test public void testAbsoluteProperty() throws EvaluationException {
		Expression property = evaluator.evaluateProperty("entity.property", entity, context);
		
		assertEquals(exp, property);
		verify(entity).getProperty("property");
		verify(context).getEntity("entity");
	}
	
	@Test public void testAbsolutePropertyNonExistingEntity() throws EvaluationException {
		Expression property = evaluator.evaluateProperty("wrong.property", entity, context);
		
		assertNull(property);
		verify(context).getEntity("wrong");
		verify(entity, never()).getProperty(anyString());
	}
	
	@Test public void testAbsolutePropertyNonExistingProperty() throws EvaluationException {
		Expression property = evaluator.evaluateProperty("entity.wrong", entity, context);
		
		assertNull(property);
		verify(context).getEntity("entity");
		verify(entity).getProperty("wrong");
	}
	
	@Test (expected=EvaluationException.class)
	public void testTooFewTokens() throws EvaluationException {
		evaluator.evaluateProperty("", entity, context);
	}
	
	@Test (expected=EvaluationException.class)
	public void testTooManyTokens() throws EvaluationException {
		evaluator.evaluateProperty("1.2.3", entity, context);
	}
	
}
