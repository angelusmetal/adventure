package com.adventure.engine;

import gnu.trove.map.hash.THashMap;

import com.adventure.engine.console.ParserReceiver;
import com.adventure.engine.console.Vocabulary;

public class GameContext implements ParserReceiver {

	THashMap<String, Entity> entityDictionary = new THashMap<String, Entity>();
	Vocabulary vocabulary;
	
	Inventory inventory = new Inventory();
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
		if ("here".equals(object) || "around".equals(object) ||object.equals(currentLocation.getProperty("shortDescription").getValue().getAsString())) {
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
			String preposition, String modifier) {
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

	public void createEntity(String entityId) {
		entityDictionary.put(entityId, new Entity());
	}
	
	public Entity getEntity(String entityId) {
		return entityDictionary.get(entityId);
	}
	
	public Entity getOrCreateEntity(String entityId) {
		if (entityDictionary.containsKey(entityId)) {
			return entityDictionary.get(entityId);
		} else {
			Entity entity = new Entity();
			entityDictionary.put(entityId, entity);
			return entity;
		}
	}
	
	public String showContent() {
		return entityDictionary.toString();
	}

	public void setCurrentLocation(Entity exit) {
		currentLocation = exit;
	}
	
	public void setEntityDictionary(THashMap<String, Entity> entityDictionary) {
		this.entityDictionary = entityDictionary;
	}

	public Vocabulary getVocabulary() {
		return vocabulary;
	}

	public void setVocabulary(Vocabulary vocabulary) {
		this.vocabulary = vocabulary;
	}

}
