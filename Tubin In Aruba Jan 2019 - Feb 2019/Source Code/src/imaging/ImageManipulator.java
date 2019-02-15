package imaging;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageManipulator {
	public static BufferedImage addTransparentBackground(BufferedImage img, Color bg) {
		BufferedImage k = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < img.getWidth(); x++) 
			for (int y = 0; y < img.getHeight(); y++) 
				if (img.getRGB(x, y) != bg.getRGB()) 
					k.setRGB(x, y, img.getRGB(x, y));
		return k;
	}
	
	public static int[] getCenteredTextCoords(Graphics2D g, String text, int width, int height) {
		int[] ret = new int[2];
		FontMetrics met = g.getFontMetrics();
		ret[0] = (width - met.stringWidth(text)) / 2;
		ret[1] = met.getAscent() + (height - (met.getAscent() + met.getDescent())) / 2;
		return ret;
	}
}
