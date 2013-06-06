package com.adventure.engine;

import gnu.trove.procedure.TObjectProcedure;

public class Inventory extends Entity {

	@Override
	protected void look(final GameContext context) {
		if (!entities.isEmpty()) {
			context.display("You have:");
			entities.forEachValue(new TObjectProcedure<Entity>() {
				@Override
				public boolean execute(Entity object) {
					context.display(" - " + object.shortDescription);
					return true;
				}
			});
		} else {
			context.display("You don't have anything with you right now");
		}
	}
	
}
