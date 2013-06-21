package com.adventure.engine.console;

public interface ParserReceiver {
	void doAction(String verb);
	void doActionOnObject(String verb, String subject);
	void doActionOnObjectWithModifier(String verb, String subject, String preposition, String modifier);
}
