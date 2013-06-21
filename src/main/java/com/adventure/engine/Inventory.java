package com.adventure.engine;

import com.adventure.engine.entity.Entity;

import gnu.trove.procedure.TObjectProcedure;

public class Inventory extends Entity {

	@Override
	protected void look(final GameContext context) {
		if (!entities.isEmpty()) {
			context.getConsole().display("You have:");
			entities.forEachValue(new TObjectProcedure<Entity>() {
				@Override
				public boolean execute(Entity object) {
					context.getConsole().display(" - " + object.getProperty("shortDescription").getValue().getAsString());
					return true;
				}
			});
		} else {
			context.getConsole().display("You don't have anything with you right now");
		}
	}
	
}
