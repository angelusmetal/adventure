package com.adventure.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.adventure.engine.Entity;
import com.adventure.engine.GameContext;
import com.adventure.engine.WordNode;
import com.adventure.engine.parser.Parser;

public class Runner {

	Parser parser = new Parser();
	WordNode verbs = WordNode.newRoot();
	List<String> articles = new ArrayList<String>();
	List<String> prepositions = new ArrayList<String>();
	GameContext context = new GameContext();
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Runner runner = new Runner();
		runner.mainLoop();
	}
	
	public Runner() {
		initParser();
		createWorld();
	}
	
	private void initParser() {
		parser.setVerbs(verbs);
		parser.setArticles(articles);
		parser.setPrepositions(prepositions);
		
		verbs.addWords("pick");
		verbs.addWords("pick up");
		verbs.addWords("look");
		verbs.addWords("look at");
		verbs.addWords("examine");
		verbs.addWords("go");
		verbs.addWords("go to");
		verbs.addWords("walk");
		verbs.addWords("use");
		verbs.addWords("open");
		
		articles.add("a");
		articles.add("the");
		
		prepositions.add("with");
	}
	
	private void createWorld() {
		
		// Objects
		final Entity fruits = new Entity();
		fruits.setName("fruits");
		fruits.setShortDescription("Some red fruits");
		fruits.setLongDescription("Small round red fruits. I hope they are edible.");
		fruits.setVisible(false);
		fruits.setPickable(true);
		
		final Entity key = new Entity();
		key.setName("key");
		key.setShortDescription("An old key");
		key.setLongDescription("A large brass key full of dirt.");
		key.setVisible(false);
		key.setPickable(true);
		
		final Entity treeMouth = new Entity() {
			@Override
			public void look(GameContext context) {
				super.look(context);
				key.setVisible(true);
			}
		};
		treeMouth.setName("tree mouth");
		treeMouth.setShortDescription("A dark, mouthlike cavity.");
		treeMouth.setLongDescription("A dark, mouthlike cavity at the bottom of the tree. It seems to have bits of bones and ashes inside, and between that mess, a brass key.");
		treeMouth.setVisible(false);
		
		final Entity tree = new Entity() {
			@Override
			public void look(GameContext context) {
				super.look(context);
				treeMouth.setVisible(true);
			}
		};
		tree.setName("tree");
		tree.setShortDescription("evil looking tree");
		tree.setLongDescription("The trunk looks as if a giant hand had twisted its very fabric in evil delight, its branches a dozen hands reaching out threateningly; or perhaps, desperately. It gives you the creeps. You notice a small cavity near the roots. Perhaps you're imagining too much, but it does look like a mouth.");
		tree.setVisible(false);
		
		// Locations
		final Entity frontYard = new Entity() {
			@Override
			public void look(GameContext context) {
				super.look(context);
				tree.setVisible(true);
				fruits.setVisible(true);
			}
		};
		frontYard.setName("front yard");
		frontYard.setShortDescription("the front yard");
		frontYard.setLongDescription("The ground is covered with a thick carpet of long dead brown grass, split by a winding pebbled path that leads from the front gate to the house's main door. On one side of the path, an eerie looking tree looms over with a threatening look, its bony fingers trying to reach out. On the other side there are some bushes from which hang ripe red fruits, waiting to be plucked.");
		frontYard.setTraversable(true);
		
		final Entity houseEntry = new Entity();
		houseEntry.setName("house entry");
		houseEntry.setShortDescription("the house's main door entry");
		houseEntry.setLongDescription("The 2 massive wooden doors are shut, its outward faces inlaid with intrincate mirrored drawings of exquisite craft, altough kissed by the unkind passage of time. The are three wooden steps that cry under every footstep, and a small canopy that used to protect from the rain, but now has evidently fed generations of termites.");
		houseEntry.setTraversable(true);
		
		final Entity mainHall = new Entity();
		mainHall.setName("main hall");
		mainHall.setShortDescription("the main hall");
		mainHall.setLongDescription("The main hall sports an impressive look. A very tall ceiling. A huge carpeted stairway leads to the top floor. The walls are covered by a combination of boisserie and plum brocatto panels. On the walls on either side hang very large paintings of a man and a woman, respectively. Probably the former owners of the state. On the left side (west) there are 2 hallways; one by the entrance (southwest) and the other by the far side (northwest). On the right (east), there is another hallway and a door.");
		mainHall.setTraversable(false);
		
		final Entity library = new Entity();
		library.setName("library");
		library.setShortDescription("the library");
		library.setLongDescription("The library is as tall as the house and two of its four walls frame a huge window hidden behind a thick curtain, which no doubt played an important part on preserving the countless books that sit on the massive bookshelves. There are thousands of books, several lifespans of reading. The corner of the room where the 2 windowed walls meet is rounded, so it, in fact, is one continuous window that spans the two walls. And along its whole run, a likewise long and curved seat housed confortable readings by the sunlight. An ebony table seats at one side with a lamp and a magnifying glass.");
		library.setTraversable(true);
		
		final Entity southwestHall = new Entity();
		southwestHall.setName("northwest hall");
		southwestHall.setShortDescription("the west hall by the front");
		southwestHall.setLongDescription("The hall has 2 windows that provide a view to the front yard and plenty of sunlight. At one end of the hall there is an opening that leads to the main hall. On the other side there is a door.");
		southwestHall.setTraversable(true);
		
		final Entity northwestHall = new Entity();
		northwestHall.setName("southwest hall");
		northwestHall.setShortDescription("the west hall by the stairs");
		northwestHall.setLongDescription("The hall has no windows, so the light used to be provided by 2 ceiling lamps. At one end of the hall there is an opening that leads to the main hall. On the other side there is a door. In the middle of one side, there is double door.");
		northwestHall.setTraversable(true);
		
		final Entity door = new Entity() {
			@Override
			public boolean handle(GameContext context, String verb, Entity modifier) {
				if ("open".equals(verb) || modifier == key) {
					mainHall.setTraversable(true);
					context.display("You opened the door of the house. The doors open with a deep sound that echo inside.");
					return true;
				}
				return false;
			}
		};
		
		door.setName("main door");
		door.setShortDescription("The main door");
		door.setLongDescription("The massive wooden doors are locked shut");
		
		// Connections
		frontYard.addEntity(houseEntry, "house");
		frontYard.addEntity(houseEntry, "door");
		frontYard.addEntity(houseEntry, "main door");
		frontYard.addEntity(tree, "tree");
		frontYard.addEntity(treeMouth, "cavity");
		frontYard.addEntity(treeMouth, "tree cavity");
		frontYard.addEntity(treeMouth, "mouth");
		frontYard.addEntity(treeMouth, "tree mouth");
		frontYard.addEntity(key, "key");
		frontYard.addEntity(key, "brass key");
		frontYard.addEntity(fruits, "fruit");
		frontYard.addEntity(fruits, "fruits");
		frontYard.addEntity(fruits, "berries");
		frontYard.addEntity(fruits, "red fruit");
		frontYard.addEntity(fruits, "red fruits");
		frontYard.addEntity(fruits, "red berries");
		
		houseEntry.addEntity(frontYard, "front yard");
		houseEntry.addEntity(frontYard, "yard");
		houseEntry.addEntity(frontYard, "entrance");
		houseEntry.addEntity(mainHall, "inside");
		houseEntry.addEntity(mainHall, "front yard");
		houseEntry.addEntity(door, "door");
		houseEntry.addEntity(door, "doors");
		houseEntry.addEntity(door, "massive doors");
		
		mainHall.addEntity(houseEntry, "main door");
		mainHall.addEntity(houseEntry, "outside");
//		mainHall.addEntity(southwestHall, "southwest hall");
//		mainHall.addEntity(northwestHall, "northwest hall");
//		mainHall.addEntity(library, "east door");
		
		// Starting location
		context.setCurrentLocation(frontYard);
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
