package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import game.GamePanel;

public class Background extends DrawableEntity {
	
	private BufferedImage bg;
	
	public Background() {
		super("Background", 0, 0, 5, 0);
		try {
			createBackground();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	private void createBackground() throws IOException {
		bg = new BufferedImage(GamePanel.WIDTH + 128, GamePanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bg.createGraphics();
		BufferedImage grass1, grass2, water;
		grass1 = ImageIO.read(getClass().getClassLoader().getResource("textures/grass1.png"));
		grass2 = ImageIO.read(getClass().getClassLoader().getResource("textures/grass2.png"));
		Random r = new Random(1);
		for (int y = -64, i = 0, scale = 2; y < bg.getHeight(); y += grass1.getHeight() / scale / 2, i++) {
			int x = grass1.getWidth() / 2 / scale;
			if (i % 2 == 1)
				x += grass1.getWidth() / 2 / scale;
			
			for (x -= 64; x < bg.getWidth(); x += grass1.getWidth() / scale / 2) 
				g.drawImage(r.nextInt(5) == 0 ? grass2 : grass1, x, y, grass1.getWidth() / scale, grass1.getHeight() / scale, null);
		}
		
		water = ImageIO.read(getClass().getClassLoader().getResourceAsStream("textures/water.png"));
		for (int x = -64; x <= bg.getWidth(); x-=16) 
			for (int yi = 0; yi < gm.yLanes.length; yi++, x+=water.getWidth() / 4) 
				g.drawImage(water, x, gm.yLanes[yi] - water.getHeight() / 2, water.getWidth(), water.getHeight(), null);
	}
	
	@Override
	public void update(double dt) {
		moveUpdate(dt);
		if (x >= 0)
			x -= 128;
	}
	
	@Override
	public void draw(Graphics2D gr) {
		Graphics2D g = (Graphics2D) gr.create();
		g.translate(x, y);
		g.drawImage(bg, 0, 0, Color.black, null);
	}
}