package com.adventure.engine.entity;

import gnu.trove.map.hash.THashMap;
import gnu.trove.procedure.TObjectObjectProcedure;
import gnu.trove.set.hash.THashSet;

import com.adventure.engine.GameContext;
import com.adventure.engine.console.Vocabulary;
import com.adventure.engine.script.evaluation.CodeEvaluator;
import com.adventure.engine.script.evaluation.EvaluationException;
import com.adventure.engine.script.syntax.Expression;
import com.adventure.engine.script.syntax.SimpleValue;

public class Entity {

	protected String name;
	
	/**
	 * Other entities being referenced from this entities. Each entity may be
	 * referenced more than once, under different aliases (the key of this map).
	 */
	protected THashMap<String, Entity> entities = new THashMap<String, Entity>();
	/**
	 * Set of entities that reference this entity.
	 */
	protected THashSet<Entity> referencing = new THashSet<Entity>();
	/**
	 * Dictionary of properties of this entity.
	 */
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
				context.display(context.getVocabulary().getCantDoMessage());
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
			context.display(context.getVocabulary().getCantDoMessage());
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
			// Remove from all referencing entities
			for (Entity ref : referencing) {
				ref.removeEntity(this);
			}
			// Clear referenced entities
			referencing.clear();
			// Add to inventory
			context.addToInventory(this);
		} else {
			context.display(context.getVocabulary().getCantPickMessage());
		}
	}
	
	protected void go(GameContext context) {
		if (isTraversable()) {
			context.setCurrentLocation(this);
			context.display("Went to " + properties.get("shortDescription").getValue().getAsString());
		} else {
			context.display(context.getVocabulary().getCantTraverseMessage());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addEntity(Entity entity) {
		addEntity(entity, entity.name);
	}
	
	public void addEntity(Entity entity, String withName) {
		// Add entity to dictionary
		entities.put(withName, entity);
		entity.referencing.add(this);
	}
	
	public void removeEntity(final Entity entity) {
		// First, find all aliases with which this entity is loaded here
		final THashSet<String> aliases = new THashSet<String>();
		entities.forEachEntry(new TObjectObjectProcedure<String, Entity>() {
			@Override
			public boolean execute(String alias, Entity e) {
				if (e.equals(entity)) {
					aliases.add(alias);
				}
				return true;
			}
		});
		
		// Then delete all aliases for this entity in the entities map
		for (String alias : aliases) {
			entities.remove(alias);
		}
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
		return "Entity [name=" + name + ", entities=" + entities
				+ ", properties=" + properties + "]";
	}

}
