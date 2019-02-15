package com.sskcode.external.imageprocessing;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import com.sskcode.external.imageprocessing.GifDecoder;
import com.sskcode.platformer.physics.Point;

public class AnimatedGIF {
	private BufferedImage[] frames; // the internal frames
	private GifDecoder d;
	private int numFrames = -1;
	public int currentFrame = -1;
	private int AnimSpeed = 5, speedCounter = 0;
	private boolean isReady = false;
	
	public boolean standStillEnd = false;

	public AnimatedGIF() {}
	
	public void read(BufferedImage i) {
		frames = new BufferedImage[1];
		frames[0] = i;
		numFrames = 1;
		currentFrame = 0;
		isReady = true;
	}
	
	public void flip(BufferedImage[] fr) {
		frames = new BufferedImage[fr.length];
		for (int i = 0; i < fr.length; i++) {
		    int width = fr[i].getWidth();
		    int height = fr[i].getHeight();
		    //BufferedImage for mirror image
		    frames[i] = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		    //create mirror image pixel by pixel
		    for(int y = 0; y < height; y++){
		      for(int rx = width - 1; rx >= 0; rx--){
		        //lx starts from the left side of the image
		        //rx starts from the right side of the image
		        //get source pixel value
		        int p = fr[i].getRGB(width - rx - 1, y);
		        //set mirror image pixel value - both left and right
		        frames[i].setRGB(rx, y, p);
		      }
		    }
		}
		numFrames = fr.length;
		currentFrame = 0;
		isReady = true;
	}
	
	public BufferedImage[] getImages() {
		return frames;
	}
	
	public void read(String animatedGIFFile) {
		d = new GifDecoder();
		d.read(getClass().getResourceAsStream(animatedGIFFile));
		numFrames = d.getFrameCount();
		currentFrame = 0;
		frames = new BufferedImage[numFrames];
		for (int i = 0; i < numFrames; i++) {
			int imgW = 60, imgH = 60;
//			Image tmp = (d.getFrame(i)).getScaledInstance(imgW, imgH, Image.SCALE_SMOOTH);
//			frames[i] = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_ARGB);
//
//		    Graphics2D g2d = frames[i].createGraphics();
//		    g2d.drawImage(tmp, 0, 0, null);
//		    g2d.dispose();
			
			frames[i] = d.getFrame(i);
		}
		isReady = true;
	}
	
	public void shrinkToSize(int imgW, int imgH) {
		for (int i = 0; i < numFrames; i++) {
			Image tmp = (frames[i]).getScaledInstance(imgW, imgH, Image.SCALE_SMOOTH);
			frames[i] = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_ARGB);

		    Graphics2D g2d = frames[i].createGraphics();
		    g2d.drawImage(tmp, 0, 0, null);
		    g2d.dispose();
		}
	}
	
	public void setAnimSpeed(int as) {
		AnimSpeed = as;
	}
	
	public void draw(Graphics g, Point position) {
		if (isReady) {
			int imageWidth = frames[currentFrame].getWidth();
			int imageHeight = frames[currentFrame].getHeight();
			int scale = 1;
			g.drawImage(frames[currentFrame], (int)position.x, (int)position.y, (int)position.x + imageWidth * scale, (int)position.y + imageHeight * scale, 0, 0,  imageWidth, imageHeight, null);
		}
	}
	
	public void nextFrame() {
		++speedCounter;
		if (speedCounter >= AnimSpeed) {
			++currentFrame;
			if (currentFrame >= numFrames && !standStillEnd) {
				currentFrame = 0;
			} else if (standStillEnd) {
				currentFrame = Math.min(currentFrame, numFrames - 1);
			}
			speedCounter = 0;
		}
	}
	public void resetFrame() {
		currentFrame = 0;
	}
}
