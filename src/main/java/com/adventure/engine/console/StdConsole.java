package com.adventure.engine.console;

public class StdConsole implements Console {

	/**
	 * Console width. Used to do word wrap.
	 */
	private final int width;

	public StdConsole(final int width) {
		this.width = width;
	}
	
	@Override
	public void display(final String text) {
		String line = text;
		while (line.length() > width) {
			for (int cut = width; cut > 0; cut--) {
				if (Character.isWhitespace(line.charAt(cut))) {
					doDisplay(line.substring(0,cut));
					line = line.substring(cut + 1);
					break;
				}
			}
		}
		doDisplay(line);
	}

	@Override
	public void warning(String text) {
		display(text);
	}

	@Override
	public void error(String text) {
		display(text);
	}
	
	private void doDisplay(String text) {
		System.out.println(text);
	}

}
