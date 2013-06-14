package com.adventure.engine.parser;

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
public class Verbs {

	private WordNode tree = WordNode.newRoot();
	private THashMap<String, List<String>> groups = new THashMap<String,List<String>>();
	private THashMap<String, String> groupMapper = new THashMap<String,String>();
	
	public void addVerbGroup(String group, List<String> verbs) throws VerbsException {
		groups.put(group, verbs);
		for (String verb : verbs) {
			tree.addWords(verb);
			if (groupMapper.contains(verb)) {
				throw new VerbsException("Verb " + verb + " is already on group " + groupMapper.get(verb) + ". Cannot be on group " + group + " at the same time.");
			}
			groupMapper.put(verb, group);
		}
	}
	
	public WordNode getTree() {
		return tree;
	}
	
	public class VerbsException extends Exception {
		private static final long serialVersionUID = 1L;
		public VerbsException (String msg) {
			super(msg);
		}
	}
}
