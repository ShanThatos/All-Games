package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import imaging.Animation;
import imaging.ImageManipulator;

public class Coin extends DrawableEntity {
	
	private static String[] coinTypes = {"Gold", "Silver", "Copper"};
	private static int[] coinValue = {10, 5, 2};
	private static Animation[] coinAnims;
	private static boolean initiated;
	private static final Rectangle2D.Double colBox = new Rectangle2D.Double(-10, -16, 20, 20);
	
	private int coinType;
	
	public Coin(double x, double y, int coinType) {
		super("Coin", x, y, -100, 0);
		if (!initiated)
			init(getClass().getClassLoader());
		this.coinType = coinType;
		super.colBox = colBox;
	}
	
	@Override
	public void update(double dt){
		moveUpdate(dt);
		entityDestroyed = entityDestroyed || x < -32;
		coinAnims[coinType].update();
	}
	
	@Override
	public void draw(Graphics2D gr) {
		Graphics2D g = (Graphics2D) gr.create();
		BufferedImage k = coinAnims[coinType].getFrame();
		g.translate(x, y);
		g.drawImage(k, -k.getWidth() / 2, -k.getHeight() / 2, null);
		super.drawColBox(gr);
	}
	
	public int getCoinValue() {
		return coinValue[coinType];
	}
	
	private static void init(ClassLoader cl) {
		try {
			initiated = true;
			coinAnims = new Animation[3];
			for (int i = 0; i < 3; i++) {
				BufferedImage k = ImageIO.read(
						cl.getResourceAsStream(
								"textures/coin_" + coinTypes[i].toLowerCase() + ".png"));
				k = ImageManipulator.addTransparentBackground(k, Color.white);
				BufferedImage[] frames = new BufferedImage[8];
				for (int j = 0, x = 0; j < 8; j++, x+=k.getWidth() / 8) 
					frames[j] = k.getSubimage(x, 0, 32, 32);
				coinAnims[i] = new Animation(frames);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}
