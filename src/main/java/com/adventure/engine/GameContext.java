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
	Entity currentLocation;
	Console console = new StdConsole(80);
	
	public void displayPrompt() {
		System.out.print("> ");
	}
	
	public Console getConsole() {
		return console;
	}
	
	@Override
	public void doAction(String verb) {
		// Send signal to current location
		try {
			currentLocation.signal(this, verb);
		} catch (EvaluationException e) {
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
			} catch (EvaluationException e) {
				e.printStackTrace();
				System.exit(1);
			}
			return;
		}
		
		// Check if the object is a visible entity in this location
		Entity entity = currentLocation.getEntity(object);
		// Or in the inventory
		if (entity == null) {
			entity = inventory.getEntity(object);
		}
		if (entity == null || !entity.isVisible()) {
			console.display(vocabulary.getMessages().get("cantDo"));
			return;
		}
		
		// Send signal (verb) to the object
		try {
			entity.signal(this, verb);
		} catch (EvaluationException e) {
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
				console.display(vocabulary.getMessages().get("cantDo"));
				return;
			}
		}
		
		// Inventory
		if ("inventory".equals(object)) {
			try {
				inventory.signal(this, verb, preposition, modifierEntity);
			} catch (EvaluationException e) {
				e.printStackTrace();
				System.exit(1);
			}
			return;
		}
		
		// Identify the object
		Entity entity = currentLocation.getEntity(object);
		if (entity == null) {
			entity = inventory.getEntity(object);
		}
		if (entity == null) {
			console.display(vocabulary.getMessages().get("cantDo"));
			return;
		}
		
		// Send signal (verb + modifier) to the object
		try {
			entity.signal(this, verb, preposition, modifierEntity);
		} catch (EvaluationException e) {
			e.printStackTrace();
			System.exit(1);
		}
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

	public Entity getCurrentLocation() {
		return currentLocation;
	}

}
