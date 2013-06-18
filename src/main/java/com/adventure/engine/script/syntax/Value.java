package com.adventure.engine.script.syntax;

import java.util.List;

public interface Value {

	boolean isSimple();
	boolean isList();
	boolean isCompound();
	String getAsString();
	List<String> getAsList();
	List<Expression> getNested();
}
