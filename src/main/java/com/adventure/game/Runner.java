package com.adventure.game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.adventure.engine.Entity;
import com.adventure.engine.GameContext;
import com.adventure.engine.console.Parser;
import com.adventure.engine.console.Vocabulary;
import com.adventure.engine.script.GameContextReader;
import com.adventure.engine.script.expression.ExpressionParserException;
import com.adventure.engine.script.expression.VocabularyParser;
import com.adventure.engine.script.syntax.SyntaxParser.ScriptParsingException;

public class Runner {

	Parser parser = new Parser();
	Vocabulary verbs = new Vocabulary();
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
		try {
			readScript();
			initParser();
		} catch (ExpressionParserException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void initParser() throws ExpressionParserException {
		
//		// Get verbs from the main object
//		VocabularyParser verbsParser = new VocabularyParser();
//		Entity voc = context.getEntity("vocabulary");
//		
//		// Parse them
//		Vocabulary verbs = verbsParser.parse(main.getProperty("verbs"));
		
		// Make the interested parties aware of them
		parser.setVocabulary(context.getVocabulary());
		
//		// Configure articles
//		List<String> articles = main.getProperty("articles").getValue().getAsList();
//		parser.setArticles(articles);
//		
//		// Configure prepositions
//		List<String> prepositions = main.getProperty("prepositions").getValue().getAsList();
//		parser.setPrepositions(prepositions);
//
	}
	
	private void readScript() throws ExpressionParserException {
		try {
			GameContextReader reader = new GameContextReader();
			context = reader.readFrom(new FileInputStream("sample.fiction"));
			
			main = context.getEntity("main");
			
			// Set starting location
			String startingLocation = main.getProperty("startingLocation").getValue().getAsString();
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
