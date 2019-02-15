package com.sskcode.platformer.entities;

import com.sskcode.platformer.physics.Collider;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Platform extends Collider{
	
	private BufferedImage tile;
	private int connX, connY;
//	private int picOffsetY = 5;
	
	public Platform(int X, int Y, int w, int h, boolean c) {
		super(X, Y, w, h, c);
		BufferedImage i = null;
		try {i = ImageIO.read(new File("resources/Levels/01/Tiles/GrassTile2.png"));} catch (IOException e) {}
		connX = 0;
		connY = 40;
		tile = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics temp = tile.getGraphics();
		for (int y = h / (i.getHeight() - connY) * (i.getHeight() - connY); y >= (i.getHeight() - connY); y-= (i.getHeight() - connY)) {
			for (int x = 0; x < tile.getWidth(); x+= (i.getWidth() - connX)) {
				temp.drawImage(i, x, y - connY, null);
			}
		}
		for (int x = 0; x < tile.getWidth(); x+= (i.getWidth() - connX)) {
			temp.drawImage(i, x, 0, null);
		}
		temp.dispose();
	}
	
	public  void draw(Graphics g, int leftX, int topY) {
		g.drawImage(tile, box.x - leftX, box.y - topY, null);
	}
}