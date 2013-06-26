package com.adventure.engine;

import gnu.trove.map.hash.THashMap;
import gnu.trove.set.hash.THashSet;

import com.adventure.engine.console.Console;
import com.adventure.engine.console.ParserReceiver;
import com.adventure.engine.console.StdConsole;
import com.adventure.engine.console.Vocabulary;
import com.adventure.engine.entity.Entity;
import com.adventure.engine.script.evaluation.EvaluationException;
import com.adventure.engine.script.syntax.Expression;
import com.adventure.engine.script.syntax.SimpleValue;

public class GameContext implements ParserReceiver {

	THashMap<String, Entity> entityDictionary = new THashMap<String, Entity>();
	Vocabulary vocabulary;
	
	Inventory inventory = new Inventory();
	Entity currentLocation, previousLocation;
	Console console = new StdConsole(80);
	
	public void displayPrompt() {
		System.out.print("> ");
	}
	
	public Console getConsole() {
		return console;
	}
	
	@Override
		// Send signal to current location
	public void doAction(String verb) {
		try {
			currentLocation.signal(this, verb);
		} catch (EvaluationException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void doActionOnObject(String verb, String object) throws EvaluationException {
		Entity entity = lookupEntity(object);
		if (entity != null) {
			entity.signal(this, verb);
		}
	}

	@Override
	public void doActionOnObjectWithModifier(String verb, String object,
			String preposition, String modifier) throws EvaluationException {
		
		Entity entity = lookupEntity(object);
		Entity modifierEntity = lookupEntity(modifier);
		
		if (entity != null && modifierEntity != null) {
			entity.signal(this, verb, preposition, modifierEntity);
		}
	}
	
	/**
	 * Look up an entity.
	 * 
	 * Determine if the entity is a special entity (current location, previous
	 * location, inventory, etc), an entity in the current location or an
	 * entity in the inventory.
	 * @param object Entity name (not id).
	 * @return Entity or null if the entity was not identified.
	 */
	private Entity lookupEntity(String object) {
		
		Entity entity = null;
		
		String specialEntity = vocabulary.getSpecialEntities().get(object);
		// Check if entity is a special entity
		if (specialEntity != null) {
			if (specialEntity.equals("thisLocation")) {
				entity = currentLocation;
			} else if (specialEntity.equals("lastLocation")) {
				entity = previousLocation;
			} else if (specialEntity.equals("inventory")) {
				entity = inventory;
			}
		} else {
			// Look up entity on current location, or on inventory
			entity = currentLocation.getEntity(object);
			if (entity == null) {
				entity = inventory.getEntity(object);
			}
			// If not found or can't receive messages 
			if (entity == null || !entity.isVisible()) {
				console.display(vocabulary.getMessages().get("cantDo"));
				entity = null;
			}
		}
		return entity;
	}
	
	@Override
	public void magicPhrase(String phrase) {
		if ("exit".equals(phrase)) {
			System.exit(0);
		} else if ("whereAmI".equals(phrase)) {
			console.display(vocabulary.getMessages().get("currentLocation") + " " + currentLocation.getProperty("shortDescription").getValue().getAsString());
		} else if ("easterEgg".equals(phrase)) {
			console.display("With a deep, baritone voice, the world trembles gently as it returns your greetings.");
		}
	}

	public void addToInventory(Entity entity, THashSet<String> aliases) {
		console.display(vocabulary.getMessages().get("pickedUp") + " " + entity.getProperty("shortDescription").getValue().getAsString());
		
		// The entity is no longer pickable; otherwise you will be able to pick it from your inventory
		entity.setProperty("pickable", new Expression("pickable", new SimpleValue("false"), -1));
		for (String alias : aliases) {
			inventory.addEntity(entity, alias);
		}
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

	public void setCurrentLocation(Entity newLocation) {
		previousLocation = currentLocation;
		currentLocation = newLocation;
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

	public Entity getCurrentLocation() {
		return currentLocation;
	}

}
