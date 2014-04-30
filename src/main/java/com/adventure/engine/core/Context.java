package com.adventure.engine.core;

import com.adventure.engine.console.Console;
import com.adventure.engine.console.StdConsole;

public class Context {

	Console console;
	Location currentLocation;
	Inventory inventory;
	
	public Context() {
		this.console = new StdConsole(80);
		this.inventory = new Inventory();
	}
	
	public Console getConsole() {
		return console;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}

	// TODO Should this belong somewhere else? Should this class have no logic?
	public void moveToLocation(String newLocation) {
		LocationConnection connection = currentLocation.getLocationConnection(newLocation);
		if (connection != null) {
			if (connection.isReachable()) {
				currentLocation = connection.getDest();
				currentLocation.enter(this);
			}
		}
	}
	
	public void pickUpItem(Item item) {
		if (!item.isPickedUp() && inventory.canAddItem(item)) {
			inventory.addItem(item);
			item.setPickedUp();
		}
	}
	
	public Item resolveItemByName(String name) {
		Item item = currentLocation.getItem(name);
		if (item == null) {
			item = inventory.getItem(name);
		}
		return item;
	}

}
