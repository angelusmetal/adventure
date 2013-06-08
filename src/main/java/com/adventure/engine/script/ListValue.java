package com.adventure.engine.script;

import java.util.ArrayList;
import java.util.List;

public class ListValue implements Value {

	final List<String> values;
	
	public ListValue(String[] values) {
		this.values = new ArrayList<String>(values.length);
		for (String value : values) {
			this.values.add(value.trim());
		}
	}
	
	public List<String> getValues() {
		return values;
	}
}
