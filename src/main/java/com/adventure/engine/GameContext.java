package com.adventure.engine;

import gnu.trove.map.hash.THashMap;

import com.adventure.engine.parser.ParserReceiver;

public class GameContext implements ParserReceiver {

	Inventory inventory = new Inventory();
	THashMap<String, Entity> entityMap = new THashMap<String, Entity>();
	
	Entity currentLocation;
	
	public void displayPrompt() {
		//System.out.println("[" + currentLocation.shortDescription + "]");
		System.out.print("> ");
	}
	
	@Override
	public void display(String text) {
		System.out.println(text);
	}
	
	@Override
	public void doAction(String verb) {
		// See if verb has any use in the current context/location
		currentLocation.signal(this, verb);
	}

	@Override
	public void doActionOnObject(String verb, String object) {
		// Synonyms of current location
		if ("here".equals(object) || "around".equals(object) ||object.equals(currentLocation.getProperty("shortDescription").getAsString())) {
			doAction(verb);
			return;
		}
		
		// Inventory
		if ("inventory".equals(object)) {
			inventory.signal(this, verb);
			return;
		}
		
		// Check if the object is a visible entity in this location
		Entity entity = currentLocation.getEntity(object);
		if (entity == null || !entity.visible) {
			display("There is no " + object);
			return;
		}
		
		// Send signal (verb) to the object
		entity.signal(this, verb);
	}

	@Override
	public void doActionOnObjectWithModifier(String verb, String object,
			String modifier) {
		// Synonyms of current location
		if ("here".equals(object) || "around".equals(object) || currentLocation.name.equals(object)) {
			doAction(verb);
			return;
		}
		
		// Identify the modifier
		Entity modifierEntity = currentLocation.getEntity(modifier);
		if (modifierEntity == null) {
			modifierEntity = inventory.getEntity(modifier);
			if (modifierEntity == null) {
				display("There is no " + modifier);
				return;
			}
		}
		
		// Inventory
		if ("inventory".equals(object)) {
			inventory.signal(this, verb, modifierEntity);
			return;
		}
		
		// Identify the object
		Entity entity = currentLocation.getEntity(object);
		if (entity == null) {
			display("There is no " + object);
			return;
		}
		
		// Send signal (verb + modifier) to the object
		entity.signal(this, verb, modifierEntity);
	}

	public void addToInventory(Entity entity) {
		display("You picked up " + entity.name);
		inventory.addEntity(entity);
	}

	public boolean isLookVerb(String verb) {
		return  "look".equals(verb) ||
				"look at".equals(verb) ||
				"see".equals(verb) ||
				"examine".equals(verb);
	}
	
	public boolean isExamineVerb(String verb) {
		return "examine".equals(verb);
	}
	
	public boolean isPickupVerb(String verb) {
		return  "pick".equals(verb) ||
				"pick up".equals(verb) ||
				"grab".equals(verb);
	}
	
	public boolean isGoVerb(String verb) {
		return  "go".equals(verb) ||
				"go to".equals(verb) ||
				"go through".equals(verb);
	}
	
	public void createEntity(String entityId) {
		entityMap.put(entityId, new Entity());
	}
	
	public Entity getEntity(String entityId) {
		return entityMap.get(entityId);
	}
	
	public String showContent() {
		return entityMap.toString();
	}

	public void setCurrentLocation(Entity exit) {
		currentLocation = exit;
	}
	
}
