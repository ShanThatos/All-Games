package game.board;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Random;

import game.graphics.MainFrame;

public class Pyramid {
	
	// All the Dice
	private PyramidDie[] dice;
	
	// The Dice inside and outside the pyramid
	private ArrayList<PyramidDie> inside;
	private ArrayList<PyramidDie> outside;
	
	// Random to pick the next die
	private Random rand;
	
	// Drawing Data
	private int x, y, width, height;
	private Color light, dark;
	
	public Pyramid() {
		inside = new ArrayList<>();
		outside = new ArrayList<>();
		dice = new PyramidDie[5];
		for (int camelID = 0; camelID < 5; camelID++) {
			dice[camelID] = new PyramidDie(camelID);
			inside.add(dice[camelID]);
		}
		rand = new Random();
		
		if (GameManager.usingGraphics)
			setDrawingData();
	}
	
	public PyramidDie getRandomDie() {
		if (inside.isEmpty())
			return null;
		PyramidDie die = inside.remove(rand.nextInt(inside.size()));
		outside.add(die);
		return die;
	}
	
	public boolean isInsideEmpty() {
		return inside.isEmpty();
	}
	
	public void returnOutsideDieToInside() {
		inside.addAll(outside);
		outside.clear();
		for (int i = 0; i < dice.length; i++)
			dice[i].reset();
	}
	
	public void setDrawingData() {
		width = 300;
		height = 300;
		x = MainFrame.HEIGHT / 2 - width / 2;
		y = MainFrame.HEIGHT / 2 - height / 2;
		
		light = new Color(255, 195, 77);
		dark = light.darker();
	}
	
	public void drawPyramid(Graphics2D gg) {		
		Graphics2D g = (Graphics2D) gg.create();
		double[] offset = {0, .15, .3, .45, .6};
		
		int centerX = x + width / 2, centerY = y + height / 2;
		g.translate(centerX, centerY);
		
		g.setColor(light);
		g.fillRect(-width / 2, -height / 2, width, height);
		Path2D path = new Path2D.Double();
		path.moveTo(-.5, -.5);
		path.lineTo(-.5, .5);
		path.lineTo(.5, .5);
		path.closePath();
		path.transform(AffineTransform.getScaleInstance(width, height));
		g.setColor(dark);
		g.fill(path);
		g.setColor(Color.black);
		g.drawLine(-width / 2, -height / 2, width / 2, height / 2);
		g.drawLine(-width / 2, height / 2, width / 2, -height / 2);
		
		for (int i = 0; i < offset.length; i++) {
			int w2 = (int) (width * (1 - offset[i]));
			int h2 = (int) (height * (1 - offset[i]));
			if (i == offset.length - 1) {
				g.setColor(light.brighter());
				g.fillRect(-w2 / 2, -h2 / 2, w2, h2);
				g.setColor(Color.black);
			}
			g.drawRect(-w2 / 2, -h2 / 2, w2, h2);
		}
	}
	
	public void drawDice(Graphics2D gg) {
		if (!inside.isEmpty()) {
			double da = 360 / inside.size();
			double radius = 40;
			for (double sa = (inside.size() % 2 == 0 ? -45 : -90), i = 0; i < inside.size(); sa -= da, i++) {
				int x = (int)(Math.cos(Math.toRadians(sa)) * radius);
				int y = (int)(Math.sin(Math.toRadians(sa)) * radius);
				
				inside.get((int) i).drawDie(gg, this.x + width / 2 + x, this.y + height / 2 + y, 20);
			}
		}
		
		for (int x = MainFrame.WIDTH * 7 / 8, y = MainFrame.HEIGHT / 2 + 70, i = 0; i < dice.length; i++, y += 70)  
			if (outside.contains(dice[i]))
				dice[i].drawDie(gg, x, y, 50);
	}
}
