package com.adventure.engine.console;

import com.adventure.engine.script.evaluation.EvaluationException;

public interface ParserReceiver {
	void doAction(String verb);
	void doActionOnObject(String verb, String subject) throws EvaluationException;
	void doActionOnObjectWithModifier(String verb, String subject, String preposition, String modifier) throws EvaluationException;
	void magicPhrase(String magicPhrase);
}
