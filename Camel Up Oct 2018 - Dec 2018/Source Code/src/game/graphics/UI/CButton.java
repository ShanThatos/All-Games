package game.graphics.UI;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

import game.graphics.MainFrame;

public class CButton {
	
	// Text
	private String words;
	// Font
	private Font font;
	// Color
	private Color background;
	private Color foreground;
	// Position
	private Point2D center;
	private Shape shape;
	
	// Extra Animations
	private boolean pressed;
	private boolean clear;
	
	private int userData;
	
	public CButton(String words, int userData) {
		this.words = words;
		this.userData = userData;
		font = new Font("Lucida Sans Unicode", Font.BOLD, 30);
		
		background = Color.black;
		foreground = Color.black;
	}
	
	public void setShape(Point2D center, Shape shape) {
		this.center = center;
		this.shape = shape;
	}
	
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setBackground(Color c) {
		background = c;
	}
	public void setForeground(Color c) {
		foreground = c;
	}
	
	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}
	public boolean getPressed() {
		return pressed;
	}
	
	public int getUserData() {
		return userData;
	}
	
	public boolean checkContains(Point2D p) {
		Point2D p2 = (Point2D) p.clone();
		p2.setLocation(p2.getX() - center.getX(), p2.getY() - center.getY());
		return shape.contains(p2);
	}
	
	public void drawButton(Graphics2D gg) {
		Graphics2D g = (Graphics2D) gg.create();
		if (!clear) {
			AffineTransform old = g.getTransform();
			
			g.translate(center.getX() - 5, center.getY() + 5);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
			g.setColor(Color.black);
			g.fill(shape);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
			
			if (!pressed) 
				g.translate(5, -5);
			
			
			Color c2 = background.darker().darker().darker().darker();
			GradientPaint gp1 = new GradientPaint(0, 0, background, 0, (int)(shape.getBounds2D().getHeight() / 1.5), c2, true);
			if (pressed) {
				gp1 = new GradientPaint(0, 0, background.darker().darker(), 0, (int)(shape.getBounds2D().getHeight() / 1.5), background, true);
			}
			g.setPaint(gp1);
			g.fill(shape);
			
			g.setColor(Color.black);
			g.setStroke(new BasicStroke(3));
			g.draw(shape);
			g.setStroke(new BasicStroke(1));
			
			g.setFont(font);
			int width = g.getFontMetrics().stringWidth(words);
			int height = g.getFontMetrics().getHeight();
			g.setColor(foreground);
			g.drawString(words, -width / 2, (int) (-height / 2 + g.getFontMetrics().getAscent()));
			
		} else {
			g.translate(center.getX(), center.getY());
		}
		if (!pressed) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float)(Math.abs(Math.sin(MainFrame.time * 2)))));
			g.setColor(background);
			g.setStroke(new BasicStroke(3));
			g.draw(shape);
			g.setStroke(new BasicStroke(1));
		}
	}
	
	public static Shape createRectangularShape(int width, int height) {
		Path2D path = new Path2D.Double();
		path.moveTo(-width / 2, -height / 2);
		path.lineTo(width / 2, -height / 2);
		path.lineTo(width / 2, height / 2);
		path.lineTo(-width / 2, height / 2);
		path.closePath();
		return path;
	}
	
	public static Shape createRoundedRectangularShape(int width, int height, int borderRadius) {
		Path2D path = new Path2D.Double();
		Arc2D arc = new Arc2D.Double();
		path.moveTo(-width / 2 + borderRadius, -height / 2);
		arc.setArcByCenter(width / 2 - borderRadius, -height / 2 + borderRadius, borderRadius, 90, -90, Arc2D.OPEN);
		path.append((Arc2D)arc.clone(), true);
		arc.setArcByCenter(width / 2 - borderRadius, height / 2 - borderRadius, borderRadius, 0, -90, Arc2D.OPEN);
		path.append((Arc2D)arc.clone(), true);
		arc.setArcByCenter(-width / 2 + borderRadius, height / 2 - borderRadius, borderRadius, 270, -90, Arc2D.OPEN);
		path.append((Arc2D)arc.clone(), true);
		arc.setArcByCenter(-width / 2 + borderRadius, -height / 2 + borderRadius, borderRadius, 180, -90, Arc2D.OPEN);
		path.append((Arc2D)arc.clone(), true);
		path.closePath();
		return path;
	}
	
	public void setClear(boolean clear) {
		this.clear = clear;
	}
	public boolean isClear() {
		return clear;
	}
}
