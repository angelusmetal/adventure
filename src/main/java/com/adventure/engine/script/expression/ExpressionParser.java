package com.adventure.engine.script.expression;

import java.util.List;

import com.adventure.engine.script.syntax.Expression;

/**
 * Base class for expression parsers. An expression parser takes an expression
 * as entry and creates an object as exit.
 * 
 * Also provides facilities for validating expression type to enforce grammar
 * or schema upon parsing.
 * 
 * @author Rodrigo Fernández (angelusmetal@gmail.com)
 *
 * @param <T> The type of object to create and return when the parsing is done.
 */
public abstract class ExpressionParser<T> {

	/**
	 * Parses an expression and returns a parsed object.
	 * @param expression A script expression to parse.
	 * @return The parsed object
	 * @throws ExpressionParserException if the expression cannot be parsed.
	 */
	public abstract T parse(Expression expression) throws ExpressionParserException;
	
	/**
	 * Make sure an expression is simple and get its String value.
	 * @param expression Expression to get String value from.
	 * @return String value in the expression
	 * @throws ExpressionParserException If the expression is not simple.
	 */
	protected String getSimple(Expression expression) throws ExpressionParserException {
		if (!expression.getValue().isSimple()) {
			throw new ExpressionParserException("Expression " + expression.getIdentifier() + " should be simple.", expression);
		}
		return expression.getValue().getAsString();
	}
	
	/**
	 * Make sure an expression is either simple or a list, and get its list
	 * of values (of one element, if it's simple).
	 * @param expression Expression to get List of String value from.
	 * @return List of String values in the expression.
	 * @throws ExpressionParserException If the expression is neither simple nor
	 * list.
	 */
	protected List<String> getList(Expression expression) throws ExpressionParserException {
		if (!expression.getValue().isList() && !expression.getValue().isSimple()) {
			throw new ExpressionParserException("Expression " + expression.getIdentifier() + " should be a list.", expression);
		}
		return expression.getValue().getAsList();
	}
	
	/**
	 * Make sure an expression is compound and get its nested expressions.
	 * @param expression Expression to get nested expressions from.
	 * @return List of nested expressions.
	 * @throws ExpressionParserException If the expression is not compound.
	 */
	protected List<Expression> getNested(Expression expression) throws ExpressionParserException {
		if (!expression.getValue().isCompound()) {
			throw new ExpressionParserException("Expression " + expression.getIdentifier() + " should be compound.", expression);
		}
		return expression.getValue().getNested();
	}
	
}
