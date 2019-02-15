package game.imaging;

import java.awt.image.BufferedImage;
import java.io.Serializable;

public class Animation implements Serializable {
	
	private transient BufferedImage[] frames;
	private int numFrames;
	
	public Animation() {
		
	}
}
