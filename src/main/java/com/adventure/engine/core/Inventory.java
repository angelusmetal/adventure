package com.adventure.engine.core;

import java.util.List;

public class Inventory {

	List<Item> items;
	
	public boolean canAddItem(Item item) {
		// TODO: Could check things like volume/weight capacity
		return true;
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	public void removeItem(Item item) {
		items.remove(item);
	}
	
	public Item getItem(String name) {
		for (Item item : items) {
			// TODO: Should it use aliases? What about name collision?
			if (item.getName().equals(name)) {
				return item;
			}
		}
		return null;
	}
	
}
