package com.adventure.engine.core;

import java.util.List;

public class LocationConnection {

	private Location source;
	private Location dest;
	private List<String> aliases;
	private List<Verb> verbs;

	public LocationConnection(Location source, Location dest, List<String> aliases, List<Verb> verbs) {
		this.source = source;
		this.dest = dest;
		this.aliases = aliases;
		this.verbs = verbs;
	}

	public Location getSource() {
		return source;
	}

	public Location getDest() {
		return dest;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public List<Verb> getVerbs() {
		return verbs;
	}
	
	public boolean isReachable() {
		return true;
	}
	
}
