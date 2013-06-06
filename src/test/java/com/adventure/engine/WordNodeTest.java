package com.adventure.engine;

import org.junit.Before;
import org.junit.Test;

import com.adventure.engine.WordNode;

import static org.junit.Assert.*;

public class WordNodeTest {
	
	WordNode root = WordNode.newRoot();
	WordNode take = new WordNode("take", root);
	WordNode takeUp = new WordNode("up", take);
	WordNode takeOff = new WordNode("off", take);
	WordNode takeOn = new WordNode("on", take);

	@Before public void setUp() {
		root.children.put("take", take);
		take.children.put("up", takeUp);
		take.children.put("off", takeOff);
		take.children.put("on", takeOn);
	}
	
	@Test public void testToString() {
		assertEquals("", root.toString());
		assertEquals("take", take.toString());
		assertEquals("take off", takeOff.toString());
	}
	
	@Test public void testPrintTree() {
		assertEquals("[take]\n  [off]\n  [up]\n  [on]\n", root.printTree());
	}
	
	@Test public void testFindSimple() {
		String[] tokens = {"take"};
		assertEquals(take, root.find(tokens));
	}
	
	@Test public void testFindChild() {
		String[] tokens = {"take", "off"};
		assertEquals(takeOff, root.find(tokens));
	}
	
	@Test public void testFindNotChild() {
		String[] tokens = {"take", "picture"};
		assertEquals(take, root.find(tokens));
	}
	
	@Test public void testFindNot() {
		String[] tokens = {"draw"};
		assertNull(root.find(tokens));
	}
	
	@Test public void testAddSingleChild() {
		root = WordNode.newRoot();
		root.addWords("draw");
		
		// Only one child (draw) was added
		assertEquals(1, root.children.size());
		assertTrue(root.children.contains("draw"));
		assertTrue(root.children.get("draw").children.isEmpty());
	}
	
	@Test public void testAddTwoChildren() {
		root = WordNode.newRoot();
		root.addWords("look up");
		
		// Only one child (look) was added
		assertEquals(1, root.children.size());
		assertTrue(root.children.contains("look"));
		WordNode look = root.children.get("look");
		
		// With only one child (up)
		assertEquals(1, look.children.size());
		assertTrue(look.children.contains("up"));
		assertTrue(look.children.get("up").children.isEmpty());
	}
	
	@Test public void testAddOverlapping() {
		root = WordNode.newRoot();
		root.addWords("look up");
		root.addWords("look out");
		
		// Only one child (look) was added
		assertEquals(1, root.children.size());
		assertTrue(root.children.contains("look"));
		WordNode look = root.children.get("look");
		
		// With two children (up & out)
		assertEquals(2, look.children.size());
		assertTrue(look.children.contains("up"));
		assertTrue(look.children.get("up").children.isEmpty());
		assertTrue(look.children.contains("out"));
		assertTrue(look.children.get("out").children.isEmpty());
	}
	
	@Test public void testDepth() {
		assertEquals(0, root.depth());
		assertEquals(1, take.depth());
		assertEquals(2, takeOff.depth());
	}
}
