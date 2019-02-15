package com.sskcode.platformer.UI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class UIButton extends JButton {
	
	private static final long serialVersionUID = 1L;
	
	public int type;
	
	// Types
	public static final String[] types = {"Start", "Play", "Pause", "Resume", "Restart", "Settings"};
	public static BufferedImage[][] btns = new BufferedImage[types.length][3];
	
	public UIButton(String type) {
		int i = 0;
		for (String a : types) {
			if (a.equals(type))
				this.type = i;
			i++;
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
//		g.drawImage(btns[type][0], super.getX(), super.getY(), null);
		int width = 80;
		int height = 40;
		int imgNum = 0;
		if (getModel().isPressed())
			imgNum = 1;
		g.drawImage(btns[type][imgNum], super.getX(), super.getY(), super.getX() + width, super.getY() + height, 0, 0, btns[type][0].getWidth(), btns[type][0].getHeight(), null);
	}
	
	public static void readAllInfo() {
		BufferedImage[] imgs = new BufferedImage[3];
		try {
			imgs[0] = ImageIO.read(new File("resources/TitleScreen/0Buttons/menu.png"));
			imgs[1] = ImageIO.read(new File("resources/TitleScreen/0Buttons/menu-clicked.png"));
			imgs[2] = ImageIO.read(new File("resources/TitleScreen/0Buttons/menu-disabled.png"));
		} catch (IOException e) {}
		for (int y = 0, i= 0; y < 90 * types.length; y+=90, i++) {
			for (int j = 0; j < imgs.length; j++) {
				btns[i][j] = imgs[j].getSubimage(0, y, imgs[j].getWidth(), 58);
			}
		}
	}
}
