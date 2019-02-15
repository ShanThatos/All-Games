package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import game.Camera;
import game.GamePanel;

public class EngineManager {
	
	// Display
	private static BufferedImage scr;
	private static Graphics2D g;
	
	// Threads & Thread Data
	private static final int NUM_THREAD_ROWS = 3, NUM_THREAD_COLUMNS = 3;
	private static RayTracingThread[] raythreads;
	
	public static void init(Camera cam) {
		scr = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g = scr.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.transform(AffineTransform.getTranslateInstance(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2));
		g.transform(AffineTransform.getScaleInstance(1, -1));
		
		raythreads = new RayTracingThread[NUM_THREAD_ROWS * NUM_THREAD_COLUMNS];
		for (int kx = -GamePanel.WIDTH / 2, i = 0, n = 0; 
				i < NUM_THREAD_COLUMNS; 
				kx += GamePanel.WIDTH / NUM_THREAD_COLUMNS, i++) {
			for (int ky = -GamePanel.HEIGHT / 2, j = 0; 
					j < NUM_THREAD_ROWS; 
					ky += GamePanel.HEIGHT / NUM_THREAD_ROWS, j++, n++) {
				raythreads[n] = new RayTracingThread(kx, ky, 
						GamePanel.WIDTH / NUM_THREAD_COLUMNS, 
						GamePanel.HEIGHT / NUM_THREAD_ROWS);
				raythreads[n].setCamera(cam);
				Thread rt = new Thread(raythreads[n]);
				rt.setName("RayTracingThread" + n);
				rt.start();
			}
		}
	}
	
	public static void update() {
		boolean updatingSections = true;
		BufferedImage display;
		int[] segmentData;
		while (updatingSections) {
			updatingSections = false;
			for (int i = 0; i < raythreads.length; i++) {
				if (raythreads[i].isDoneWithImage()) {
					segmentData = raythreads[i].getSegmentData();
					display = raythreads[i].getDisplay();
					g.drawImage(display, segmentData[0], segmentData[1], segmentData[2], segmentData[3], Color.red, null);
				} else 
					updatingSections = true;
			}
		}
		for (int i = 0; i < raythreads.length; i++) {
			raythreads[i].resetDoneWithImage();
		}
	}
	
	public static BufferedImage getDisplay() {
		return scr;
	}
}
