package com.adventure.engine.console;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.adventure.engine.WordNode;
import com.adventure.engine.script.evaluation.EvaluationException;

public class Parser {

	Vocabulary vocabulary;
	
	public void setVocabulary(Vocabulary vocabulary) {
		this.vocabulary = vocabulary;
	}
	
	public void parse(String sentence, ParserReceiver receiver, Console console) throws EvaluationException {
		String normalized = sentence.trim().toLowerCase();
		
		// Check if sentence is a known magic phrase
		String magicPhrase = vocabulary.getMagicPhrases().get(normalized);
		if (magicPhrase != null) {
			receiver.magicPhrase(magicPhrase);
			return;
		}
		
		// Tokenize
		String[] tokens = StringUtils.split(normalized);
		
		// Lookup verb
		WordNode verb = vocabulary.getVerbTree().find(tokens);
		
		if (verb == null) {
			console.display(vocabulary.getMessages().get("didntUnderstand"));
			return;
		}
		
		// Identify subject
		if (verb.depth() < tokens.length) {
			List<String> subject = new ArrayList<String>();
			int i = verb.depth();
			
			// Add first token, only if it's not an article
			String token = tokens[i];
			if (!vocabulary.getArticles().contains(token)) {
				subject.add(token);
			}
			
			// Add the remaining tokens until a preposition is found 
			String preposition = null;
			for (i++; i < tokens.length; ++i) {
				token = tokens[i];
				// Preposition found
				if (vocabulary.getPrepositions().contains(token)) {
					preposition = token;
					i++;
					break;
				}
				subject.add(token);
			}
			
			// Get Modifier
			if (i < tokens.length) {
				String modifier = "";
				modifier = tokens[i++];
				for (; i < tokens.length; ++i) {
					modifier += " " + tokens[i];
				}
				receiver.doActionOnObjectWithModifier(verb.toString(), StringUtils.join(subject, ' '), preposition, modifier);
			} else {
				receiver.doActionOnObject(verb.toString(), StringUtils.join(subject, ' '));
			}
		} else {
			receiver.doAction(verb.toString());
		}
	}

}
