package com.adventure.engine;

import gnu.trove.map.hash.THashMap;

import com.adventure.engine.console.ParserReceiver;
import com.adventure.engine.console.Vocabulary;
import com.adventure.engine.entity.Entity;
import com.adventure.engine.entity.EntityHandlingException;

public class GameContext implements ParserReceiver {

	THashMap<String, Entity> entityDictionary = new THashMap<String, Entity>();
	Vocabulary vocabulary;
	
	Inventory inventory = new Inventory();
	Entity currentLocation;
	
	
	public void displayPrompt() {
		System.out.print("> ");
	}
	
	@Override
	public void display(String text) {
		System.out.println(text);
	}
	
	@Override
	public void doAction(String verb) {
		// Send signal to current location
		try {
			currentLocation.signal(this, verb);
		} catch (EntityHandlingException e) {
			e.printStackTrace();
			System.exit(1);
		}
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
			try {
				inventory.signal(this, verb);
			} catch (EntityHandlingException e) {
				e.printStackTrace();
				System.exit(1);
			}
			return;
		}
		
		// Check if the object is a visible entity in this location
		Entity entity = currentLocation.getEntity(object);
		if (entity == null || !entity.isVisible()) {
			display("There is no " + object);
			return;
		}
		
		// Send signal (verb) to the object
		try {
			entity.signal(this, verb);
		} catch (EntityHandlingException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void doActionOnObjectWithModifier(String verb, String object,
			String preposition, String modifier) {
		// Synonyms of current location
		if ("here".equals(object) || "around".equals(object) || currentLocation.getName().equals(object)) {
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
			try {
				inventory.signal(this, verb, modifierEntity);
			} catch (EntityHandlingException e) {
				e.printStackTrace();
				System.exit(1);
			}
			return;
		}
		
		// Identify the object
		Entity entity = currentLocation.getEntity(object);
		if (entity == null) {
			display("There is no " + object);
			return;
		}
		
		// Send signal (verb + modifier) to the object
		try {
			entity.signal(this, verb, modifierEntity);
		} catch (EntityHandlingException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void addToInventory(Entity entity) {
		display("You picked up " + entity.getName());
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
