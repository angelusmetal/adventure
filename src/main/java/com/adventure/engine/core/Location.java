package com.adventure.engine.core;

import gnu.trove.map.hash.THashMap;

public abstract class Location extends Entity {

	private THashMap<String, LocationConnection> connections;
	private THashMap<String, Item> items;
	protected String name;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addLocation(LocationConnection connection) {
		for(String alias : connection.getAliases()) {
			connections.put(alias, connection);
		}
	}
	
	public void removeLocation(LocationConnection connection) {
		for (String alias : connection.getAliases()) {
			connections.remove(alias);
		}
	}
	
	public LocationConnection getLocationConnection(String alias) {
		return connections.get(alias);
	}
	
	public void addItem(Item item) {
		for (String alias : item.getAliases()) {
			items.put(alias, item);
		}
	}
	
	public void removeItem(Item item) {
		for (String alias : item.getAliases()) {
			items.remove(alias);
		}
	}
	
	public Item getItem(String alias) {
		return items.get(alias);
	}
	
	public void enter(Context context) {
		context.getConsole().display(name);
	}

}
