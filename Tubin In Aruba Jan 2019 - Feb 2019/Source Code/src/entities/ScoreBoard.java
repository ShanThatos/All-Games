package entities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import game.GamePanel;
import imaging.ImageManipulator;

public class ScoreBoard extends DrawableEntity {
	
	private Player ply;
	
	public ScoreBoard(Player ply) {
		super("UI2", GamePanel.WIDTH - 100, 10, 0, 0);
		this.ply = ply;
	}
	
	@Override
	public void draw(Graphics2D gr) {
		Graphics2D g = (Graphics2D) gr.create();
		g.translate(x, y);
		
		g.setColor(Color.white);
		g.fillRect(0, 0, 90, 90);
		g.setStroke(new BasicStroke(3));
		g.setColor(Color.black);
		g.drawRect(0, 0, 90, 90);
		g.setStroke(new BasicStroke(1));
		g.setFont(new Font("Arial", Font.BOLD, 50));
		String text = ply.getScore() + "";
		int[] crds = ImageManipulator.getCenteredTextCoords(g, text, 90, 90);
		g.setColor(Color.black);
		g.drawString(text, crds[0], crds[1]);
	}

	@Override
	public void update(double dt) {
		moveUpdate(dt);
	}
}
