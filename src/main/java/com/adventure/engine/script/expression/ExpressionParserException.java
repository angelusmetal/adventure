package com.adventure.engine.script.expression;

import com.adventure.engine.script.syntax.Expression;

/**
 * An exception that happens when parsing an expression.
 * 
 * @author Rodrigo Fernández (angelusmetal@gmail.com)
 *
 */
public class ExpressionParserException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ExpressionParserException(String msg, Expression exp) {
		super("Error at line " + exp.getLineNumber() + ". " + msg);
	}
	
	public ExpressionParserException(Throwable e, Expression exp) {
		super("Error at line " + exp.getLineNumber() + ". ", e);
	}
	
	public ExpressionParserException(String msg, Throwable e, Expression exp) {
		super("Error at line " + exp.getLineNumber() + ". " + msg, e);
	}
	
}
