package com.adventure.engine.console;

public interface Console {

	/**
	 * Display a text in the console.
	 * @param text Text to display.
	 */
	void display(String text);
	/**
	 * Display a warning text in the console.
	 * @param text Text to display.
	 */
	void warning(String text);
	/**
	 * Display an error text in the console.
	 * @param text Text to display.
	 */
	void error(String text);
}