package com.adventure.engine.script.grammar;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class CompoundValue implements Value {

	final List<Expression> expressions;
	
	public CompoundValue(List<Expression> expressions) {
		this.expressions = expressions;
	}
	
	public List<Expression> getExpressions() {
		return expressions;
	}
	
	@Override
	public String toString() {
		return StringUtils.join(expressions, ", ");
	}
}
