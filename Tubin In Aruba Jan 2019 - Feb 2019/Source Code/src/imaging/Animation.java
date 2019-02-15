package imaging;

import java.awt.image.BufferedImage;

public class Animation {
	
	private BufferedImage[] frames;
	private int currentFrame;
	private long frameTime, lastFrameUpdate;
	
	public Animation(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		frameTime = 100000000;
		lastFrameUpdate = System.nanoTime();
	}
	
	public void setFrameTime(long frameTime) {
		this.frameTime = frameTime;
	}
	
	public void update() {
		long timeElapsed = System.nanoTime() - lastFrameUpdate;
		if (timeElapsed > frameTime) {
			currentFrame = (currentFrame + 1) % frames.length;
			lastFrameUpdate = System.nanoTime();
		}
	}
	
	public BufferedImage getFrame() {
		return frames[currentFrame];
	}
}
