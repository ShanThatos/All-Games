package game.board;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.image.BufferedImage;
import java.util.Random;

public class PyramidDie {
	
	// Camel ID
	private int camelID;
	
	// Last roll
	private int lastRoll;
	
	// Random for all die
	private static Random rand;
	
	// Die faces
	private static BufferedImage[] faces;
	
	public PyramidDie(int camelID) {
		if (rand == null)
			init();
		this.camelID = camelID;
		lastRoll = -1;
	}
	
	public int rollDie() {
		lastRoll = rand.nextInt(3) + 1;
		return lastRoll;
	}
	
	public void reset() {
		lastRoll = -1;
	}
	
	public int getCamelID() {
		return camelID;
	}
	
	public void drawDie(Graphics2D gg, int x, int y, int size) {
		Graphics2D g = (Graphics2D) gg.create();
		Color c = Camel.getGColors()[camelID];
		g.setColor(c);
		g.translate(x, y);
		g.fillRect(-size / 2, -size / 2, size, size);
		if (lastRoll != -1)
			g.drawImage(faces[lastRoll - 1], -size / 2, -size / 2, size, size, null);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(2));
		g.drawRect(-size / 2, -size / 2, size, size);
		g.setStroke(new BasicStroke(1));
		
		Path2D path = new Path2D.Double();
		
		path.moveTo(0, 0);
		path.lineTo(-size / 4, 0);
		path.lineTo(-size / 4, size);
		path.lineTo(0, size);
		path.closePath();
		path.transform(AffineTransform.getShearInstance(0, -1));
		g.translate(-size / 2, -size / 2);
		g.setColor(c);
		g.fill(path);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(2));
		g.draw(path);
		g.setStroke(new BasicStroke(1));
		
		path.transform(AffineTransform.getTranslateInstance(0, size));
		Path2D path2 = new Path2D.Double();
		PathIterator pit = path.getPathIterator(AffineTransform.getScaleInstance(1, 1));
		while (!pit.isDone()) {
			double[] coords = new double[2];
			int type = pit.currentSegment(coords);
			switch (type) {
			case PathIterator.SEG_MOVETO:
				path2.moveTo(coords[1], coords[0]);
				break;
			case PathIterator.SEG_LINETO:
				path2.lineTo(coords[1], coords[0]);
				break;
			case PathIterator.SEG_CLOSE:
				path2.closePath();
				break;
			}
			pit.next();
		}
		g.translate(-size - size / 4, size + size / 4);
		g.setColor(c);
		g.fill(path2);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(2));
		g.draw(path2);
		g.setStroke(new BasicStroke(1));
	}
	
	private static void init() {
		rand = new Random();
		
		faces = new BufferedImage[6];
		int width = 100, height = 100, radius = 10;
		for (int i = 0; i < faces.length; i++) {
			faces[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = faces[i].createGraphics();
			g.setColor(Color.white);
			switch (i + 1) {
			case 5:
				g.fillOval(3 * width / 4 - radius, height / 4 - radius, radius * 2, radius * 2);
				g.fillOval(width / 4 - radius, 3 * height / 4 - radius, radius * 2, radius * 2);
			case 3:
				g.fillOval(width / 4 - radius, height / 4 - radius, radius * 2, radius * 2);
				g.fillOval(3 * width / 4 - radius, 3 * height / 4 - radius, radius * 2, radius * 2);
			case 1:
				g.fillOval(width / 2 - radius, height / 2 - radius, radius * 2, radius * 2);
				break;
			case 6:
				g.fillOval(3 * width / 4 - radius, height / 2 - radius, radius * 2, radius * 2);
				g.fillOval(width / 4 - radius, height / 2 - radius, radius * 2, radius * 2);
			case 4:
				g.fillOval(3 * width / 4 - radius, height / 4 - radius, radius * 2, radius * 2);
				g.fillOval(width / 4 - radius, 3 * height / 4 - radius, radius * 2, radius * 2);
			case 2:
				g.fillOval(width / 4 - radius, height / 4 - radius, radius * 2, radius * 2);
				g.fillOval(3 * width / 4 - radius, 3 * height / 4 - radius, radius * 2, radius * 2);
				break;
			}
			g.setColor(Color.black);
			g.setStroke(new BasicStroke(5));
			switch (i + 1) {
			case 5:
				g.drawOval(3 * width / 4 - radius, height / 4 - radius, radius * 2, radius * 2);
				g.drawOval(width / 4 - radius, 3 * height / 4 - radius, radius * 2, radius * 2);
			case 3:
				g.drawOval(width / 4 - radius, height / 4 - radius, radius * 2, radius * 2);
				g.drawOval(3 * width / 4 - radius, 3 * height / 4 - radius, radius * 2, radius * 2);
			case 1:
				g.drawOval(width / 2 - radius, height / 2 - radius, radius * 2, radius * 2);
				break;
			case 6:
				g.drawOval(3 * width / 4 - radius, height / 2 - radius, radius * 2, radius * 2);
				g.drawOval(width / 4 - radius, height / 2 - radius, radius * 2, radius * 2);
			case 4:
				g.drawOval(3 * width / 4 - radius, height / 4 - radius, radius * 2, radius * 2);
				g.drawOval(width / 4 - radius, 3 * height / 4 - radius, radius * 2, radius * 2);
			case 2:
				g.drawOval(width / 4 - radius, height / 4 - radius, radius * 2, radius * 2);
				g.drawOval(3 * width / 4 - radius, 3 * height / 4 - radius, radius * 2, radius * 2);
				break;
			}
		}
	}
}
