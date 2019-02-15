package game.tiles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;

public class PyramidTile {
	public static void drawPyramidTile(Graphics2D gg, int x, int y, int width, int height) {
		Color c = new Color(153, 77, 0);
		Color c2 = c;
		Graphics2D g = (Graphics2D) gg.create();
		
		g.setColor(c2);
		g.fillRoundRect(x, y, width, height, 5, 5);
		g.setColor(Color.black);
		g.setStroke(new BasicStroke(3));
		g.drawRoundRect(x, y, width, height, 5, 5);
		g.setStroke(new BasicStroke(1));
		
		g.setColor(c2.brighter().brighter());
		Path2D path = new Path2D.Double();
		path.moveTo(x + width / 2, y + height / 3);
		path.lineTo(x + width / 5, y + 2 * height / 3);
		path.lineTo(x + 3 * width / 5, y + 4 * height / 5);
		path.closePath();
		g.fill(path);
		g.setColor(Color.black);
		g.draw(path);
		
		g.setColor(c2.brighter());
		Path2D path2 = new Path2D.Double();
		path2.moveTo(x + width / 2, y + height / 3);
		path2.lineTo(x + 3 * width / 5, y + 4 * height / 5);
		path2.lineTo(x + 4 * width / 5, y + 2 * height / 3);
		path2.closePath();
		g.fill(path2);
		g.setColor(Color.black);
		g.draw(path2);
	}
}
