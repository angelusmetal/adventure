package com.adventure.engine.core;

import java.util.List;

public abstract class Item {

	protected String name;
	protected List<String> aliases;
	protected boolean pickedUp;
	
	public String getName() {
		return name;
	}
	
	public List<String> getAliases() {
		return aliases;
	}
	
	public boolean isStackable() {
		return false;
	}
	
	public int getAmount() {
		return 1;
	}

	public void setPickedUp() {
		pickedUp = true;
	}
	
	public boolean isPickedUp() {
		return pickedUp;
	}
}
