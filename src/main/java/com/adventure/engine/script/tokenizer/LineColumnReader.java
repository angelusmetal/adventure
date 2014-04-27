package com.adventure.engine.script.tokenizer;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

/**
 * Wraps {@link LineNumberReader} so that it also keeps track of the column
 * number. Additionally, normalizes line number to start in 1, instead of 0 (so
 * that it behaves the same as column number.
 * 
 * Note: it only updates the column number when using read().
 * 
 * @author Rodrigo Fernandez (angelusmetal@gmail.com)
 */
public class LineColumnReader extends LineNumberReader {

	private int column = 0;
	private int markedColumn = 0;
	
	public LineColumnReader(Reader in) {
		super(in);
		super.setLineNumber(1);
	}
	
	@Override public void mark(int readAheadLimit) throws IOException {
		markedColumn = column;
		super.mark(readAheadLimit);
	}
	
	@Override public void reset() throws IOException {
		column = markedColumn;
		super.reset();
	}
	
	@Override public int read() throws IOException {
		int c = super.read();
		if (c == '\n') {
			column = 0;
		} else {
			column++;
		}
		return c;
	}
	
	public int getColumn() {
		return column;
	}

}
