package com.adventure.engine.script.syntax;

import java.util.Arrays;
import java.util.List;

public class SimpleValue implements Value {

	final String value;
	
	public SimpleValue(final String value) {
		this.value = value;
	}
	
	@Override
	public boolean isSimple() {
		return true;
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public boolean isCompound() {
		return false;
	}

	@Override
	public String getAsString() {
		return value;
	}

	@Override
	public List<String> getAsList() {
		return Arrays.asList(value);
	}

	@Override
	public List<Expression> getNested() {
		return null;
	}

	@Override
	public String toString() {
		return value;
	}

}
