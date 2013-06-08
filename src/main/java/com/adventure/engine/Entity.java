package com.adventure.engine;

import com.adventure.engine.script.grammar.Value;

import gnu.trove.map.hash.THashMap;

public class Entity {

	protected boolean visible = true;
	protected String name;
	protected String shortDescription;
	protected String longDescription;
	
	protected boolean pickable = false;
	protected String nonPickableDescription = "I can't pick that";
	
	protected boolean traversable = false;
	protected String nonTraversableDescription = "I can't go there";
	
	protected THashMap<String, Entity> entities = new THashMap<String, Entity>();
	protected THashMap<String, Value> properties = new THashMap<String, Value>();
	//protected THashMap<Event, EventHandler> eventHandlers = new THashMap<Event, EventHandler>();
	
	final public boolean signal(GameContext context, String verb) {
		if (context.isLookVerb(verb)) {
			look(context);
		} else if (context.isPickupVerb(verb)) {
			pick(context);
		} else if (context.isGoVerb(verb)) {
			go(context);
		} else {
			// Handle non-standard actions
			return handle(context, verb);
		}
		return true;
	}
	
	final public boolean signal(GameContext context, String verb, Entity modifier) {
		// Handle non-standard actions
		boolean result = handle(context, verb, modifier);
		if (result == false) {
			context.display("I can't do that");
		}
		return result;
	}
	
	public boolean handle(GameContext context, String verb) { return false; }
	public boolean handle(GameContext context, String verb, Entity modifier)  { return false; }
	
	public void setProperty(String property, Value value) {
		properties.put(property, value);
	}
	
	public Value getProperty(String property) {
		return properties.get(property);
	}
	
	/*
	 * Default actions
	 */
	protected void look(GameContext context) {
		context.display(properties.get("longDescription").getAsString());
	}
	
	protected void pick(GameContext context) {
		if (pickable) {
			context.addToInventory(this);
		} else {
			context.display(nonPickableDescription);
		}
	}
	
	protected void go(GameContext context) {
		if (traversable) {
			context.setCurrentLocation(this);
			context.display("Went to " + properties.get("shortDescription").getAsString());
		} else {
			context.display(nonTraversableDescription);
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public boolean isPickable() {
		return pickable;
	}

	public void setPickable(boolean pickable) {
		this.pickable = pickable;
	}

	public String getNonPickableDescription() {
		return nonPickableDescription;
	}

	public void setNonPickableDescription(String nonPickableDescription) {
		this.nonPickableDescription = nonPickableDescription;
	}

	public boolean isTraversable() {
		return traversable;
	}

	public void setTraversable(boolean traversable) {
		this.traversable = traversable;
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

	@Override
	public String toString() {
		return "Entity [visible=" + visible + ", name=" + name
				+ ", shortDescription=" + shortDescription
				+ ", longDescription=" + longDescription + ", pickable="
				+ pickable + ", nonPickableDescription="
				+ nonPickableDescription + ", traversable=" + traversable
				+ ", nonTraversableDescription=" + nonTraversableDescription
				+ ", entities=" + entities.keySet() + ", properties=" + properties + "]";
	}

}
