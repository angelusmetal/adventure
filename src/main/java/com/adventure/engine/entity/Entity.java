package com.adventure.engine.entity;

import gnu.trove.map.hash.THashMap;

import com.adventure.engine.GameContext;
import com.adventure.engine.console.Vocabulary;
import com.adventure.engine.script.evaluation.CodeEvaluator;
import com.adventure.engine.script.evaluation.EvaluationException;
import com.adventure.engine.script.syntax.Expression;
import com.adventure.engine.script.syntax.SimpleValue;

public class Entity {

	protected String name;
	protected String nonPickableDescription = "I can't pick that";
	protected String nonTraversableDescription = "I can't go there";
	
	protected THashMap<String, Entity> entities = new THashMap<String, Entity>();
	protected THashMap<String, Expression> properties = new THashMap<String, Expression>();
	
	static protected CodeEvaluator codeEvaluator = new CodeEvaluator();
	
	public Entity() {
		setProperty("visible", new Expression("visiable", new SimpleValue("true"), 0));
		setProperty("pickable", new Expression("pickable", new SimpleValue("false"), 0));
		setProperty("traversable", new Expression("traversable", new SimpleValue("false"), 0));
	}
	
	/**
	 * Signal an entity with a verb. If the entity has handler code for that
	 * verb, or for its verb group, it will call that code. Standard actions
	 * will trigger a default code, regardless of whether they have custom
	 * handler code.
	 * @param context 
	 * @param verb
	 * @return
	 * @throws EvaluationException
	 */
	final public boolean signal(GameContext context, String verb) throws EvaluationException {
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
			codeEvaluator.evaluate(code, this, context);
		}
		
		// Then, always call the default handler
		if ("@look".equals(group)) {
			look(context);
		} else if ("@pick".equals(group)) {
			pick(context);
		} else if ("@go".equals(group)) {
			go(context);
		} else {
			if (code == null) {
				context.display("I can't do that");
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Signal an entity with a complex message (verb, preposition and modifier
	 * entity). There is no default handling code, so it will look for custom
	 * code to handle this verb with the supplied preposition and entity, or
	 * the same, but for the verb group. Otherwise, a non-successful action
	 * message is displayed.
	 * @param context
	 * @param verb
	 * @param preposition
	 * @param modifier
	 * @return
	 * @throws EvaluationException
	 */
	final public boolean signal(GameContext context, String verb, String preposition, Entity modifier) throws EvaluationException {
		
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
			codeEvaluator.evaluate(code, this, context);
			return true;
		} else {
			context.display("I can't do that");
			return false;
		}
	}
	
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
