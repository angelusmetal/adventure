package com.adventure.engine.script.syntax;

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
	
	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public boolean isList() {
		return true;
	}

	@Override
	public boolean isCompound() {
		return false;
	}

	@Override
	public String getAsString() {
		return StringUtils.join(values,", ");
	}

	@Override
	public List<String> getAsList() {
		return values;
	}

	@Override
	public List<Expression> getNested() {
		return null;
	}

	@Override
	public String toString() {
		return StringUtils.join(values,", ");
	}

}
