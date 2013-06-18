package com.adventure.engine.console;

import java.util.ArrayList;
import java.util.List;

import gnu.trove.map.hash.THashMap;

import com.adventure.engine.WordNode;

/**
 * This manages everything related to verbs. Until I find a better way or name
 * for it.
 * 
 * @author Rodrigo Fernández (angelusmetal@gmail.com)
 *
 */
public class Vocabulary {

	private WordNode verbTree = WordNode.newRoot();
	private THashMap<String, List<String>> verbGroups = new THashMap<String,List<String>>();
	private THashMap<String, String> verbGroupMapper = new THashMap<String,String>();
	private List<String> articles = new ArrayList<String>();
	private List<String> prepositions = new ArrayList<String>();
	
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
	
	public void addPrepositions(List<String> prepositions) {
		this.prepositions.addAll(prepositions);
	}
	
	public WordNode getVerbTree() {
		return verbTree;
	}
	
	public List<String> getArticles() {
		return articles;
	}
	
	public List<String> getPrepositions() {
		return prepositions;
	}
	
	/**
	 * Returns the group a verb belongs to, or null if it doesn't belong to any.
	 */
	public String getVerbGroup(String verb) {
		return verbGroupMapper.get(verb);
	}
	
	public class VocabularyException extends Exception {
		private static final long serialVersionUID = 1L;
		public VocabularyException (String msg) {
			super(msg);
		}
	}
}
