package com.adventure.engine.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.adventure.engine.WordNode;
import com.adventure.engine.parser.Parser;
import com.adventure.engine.parser.ParserReceiver;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.*;

public class ParserTest {

	Parser parser = new Parser();
	@Mock ParserReceiver context;
	WordNode verbs = WordNode.newRoot();
	List<String> articles = new ArrayList<String>();
	List<String> prepositions = new ArrayList<String>();
	
	@Before public void setUp() {
		initMocks(this);
		
		verbs.addWords("pick up");
		articles.add("the");
		prepositions.add("with");
		
		parser.setVerbs(verbs);
		parser.setArticles(articles);
		parser.setPrepositions(prepositions);
	}
	
	@Test public void testSingleVerb() {
		parser.parse("pick", context);
		verify(context).doAction("pick");
	}
	
	@Test public void testCompoundVerb() {
		parser.parse("pick up", context);
		verify(context).doAction("pick up");
	}
	
	@Test public void testSingleVerbObject() {
		parser.parse("pick coal", context);
		verify(context).doActionOnObject("pick", "coal");
	}
	
	@Test public void testSingleVerbArticleObject() {
		parser.parse("pick the coal", context);
		verify(context).doActionOnObject("pick", "coal");
	}
	
	@Test public void testCompoundVerbObject() {
		parser.parse("pick up coal", context);
		verify(context).doActionOnObject("pick up", "coal");
	}
	
	@Test public void testCompoundVerbArticleObject() {
		parser.parse("pick up the coal", context);
		verify(context).doActionOnObject("pick up", "coal");
	}
	
	@Test public void testCompoundVerbArticleObjectWithModifier() {
		parser.parse("pick up the coal with gloves", context);
		verify(context).doActionOnObjectWithModifier("pick up", "coal", "gloves");
	}
}
