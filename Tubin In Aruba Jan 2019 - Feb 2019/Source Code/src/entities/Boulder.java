package entities;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import imaging.Animation;

public class Boulder extends DrawableEntity {
	
	private static Animation[] allBoulders;
	private static boolean initiated;
	private static final Rectangle2D.Double colBox = new Rectangle2D.Double(20, 20, 20, 30);
	
	private int boulderType;
	
	public Boulder(double x, double y, int boulderType) {
		super("Boulder", x, y, -100, 0);
		if (!initiated)
			init(getClass().getClassLoader());
		super.colBox = colBox;
	}
	
	@Override
	public void update(double dt) {
		moveUpdate(dt);
		entityDestroyed = entityDestroyed || x < -64;
		allBoulders[boulderType].update();
	}
	@Override
	public void draw(Graphics2D gr) {
		Graphics2D g = (Graphics2D) gr.create();
		g.translate(x, y);
		g.drawImage(allBoulders[boulderType].getFrame(), 0, 0, null);
		super.drawColBox(gr);
	}
	
	private static void init(ClassLoader cl) {
		try {
			initiated = true;
			String[][] allImgs = {{"boulder.png"}};
			allBoulders = new Animation[allImgs.length];
			for (int i = 0; i < allImgs.length; i++) {
				BufferedImage[] frames = new BufferedImage[allImgs[i].length];
				for (int j = 0; j < allImgs[i].length; j++) {
					frames[j] = ImageIO.read(cl.getResourceAsStream("textures/" + allImgs[i][j]));
				}
				allBoulders[i] = new Animation(frames);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
