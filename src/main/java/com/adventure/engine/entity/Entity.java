package com.adventure.engine.entity;

import java.util.List;

import com.adventure.engine.GameContext;
import com.adventure.engine.console.Vocabulary;
import com.adventure.engine.script.evaluation.EvaluationException;
import com.adventure.engine.script.evaluation.PropertyEvaluator;
import com.adventure.engine.script.syntax.Expression;
import com.adventure.engine.script.syntax.SimpleValue;

import gnu.trove.map.hash.THashMap;

public class Entity {

	protected String name;
	protected String nonPickableDescription = "I can't pick that";
	protected String nonTraversableDescription = "I can't go there";
	
	protected THashMap<String, Entity> entities = new THashMap<String, Entity>();
	protected THashMap<String, Expression> properties = new THashMap<String, Expression>();
	
	static protected PropertyEvaluator propertyEvaluator = new PropertyEvaluator();
	
	public Entity() {
		setProperty("visible", new Expression("visiable", new SimpleValue("true"), 0));
		setProperty("pickable", new Expression("pickable", new SimpleValue("false"), 0));
		setProperty("traversable", new Expression("traversable", new SimpleValue("false"), 0));
	}
	
	final public boolean signal(GameContext context, String verb) throws EntityHandlingException {
		Vocabulary vocabulary = context.getVocabulary();
		String group = vocabulary.getVerbGroup(verb);
		
		// Look up handler code. First try to get code that handles the
		// specific verb. If not, try to get code that handles the verb group.
		// And finally, if not, use the default handling code.
		
		String verbHandler = "on " + verb;
		String groupHandler = "on " + group;
		Expression code = null;
		
		// verb handler
		if (properties.containsKey(verbHandler)) {
			code = properties.get(verbHandler);
		}
		// group handler
		else if (properties.containsKey(groupHandler)) {
			code = properties.get(groupHandler);
		}
		// if a specific handler was found, evaluate the handler code
		if (code != null) {
			List<Expression> nested = code.getValue().getNested();
			for (Expression exp : nested) {
				try {
					// For now, only property assignments
					Entity entity = propertyEvaluator.getEntity(exp.getIdentifier(), this, context);
					String property = propertyEvaluator.getProperty(exp.getIdentifier());
					entity.setProperty(property, exp);
				} catch (EvaluationException e) {
					throw new EntityHandlingException(e);
				}
			}
		}
		
		// Then, always call the default handler
		if ("@look".equals(group)) {
			look(context);
		} else if ("@pick".equals(group)) {
			pick(context);
		} else if ("@go".equals(group)) {
			go(context);
		} else {
			// Handle non-standard actions
			return handle(context, verb);
		}
		
		return true;
	}
	
	final public boolean signal(GameContext context, String verb, String preposition, Entity modifier) throws EntityHandlingException {
		
		Vocabulary vocabulary = context.getVocabulary();
		String group = vocabulary.getVerbGroup(verb);
		
		// Look up handler code. First try to get code that handles the
		// specific verb. If not, try to get code that handles the verb group.
		// And finally, if not, use the default handling code.
		
		String verbHandler = "on " + verb + " " + preposition + " " + modifier.getName();
		String groupHandler = "on " + group + " " + preposition + " " + modifier.getName();
		Expression code = null;
		
		// verb handler
		if (properties.containsKey(verbHandler)) {
			code = properties.get(verbHandler);
		}
		// group handler
		else if (properties.containsKey(groupHandler)) {
			code = properties.get(groupHandler);
		}
		// if a specific handler was found, evaluate the handler code
		if (code != null) {
			List<Expression> nested = code.getValue().getNested();
			for (Expression exp : nested) {
				try {
					// TODO Move this code to its own place
					if ("display".equals(exp.getIdentifier())) {
						context.display(exp.getValue().getAsString()); // TODO Missing replacement of @annotated expressions
					} else {
						// For now, only property assignments
						Entity entity = propertyEvaluator.getEntity(exp.getIdentifier(), this, context);
						String property = propertyEvaluator.getProperty(exp.getIdentifier());
						entity.setProperty(property, exp);
					}
				} catch (EvaluationException e) {
					throw new EntityHandlingException(e);
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	public boolean handle(GameContext context, String verb) { return false; }
	public boolean handle(GameContext context, String verb, Entity modifier)  { return false; }
	
	public void setProperty(String property, Expression value) {
		properties.put(property, value);
	}
	
	public Expression getProperty(String property) {
		return properties.get(property);
	}
	
	/*
	 * Default actions
	 */
	protected void look(GameContext context) {
		context.display(properties.get("longDescription").getValue().getAsString());
	}
	
	protected void pick(GameContext context) {
		if (isPickable()) {
			context.addToInventory(this);
		} else {
			context.display(nonPickableDescription);
		}
	}
	
	protected void go(GameContext context) {
		if (isTraversable()) {
			context.setCurrentLocation(this);
			context.display("Went to " + properties.get("shortDescription").getValue().getAsString());
		} else {
			context.display(nonTraversableDescription);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNonPickableDescription() {
		return nonPickableDescription;
	}

	public void setNonPickableDescription(String nonPickableDescription) {
		this.nonPickableDescription = nonPickableDescription;
	}

	public void addEntity(Entity entity) {
		entities.put(entity.name, entity);
	}
	
	public void addEntity(Entity entity, String withName) {
		entities.put(withName, entity);
	}
	
	public Entity getEntity(String entity) {
		return entities.get(entity);
	}
	
	public boolean isPickable() {
		return "true".equals(getProperty("pickable").getValue().getAsString());
	}

	public boolean isTraversable() {
		return "true".equals(getProperty("traversable").getValue().getAsString());
	}

	public boolean isVisible() {
		return "true".equals(getProperty("visible").getValue().getAsString());
	}

	@Override
	public String toString() {
		return "Entity [name=" + name + ", nonPickableDescription="
				+ nonPickableDescription + ", nonTraversableDescription="
				+ nonTraversableDescription + ", entities=" + entities
				+ ", properties=" + properties + "]";
	}

}
