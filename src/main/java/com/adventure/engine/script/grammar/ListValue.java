package com.adventure.engine.script.grammar;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
	
	@Override
	public String toString() {
		return StringUtils.join(values,", ");
	}
}
