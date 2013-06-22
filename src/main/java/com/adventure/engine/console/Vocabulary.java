package com.adventure.engine.console;

import java.util.ArrayList;
import java.util.List;

import gnu.trove.map.hash.THashMap;

import com.adventure.engine.WordNode;

/**
 * This manages everything related to vocabulary. Until I find a better way or name
 * for it.
 * 
 * @author Rodrigo Fernández (angelusmetal@gmail.com)
 *
 */
public class Vocabulary {

	/**
	 * Word tree of words with all the known verbs.
	 */
	private WordNode verbTree = WordNode.newRoot();
	/**
	 * Map containing the verb groups. Each key (verb group name) maps to the
	 * list of verbs in that group.
	 */
	private THashMap<String, List<String>> verbGroups = new THashMap<String,List<String>>();
	/**
	 * Maps each verb to its corresponding verb group. Obviously a verb can't
	 * map to more than one group.
	 */
	private THashMap<String, String> verbGroupMapper = new THashMap<String,String>();
	/**
	 * List of known articles. Articles will be recognized as pertaining to the
	 * object or modifier, but will be discarded from it, during parsing.
	 */
	private List<String> articles = new ArrayList<String>();
	/**
	 * List of known prepositions.
	 */
	private List<String> prepositions = new ArrayList<String>();
	/**
	 * Map of messages. Each key (message identifier) maps to the message as is
	 * displayed to the player.
	 */
	private THashMap<String, String> messages = new THashMap<String, String>();
	/**
	 * Map of magic phrases. Each key (actual phrase) maps to an identifier.
	 * The mapping is done in this fashion because it is easier to check the
	 * magic phrase, as entered, in the map (as key) and get the corresponding
	 * identifier, if it's a known magic phrase. If a magic phrase has various
	 * alternatives, all of them will be different keys that map to the same
	 * identifier as value.
	 */
	private THashMap<String, String> magicPhrases = new THashMap<String, String>();
	
	public void addVerbGroup(String group, List<String> verbs) throws VocabularyException {
		verbGroups.put(group, verbs);
		for (String verb : verbs) {
			verbTree.addWords(verb);
			if (verbGroupMapper.contains(verb)) {
				throw new VocabularyException("Verb " + verb + " is already on group " + verbGroupMapper.get(verb) + ". Cannot be on group " + group + " at the same time.");
			}
			verbGroupMapper.put(verb, group);
		}
	}
	
	public void addArticles(List<String> articles) {
		this.articles.addAll(articles);
	}
	
	public List<String> getArticles() {
		return articles;
	}
	
	public void addPrepositions(List<String> prepositions) {
		this.prepositions.addAll(prepositions);
	}
	
	public List<String> getPrepositions() {
		return prepositions;
	}
	
	public WordNode getVerbTree() {
		return verbTree;
	}
	
	/**
	 * Returns the group a verb belongs to, or null if it doesn't belong to any.
	 */
	public String getVerbGroup(String verb) {
		return verbGroupMapper.get(verb);
	}
	
	public void addMessage(String identifier, String message) {
		messages.put(identifier, message);
	}
	
	public THashMap<String, String> getMessages() {
		return messages;
	}
	
	public void addMagicPhrase(String identifier, String phrase) {
		// Here the identifier is value instead of key, because the lookup will
		// be done the other way around.
		magicPhrases.put(phrase, identifier);
	}
	
	public THashMap<String, String> getMagicPhrases() {
		return magicPhrases;
	}
	
	public class VocabularyException extends Exception {
		private static final long serialVersionUID = 1L;
		public VocabularyException (String msg) {
			super(msg);
		}
	}

}
