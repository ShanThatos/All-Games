package game.graphics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Drawer {
	
	
	// Graphics object, Color of camel, Center of camel, Rotation of camel (Rad), Size/Scale of camel 
	public static void drawCamel(Graphics2D g, Color c, Point2D center, double rot, int size) {
		AffineTransform old = g.getTransform();
		
		g.translate(center.getX(), center.getY());
		g.rotate(rot);
		g.scale(size, size);
		
		
		
		g.setTransform(old);
	}
	
	
	// For drawing your own shapes....
	private static boolean drawing;
	private static Point2D point;
	private static Path2D path;
	
	private static Path2D test;
	
	public static void main(String[] args) throws IOException {
		
		// File to draw
		BufferedImage camel = ImageIO.read(new File("oasis.jpg"));
		// CHANGE THIS^^^^^^^^^^
		
		
		JFrame fr = new JFrame("Drawing Board");
		JPanel cp = new JPanel() {
			@Override
			public void paintComponent(Graphics gr) {
				super.paintComponent(gr);
				Graphics2D g = (Graphics2D) ((Graphics2D) gr).create();
				// oof
				
				if (path != null) {
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
					g.setColor(Color.red);
					g.setStroke(new BasicStroke(10));
					g.draw(path);
				}
				if (test != null) {
					AffineTransform old = g.getTransform();
					g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
					g.setColor(Color.green);
					g.translate(camel.getWidth() / 2, camel.getHeight() / 2);
					g.scale(camel.getWidth(), camel.getHeight());
					// the part that shows up in the coordinates
					g.fill(test);
					g.setTransform(old);
				}
				
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
				g.drawImage(camel, 0, 0, null);
			}
		};
		cp.setPreferredSize(new Dimension(camel.getWidth(), camel.getHeight()));
		cp.setLayout(null);
		fr.setContentPane(cp);
		fr.pack();
		fr.setLocationRelativeTo(null);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setResizable(false);
		
		cp.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				drawing = true;
				point = e.getPoint();
			}
			@Override
			public void mouseMoved(MouseEvent e) {
				drawing = false;
				point = e.getPoint();
			}
		});
		
		JButton done = new JButton("Done");
		done.setBounds(camel.getWidth() - 100, 100, 80, 30);
		done.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				path.closePath();
				JTextArea text = new JTextArea();
				PathIterator pt = path.getPathIterator(AffineTransform.getScaleInstance(1, 1));
				
				test = new Path2D.Double();
				while (!pt.isDone()) {
					double[] coords = new double[6];
					int type = pt.currentSegment(coords);
					if (type != PathIterator.SEG_CLOSE) {
						coords[0] -= cp.getWidth() / 2;
						coords[1] -= cp.getHeight() / 2;
						
						coords[0] /= camel.getWidth();
						coords[1] /= camel.getHeight();
						
						text.append(String.format("%.2f %.2f%n", coords[0], coords[1]));
					}
					
					if (type == PathIterator.SEG_MOVETO)
						test.moveTo(coords[0], coords[1]);
					else if (type == PathIterator.SEG_LINETO)
						test.lineTo(coords[0], coords[1]);
					else if (type == PathIterator.SEG_CLOSE)
						test.closePath();
					
					pt.next();
				}
				
				JScrollPane scroll = new JScrollPane (text, 
						   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				scroll.setViewportView(text);
				scroll.setPreferredSize(new Dimension(200, 300));
				
				JFrame popup = new JFrame();
				popup.setContentPane(scroll);
				popup.pack();
				popup.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				popup.setLocationRelativeTo(fr);
				popup.setVisible(true);
			}
		});
		
		JButton delete = new JButton("Undo");
		delete.setBounds(camel.getWidth() - 100, 150, 80, 30);
		delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Path2D copy = new Path2D.Double();
				PathIterator pt = path.getPathIterator(AffineTransform.getScaleInstance(1, 1));
				int numSegs = 0;
				while (!pt.isDone()) {
					numSegs++;
					pt.next();
				}
				
				PathIterator pt2 = path.getPathIterator(AffineTransform.getScaleInstance(1, 1));
				while (!pt2.isDone() && numSegs > 5) {
					double[] coords = new double[2];
					int type = pt2.currentSegment(coords);
					if (type == PathIterator.SEG_MOVETO)
						copy.moveTo(coords[0], coords[1]);
					else if (type == PathIterator.SEG_LINETO)
						copy.lineTo(coords[0], coords[1]);
					numSegs--;
					pt2.next();
				}
				path = copy;
			}
		});
		
		
		cp.add(done);
		cp.add(delete);
		
		fr.setVisible(true);
		
		path = new Path2D.Double();
		// Stroud's death
		while (true) {
			if (drawing) {
				if (path.getCurrentPoint() == null) 
					path.moveTo(point.getX(), point.getY());
				else {
					if (path.getCurrentPoint().distance(point) > 10)
						path.lineTo(point.getX(), point.getY());
				}
			}
			// rip fps
			try { Thread.sleep(50); } catch (Exception e) {}
			fr.repaint();
		}
	}
}
