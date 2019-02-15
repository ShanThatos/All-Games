package game._start;

import game.board.GameManager;
import game.input.TextHandler;

public class TextRunner {
	public static void main(String[] args) {
		TextHandler input = new TextHandler();
		GameManager gm = new GameManager(5, input);
		gm.start();
	}
}
