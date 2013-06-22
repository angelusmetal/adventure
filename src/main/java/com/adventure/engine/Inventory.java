package com.adventure.engine;

import gnu.trove.procedure.TObjectProcedure;
import gnu.trove.set.hash.THashSet;

import com.adventure.engine.entity.Entity;

public class Inventory extends Entity {

	protected THashSet<Entity> bag = new THashSet<Entity>();
	
	@Override
	protected void look(final GameContext context) {
		if (!entities.isEmpty()) {
			context.getConsole().display("You have:");
			bag.forEach(new TObjectProcedure<Entity>() {
				@Override
				public boolean execute(Entity object) {
					context.getConsole().display(" - " + object.getProperty("shortDescription").getValue().getAsString());
					return true;
				}
			});
		} else {
			context.getConsole().display(context.getVocabulary().getMessages().get("emptyInventory"));
		}
	}

	public void addEntity(Entity entity, String withName) {
		super.addEntity(entity, withName);
		// Add entity to bag
		bag.add(entity);
	}
	
	public void removeEntity(final Entity entity) {
		super.removeEntity(entity);
		// Remove entity from bag
		bag.remove(entity);
	}

}
