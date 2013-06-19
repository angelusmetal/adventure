package com.adventure.game;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.adventure.engine.GameContext;
import com.adventure.engine.console.Parser;
import com.adventure.engine.console.Vocabulary;
import com.adventure.engine.entity.Entity;
import com.adventure.engine.script.GameContextReader;
import com.adventure.engine.script.expression.ExpressionParserException;
import com.adventure.engine.script.syntax.SyntaxParser.ScriptParsingException;

public class Runner {

	Parser parser = new Parser();
	Vocabulary verbs = new Vocabulary();
	List<String> articles;
	List<String> prepositions;
	GameContext context = new GameContext();
	Entity start;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("You should provide a fiction file.");
			System.exit(1);
		}
		String filename = args[0];
		Runner runner = new Runner(filename);
		runner.mainLoop();
	}
	
	public Runner(String filename) {
		try {
			readScript(filename);
		} catch (ExpressionParserException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ScriptParsingException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private void readScript(String filename) throws ExpressionParserException, FileNotFoundException, IOException, ScriptParsingException {
		GameContextReader reader = new GameContextReader();
		context = reader.readFrom(new FileInputStream(filename));
		
		// Configure parser
		parser.setVocabulary(context.getVocabulary());

		// Start configuration
		start = context.getEntity("@start");
		
		String startingLocation = start.getProperty("location").getValue().getAsString();
		context.setCurrentLocation(context.getEntity(startingLocation));

		String welcome = start.getProperty("welcome").getValue().getAsString();
		context.display(welcome);
	}
	
	private void mainLoop() {
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
