package com.adventure.game;

import gnu.trove.map.hash.THashMap;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.adventure.engine.Entity;
import com.adventure.engine.GameContext;
import com.adventure.engine.WordNode;
import com.adventure.engine.parser.Parser;
import com.adventure.engine.script.GameContextReader;
import com.adventure.engine.script.GameContextReader.GameContextReaderException;
import com.adventure.engine.script.ScriptParser.ScriptParsingException;
import com.adventure.engine.script.grammar.Expression;

public class Runner {

	Parser parser = new Parser();
	WordNode verbs = WordNode.newRoot();
	THashMap<String, List<String>> verbGroups = new THashMap<String,List<String>>();
	THashMap<String, String> verbToGroup = new THashMap<String, String>();
	List<String> articles;
	List<String> prepositions;
	GameContext context = new GameContext();
	Entity main;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Runner runner = new Runner();
		runner.mainLoop();
	}
	
	public Runner() {
		readScript();
		initParser();
	}
	
	private void initParser() {
		// Configure verbs
		parser.setVerbs(verbs);
		List<Expression> verbExpressions = main.getProperty("verbs").getNested();
		for (Expression expression : verbExpressions) {
			List<String> verbList = expression.getValue().getAsList();
			List<String> verbsInGroup = new ArrayList<String>();
			for (String verb : verbList) {
				verbs.addWords(verb); // add verb to verb tree
				verbsInGroup.add(verb); // add verb to current group
				verbToGroup.put(verb, expression.getIdentifier()); // map verb to its group name 
			}
			// Register verb group
			verbGroups.put(expression.getIdentifier(), verbsInGroup);
		}
		
		// Configure articles
		List<String> articles = main.getProperty("articles").getAsList();
		parser.setArticles(articles);
		
		// Configure prepositions
		List<String> prepositions = main.getProperty("prepositions").getAsList();
		parser.setPrepositions(prepositions);

	}
	
	private void readScript() {
		try {
			GameContextReader reader = new GameContextReader();
			context = reader.initialize(new FileInputStream("sample.fiction"));
			
			main = context.getEntity("main");
			
			// Set starting location
			String startingLocation = main.getProperty("startingLocation").getAsString();
			context.setCurrentLocation(context.getEntity(startingLocation));

//			System.out.println(context.showContent());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptParsingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GameContextReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void mainLoop() {
		context.display("Adventure :)");
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		String sentence;
		try {
			context.displayPrompt();
			while ((sentence = bf.readLine()) != null) {
				parser.parse(sentence, context);
				context.displayPrompt();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
