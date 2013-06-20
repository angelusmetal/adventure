package com.adventure.engine.script.expression;

import java.util.List;

import com.adventure.engine.console.Vocabulary;
import com.adventure.engine.console.Vocabulary.VocabularyException;
import com.adventure.engine.script.syntax.Expression;

/**
 * Takes care of parsing a vocabulary expression to populate a {@link Vocabulary} object.
 * 
 * @author Rodrigo Fernández (angelusmetal@gmail.com)
 *
 */
public class VocabularyParser extends ExpressionParser<Vocabulary> {

	@Override
	public Vocabulary parse(Expression expression) throws ExpressionParserException {
		Vocabulary vocabulary = new Vocabulary();
		
		// vocabulary definition should be compound
		List<Expression> nested = getNested(expression);
		
		for (Expression exp : nested) {
			String identifier = exp.getIdentifier();
			
			if ("verbs".equals(identifier)) {
				parseVerbs(vocabulary, exp);
			} else if ("articles".equals(identifier)) {
				vocabulary.addArticles(getList(exp));
			} else if ("prepositions".equals(identifier)) {
				vocabulary.addPrepositions(getList(exp));
			} else if ("cantPick".equals(identifier)) {
				vocabulary.setCantPickMessage(getSimple(exp));
			} else if ("cantTraverse".equals(identifier)) {
				vocabulary.setCantTraverseMessage(getSimple(exp));
			} else if ("cantDo".equals(identifier)) {
				vocabulary.setCantDoMessage(getSimple(exp));
			} else if ("didntUnderstand".equals(identifier)) {
				vocabulary.setDidntUnderstandMessage(getSimple(exp));
			} else {
				throw new ExpressionParserException(identifier + " is not valid within expression.", exp);
			}
		}
		
		return vocabulary;
	}
	
	void parseVerbs(Vocabulary vocabulary, Expression expression) throws ExpressionParserException {
		// vocabulary definition must be compound
		List<Expression> nested = getNested(expression);
		
		for (Expression exp : nested) {
			// Each entry should be a verb group
			// (identifier is verb group, value is list of verbs in the group)
			List<String> verbsList = getList(exp);
			try {
				// Add each verb in the list to that group
				vocabulary.addVerbGroup(exp.getIdentifier(), verbsList);
			} catch (VocabularyException e) {
				// Wrap verb exceptions (probably tried to add the same verb in different groups)
				throw new ExpressionParserException(e, expression);
			}
		}
	}
	
}
