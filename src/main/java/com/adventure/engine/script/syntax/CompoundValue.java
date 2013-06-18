package com.adventure.engine.script.syntax;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class CompoundValue implements Value {

	final List<Expression> expressions;
	
	public CompoundValue(List<Expression> expressions) {
		this.expressions = expressions;
	}
	
	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public boolean isList() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCompound() {
		return true;
	}

	@Override
	public String getAsString() {
		return null;
	}

	@Override
	public List<String> getAsList() {
		return null;
	}

	@Override
	public List<Expression> getNested() {
		return expressions;
	}
	
	@Override
	public String toString() {
		return StringUtils.join(expressions, ", ");
	}

}
