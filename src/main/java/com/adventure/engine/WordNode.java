package com.adventure.engine;

import org.apache.commons.lang3.StringUtils;

import gnu.trove.map.hash.THashMap;
import gnu.trove.procedure.TObjectObjectProcedure;

/**
 * A node expressing a word hierarchy.
 * @author Rodrigo Fernandez (angelusmetal@gmail.com)
 */
public class WordNode {

	final THashMap<String, WordNode> children = new THashMap<String, WordNode>();
	final WordNode parent;
	final String word;

	public WordNode(String word, WordNode parent) {
		this.parent = parent;
		this.word = word;
	}
	
	static public WordNode newRoot() {
		return new WordNode("", null);
	}
	
	/**
	 * Add one or more hierarchical words, each one as a sub-node of the
	 * previous one.
	 * @param words One or more words, separated by whitespace.
	 */
	public void addWords(String words) {
		String[] tokens = StringUtils.split(words);
		addChildren(tokens, 0);
	}
	
	void addChildren(String[] tokens, int offset) {
		if (offset < tokens.length) {
			WordNode child;
			if (children.contains(tokens[offset])) {
				child = children.get(tokens[offset]);
			} else {
				child = new WordNode(tokens[offset], this);
			}
			children.put(tokens[offset], child);
			child.addChildren(tokens, offset+1);
		}
	}
	
	/**
	 * Traverses as many nodes as matching tokens and returns the farthest
	 * match, or null if there was no match.
	 * @param tokens Matching tokens
	 */
	public WordNode find(String[] tokens) {
		return find(tokens, 0);
	}
	
	WordNode find(String[] tokens, int offset) {
		if (offset < tokens.length && children.contains(tokens[offset])) {
			WordNode child = children.get(tokens[offset]);
			WordNode deeper = child.find(tokens, offset + 1);
			return deeper == null ? child : deeper;
		} else {
			return null;
		}
	}
	
	@Override
	public String toString() {
		if (parent == null) {
			return word;
		}
		if (parent.word.isEmpty()) {
			return word;
		}
		return parent + " " + word;
	}
	
	public int depth() {
		return parent == null ? 0 : parent.depth() + 1;
	}
	
	/**
	 * Print tree of this node as a String.
	 */
	public String printTree() {
		StringBuilder sb = new StringBuilder();
		toString(sb, 0);
		return sb.toString();
	}
	
	void toString(final StringBuilder sb, final int padding) {
		children.forEachEntry(new TObjectObjectProcedure<String, WordNode>() {
			public boolean execute(String token, WordNode node) {
				for (int i = 0; i < padding; ++i) { sb.append(' '); }
				sb.append('[').append(token).append(']').append('\n');
				node.toString(sb, padding+2);
				return true;
			}
		});
	}
}
