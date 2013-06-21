package com.adventure.engine.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.adventure.engine.WordNode;
import com.adventure.engine.console.Console;
import com.adventure.engine.console.Parser;
import com.adventure.engine.console.ParserReceiver;
import com.adventure.engine.console.Vocabulary;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.*;

public class ParserTest {

	Parser parser = new Parser();
	@Mock ParserReceiver context;
	@Mock Console console;
	@Mock Vocabulary vocabulary;
	WordNode verbTree = WordNode.newRoot();
	List<String> articles = new ArrayList<String>();
	List<String> prepositions = new ArrayList<String>();
	
	@Before public void setUp() {
		initMocks(this);
		
		when(vocabulary.getVerbTree()).thenReturn(verbTree);
		when(vocabulary.getArticles()).thenReturn(articles);
		when(vocabulary.getPrepositions()).thenReturn(prepositions);
		
		verbTree.addWords("pick up");
		articles.add("the");
		prepositions.add("with");
		
		parser.setVocabulary(vocabulary);
	}
	
	@Test public void testSingleVerb() {
		parser.parse("pick", context, console);
		verify(context).doAction("pick");
	}
	
	@Test public void testCompoundVerb() {
		parser.parse("pick up", context, console);
		verify(context).doAction("pick up");
	}
	
	@Test public void testSingleVerbObject() {
		parser.parse("pick coal", context, console);
		verify(context).doActionOnObject("pick", "coal");
	}
	
	@Test public void testSingleVerbArticleObject() {
		parser.parse("pick the coal", context, console);
		verify(context).doActionOnObject("pick", "coal");
	}
	
	@Test public void testCompoundVerbObject() {
		parser.parse("pick up coal", context, console);
		verify(context).doActionOnObject("pick up", "coal");
	}
	
	@Test public void testCompoundVerbArticleObject() {
		parser.parse("pick up the coal", context, console);
		verify(context).doActionOnObject("pick up", "coal");
	}
	
	@Test public void testCompoundVerbArticleObjectWithModifier() {
		parser.parse("pick up the coal with gloves", context, console);
		verify(context).doActionOnObjectWithModifier("pick up", "coal", "with", "gloves");
	}
	
	@Test public void testCompoundVerbArticleObjectWithModifierUnknownPreposition() {
		parser.parse("pick up the coal under gloves", context, console);
		verify(context, never()).doActionOnObjectWithModifier(anyString(), anyString(), anyString(), anyString());
	}
}
