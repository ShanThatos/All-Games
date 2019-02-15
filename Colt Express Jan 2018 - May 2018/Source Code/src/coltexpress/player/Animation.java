package coltexpress.player;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Animation {
	
	// All Frames for action & flipped for DIM2
	public BufferedImage[][] frames;
	public int numFrames, currentFrame, animSpeed = 5, currentTime = 0;
	public int numTimesRun = 0;
	
	public Animation(BufferedImage[][] frames) {
		this.frames = frames;
		numFrames = frames.length;
		currentFrame = 0;
	}
	
	
	public void drawAnimation(Graphics g, Point pos, boolean flipped) {
		BufferedImage bi = frames[currentFrame][flipped ? 1 : 0];
		try {
			g.drawImage(bi, pos.x - Animator.PLAYER_WIDTH / 2, pos.y - Animator.PLAYER_HEIGHT / 2, pos.x + Animator.PLAYER_WIDTH / 2, pos.y + Animator.PLAYER_HEIGHT / 2, 0, 0, bi.getWidth(), bi.getHeight(), null);
		} catch (Exception e) {}
	}
	
	
	
	public void nextFrame() {
		currentTime++;
		if (currentTime == animSpeed) {
			currentFrame++;
			if (currentFrame == numFrames) {
				numTimesRun++;
				currentFrame = 0;
			}
			currentTime = 0;
		}
	}
	
	public void resetFrame() {
		currentFrame = 0;
		currentTime = 0;
	}
}
