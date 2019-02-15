package coltexpress.game;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Background {
	public BufferedImage[][] elements;
	public int[] bgX;
	public int[] bgYOffset = {0, 100, 0, 100, 180};
	public int[] inc = {0, 3, 5, 8, 15};
	
	public static boolean inATunnel;
	public int tunnelX;
	
	public int timer;
	
	public Background() throws IOException {
		elements = new BufferedImage[2][];
		elements[0] = new BufferedImage[5];
		bgX = new int[5];
		elements[0][0] = ImageIO.read(new File("Files/Background/Sliding/D.png"));
		elements[0][1] = ImageIO.read(new File("Files/Background/Sliding/C.png"));
		elements[0][2] = ImageIO.read(new File("Files/Background/Sliding/B.png"));
		elements[0][3] = ImageIO.read(new File("Files/Background/Sliding/B.png"));
		elements[0][4] = ImageIO.read(new File("Files/Background/Sliding/A.png"));
		elements[1] = new BufferedImage[1];
		elements[1][0] = ImageIO.read(new File("Files/Background/Sliding/Tunnel.jpg"));
		timer = 1000;
	}
	
	
	public void drawBackground(Graphics g) {
		
		if (!inATunnel) {
			int i = 0;
			for (BufferedImage bg : elements[0]) {
				g.drawImage(bg, bgX[i] - GameFrame.WIDTH, -bgYOffset[i], bgX[i], GameFrame.HEIGHT - bgYOffset[i], 0, 0, bg.getWidth(), bg.getHeight(), null);
				g.drawImage(bg, bgX[i], -bgYOffset[i], bgX[i] + GameFrame.WIDTH, GameFrame.HEIGHT - bgYOffset[i], 0, 0, bg.getWidth(), bg.getHeight(), null);
				i++;
			}
			
			for (int j = 0; j < bgX.length; j++) {
				bgX[j] += inc[j];
				if (bgX[j] >= GameFrame.WIDTH)
					bgX[j] = 0;
			}
		} else {
			g.drawImage(elements[1][0], tunnelX - GameFrame.WIDTH, 0, tunnelX, GameFrame.HEIGHT, 0, 0, elements[1][0].getWidth(), elements[1][0].getHeight(), null);
			g.drawImage(elements[1][0], tunnelX, 0, tunnelX + GameFrame.WIDTH, GameFrame.HEIGHT, 0, 0, elements[1][0].getWidth(), elements[1][0].getHeight(), null);
			tunnelX+=15;
			if (tunnelX >= GameFrame.WIDTH)
				tunnelX = 0;
		}
		
		if (!inATunnel) {
			if (--timer <= 0) {
				if (Math.random() < .3) {
					inATunnel = true;
					timer = (int)(Math.random() * 1000 + 500);
				}
			}
		} else if (--timer <= 0) {
			inATunnel = false;
			timer = (int)(Math.random() * 1000 + 1000);
		}
	}
}
