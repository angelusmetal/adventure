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
import com.adventure.engine.parser.Verbs;
import com.adventure.engine.parser.Verbs.VerbsException;
import com.adventure.engine.script.GameContextReader;
import com.adventure.engine.script.ScriptParser;
import com.adventure.engine.script.GameContextReader.GameContextReaderException;
import com.adventure.engine.script.ScriptParser.ScriptParsingException;
import com.adventure.engine.script.grammar.Expression;

public class Runner {

	Parser parser = new Parser();
	Verbs verbs = new Verbs();
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
		try {
			initParser();
		} catch (VerbsException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void initParser() throws VerbsException {
		// Configure verbs
		parser.setVerbs(verbs);
		List<Expression> verbExpressions = main.getProperty("verbs").getNested();
		for (Expression expression : verbExpressions) {
			if (expression.getValue().isCompound()) {
//				throw new Exception("Verb group definitions cannot be compound expressions");
			}
			verbs.addVerbGroup(expression.getIdentifier(), expression.getValue().getAsList());
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
			context = reader.readFrom(new FileInputStream("sample.fiction"));
			
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
