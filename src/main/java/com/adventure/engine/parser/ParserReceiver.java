package com.adventure.engine.parser;

public interface ParserReceiver {
	void doAction(String verb);
	void doActionOnObject(String verb, String subject);
	void doActionOnObjectWithModifier(String verb, String subject, String modifier);
	void display(String string);
}
