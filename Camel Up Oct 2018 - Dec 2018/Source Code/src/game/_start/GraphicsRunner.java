package game._start;

import game.board.GameManager;
import game.graphics.MainFrame;
import game.graphics.UI.CImage;
import game.input.GraphicsHandler;

public class GraphicsRunner {
	public static void main(String[] args) {
		GraphicsHandler input = new GraphicsHandler();
		GameManager.usingGraphics = true;
		GameManager gm = new GameManager(5, input);
		MainFrame fr = new MainFrame(gm, input);
		gm.start();
		fr.start();
	}
}
