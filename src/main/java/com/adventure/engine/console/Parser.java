package com.adventure.engine.console;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.adventure.engine.WordNode;

public class Parser {

	Vocabulary verbs;
	List<String> articles = Collections.emptyList();
	List<String> prepositions = Collections.emptyList();
	
	public void setVocabulary(Vocabulary verbs) {
		this.verbs = verbs;
	}
	
	public void setArticles(List<String> articles) {
		this.articles = articles;
	}
	
	public void setPrepositions(List<String> prepositions) {
		this.prepositions = prepositions;
	}
	
	public void parse(String sentence, ParserReceiver receiver) {
		// Tokenize
		String[] tokens = StringUtils.split(sentence.toLowerCase());
		
		// Lookup verb
		WordNode verb = verbs.getVerbTree().find(tokens);
		
		if (verb == null) {
			receiver.display("Didn't understand: " + sentence);
			return;
		}
		
		// Identify subject
		if (verb.depth() < tokens.length) {
			List<String> subject = new ArrayList<String>();
			int i = verb.depth();
			
			// Add first token, only if it's not an article
			String token = tokens[i];
			if (!articles.contains(token)) {
				subject.add(token);
			}
			
			// Add the remaining tokens until a preposition is found 
			String preposition = null;
			for (i++; i < tokens.length; ++i) {
				token = tokens[i];
				// Preposition found
				if (prepositions.contains(token)) {
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
