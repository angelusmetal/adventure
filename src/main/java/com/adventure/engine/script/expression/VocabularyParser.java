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
		
		setDefaults(vocabulary);
		
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
			} else if ("messages".equals(identifier)) {
				parseMessages(vocabulary, exp);
			} else if ("magicPhrases".equals(identifier)) {
				parseMagicPhrases(vocabulary, exp);
			} else {
				throw new ExpressionParserException(identifier + " is not valid within expression.", exp);
			}
		}
		
		return vocabulary;
	}
	
	void parseVerbs(Vocabulary vocabulary, Expression expression) throws ExpressionParserException {
		// verbs definition must be compound
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
	
	void parseMessages(Vocabulary vocabulary, Expression expression) throws ExpressionParserException {
		// messages definition must be compound
		List<Expression> nested = getNested(expression);
		
		for (Expression exp : nested) {
			// Each entry should be a message string
			vocabulary.addMessage(exp.getIdentifier(), getSimple(exp));
		}
	}
	
	void parseMagicPhrases(Vocabulary vocabulary, Expression expression) throws ExpressionParserException {
		// messages definition must be compound
		List<Expression> nested = getNested(expression);
		
		for (Expression exp : nested) {
			// Each entry should be a list
			List<String> phrases = getList(exp);
			for (String phrase : phrases) {
				vocabulary.addMagicPhrase(exp.getIdentifier(), phrase);
			}
		}
	}
	
	/**
	 * Add default values to vocabulary, in case they are not defined.
	 */
	void setDefaults(Vocabulary vocabulary) {
		vocabulary.addMessage("cantPick", "I can't pick that");
		vocabulary.addMessage("cantTraverse", "I can't go there");
		vocabulary.addMessage("cantDo", "I can't do that");
		vocabulary.addMessage("didntUnderstand", "I didn't understand that");
		vocabulary.addMessage("pickedUp", "You picked up");
		vocabulary.addMessage("emptyInventory", "You don't have anything with you right now");
		vocabulary.addMessage("changedLocation", "You went to");
	}
}
