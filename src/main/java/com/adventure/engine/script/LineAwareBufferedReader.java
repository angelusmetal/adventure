package com.adventure.engine.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class LineAwareBufferedReader extends BufferedReader {

	private int currentLine = 0;
	private String cache = null;
	
	public LineAwareBufferedReader(Reader in) {
		super(in);
	}
	
	@Override
	public String readLine() throws IOException {
		currentLine++;
		if (cache != null) {
			String line = cache;
			cache = null;
			return line;
		} else {
			return super.readLine();
		}
	}
	
	public void putBack(String line) {
		cache = line;
	}
	
	public int getCurrentLine() {
		return currentLine;
	}
	
}
