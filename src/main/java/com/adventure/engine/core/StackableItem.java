package com.adventure.engine.core;

public abstract class StackableItem extends Item {

	protected int amount;
	
	public boolean isStackable() {
		return true;
	}
	
	public int getAmount() {
		return amount;
	}
	
}
