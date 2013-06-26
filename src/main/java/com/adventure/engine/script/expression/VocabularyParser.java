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
			} else if ("specialEntities".equals(identifier)) {
				parseSpecialEntities(vocabulary, exp);
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
	
	/**
	 * Add each message to the vocabulary
	 */
	void parseMessages(Vocabulary vocabulary, Expression expression) throws ExpressionParserException {
		List<Expression> nested = getNested(expression);
		for (Expression exp : nested) {
			if (!isValidMessage(exp.getIdentifier())) {
				throw new ExpressionParserException("'" + exp.getIdentifier() + "' is not valid message type.", exp);
			}
			vocabulary.addMessage(exp.getIdentifier(), getSimple(exp));
		}
	}
	
	boolean isValidMessage(String identifier) {
		return  "cantPick".equals(identifier) ||
				"cantTraverse".equals(identifier) ||
				"cantDo".equals(identifier) ||
				"didntUnderstand".equals(identifier) ||
				"pickedUp".equals(identifier) ||
				"emptyInventory".equals(identifier) ||
				"changedLocation".equals(identifier) ||
				"currentLocation".equals(identifier);
	}
	
	/**
	 * Add each magic phrase to the vocabulary
	 */
	void parseMagicPhrases(Vocabulary vocabulary, Expression expression) throws ExpressionParserException {
		List<Expression> nested = getNested(expression);
		for (Expression exp : nested) {
			List<String> phrases = getList(exp);
			for (String phrase : phrases) {
				if (!isValidMagicPhrase(exp.getIdentifier())) {
					throw new ExpressionParserException("'" + exp.getIdentifier() + "' is not valid magic phrase.", exp);
				}
				vocabulary.addMagicPhrase(exp.getIdentifier(), phrase);
			}
		}
	}

	boolean isValidMagicPhrase(String identifier) {
		return  "exit".equals(identifier) ||
				"whereAmI".equals(identifier) ||
				"easterEgg".equals(identifier);
	}
	
	/**
	 * Add special entity to the vocabulary
	 */
	void parseSpecialEntities(Vocabulary vocabulary, Expression expression) throws ExpressionParserException {
		List<Expression> nested = getNested(expression);
		for (Expression exp : nested) {
			List<String> entities = getList(exp);
			for (String entity : entities) {
				if (!isValidSpecialEntity(exp.getIdentifier())) {
					throw new ExpressionParserException("'" + exp.getIdentifier() + "' is not valid special entity.", exp);
				}
				vocabulary.addSpecialEntity(exp.getIdentifier(), entity);
			}
		}
	}
	
	boolean isValidSpecialEntity(String identifier) {
		return  "thisLocation".equals(identifier) ||
				"lastLocation".equals(identifier) ||
				"inventory".equals(identifier);
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
