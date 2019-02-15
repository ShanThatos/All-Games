package coltexpress.player;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Animator {
	
	// Dimensions: Player, Action
	// Frames & Flips are inside Animation class
	public static Animation[][] allAnimations;
	public static String[] playerNames = {"Belle", "Cheyenne", "Django", "Doc", "Ghost", "Tuco"};
	public static String[] animNames = {"Idle", "Loot", "Punch", "Run", "Shoot", "Climb"};
	public static boolean flippedAnim[] = {false, false, true, true, true, false};
	public static int[] numEachAnim = {1, 4, 2, 9, 2, 4};
	
	// Animations for Marshall
//	public static Animation[] allMAnimations;
	public static String[] animMNames = {"Idle", "Run", "Shoot"};
	public static boolean flippedMAnim[] = {false, true, true};
	public static int[] numEachMAnim = {1, 9, 2};
	
	// Individual stuff
	public int playerAnimID;
	public boolean animating, flipped;
	public Point origPoint, nextPoint;
	public Point vector2;
	public int currentAnimation;
	public static final int PLAYER_WIDTH = 35, PLAYER_HEIGHT = 55;
	
	public Animator(int playerAnimID) {
		this.playerAnimID = playerAnimID;
	}
	
	public void drawPlayer(Graphics g, Point pos) {
		allAnimations[playerAnimID][0].drawAnimation(g, pos, false);
		origPoint = (Point)pos.clone();
		nextPoint = (Point)pos.clone();
		vector2 = new Point(0, 0);
	}
	
	public void drawPlayerAnimation(Graphics g) {
//		if (animating && currentAnimation == 3)
//			animating = origPoint.distance(nextPoint) < 10;
		if (animating && playerAnimID != 6) {
			if (currentAnimation == 4)
				animating = allAnimations[playerAnimID][currentAnimation].numTimesRun < 5;
			if (currentAnimation == 2)
				animating = allAnimations[playerAnimID][currentAnimation].numTimesRun < 5;
			if (currentAnimation == 1)
				animating = allAnimations[playerAnimID][currentAnimation].numTimesRun < 1;
		} else if (animating) {
			if (currentAnimation == 2)
				animating = allAnimations[playerAnimID][currentAnimation].numTimesRun < 5;
		}
		
		if (!animating) {
			allAnimations[playerAnimID][currentAnimation].numTimesRun = 0;
		}
		allAnimations[playerAnimID][currentAnimation].drawAnimation(g, origPoint, flipped);
		origPoint.x += vector2.x;
		origPoint.y += vector2.y;
		allAnimations[playerAnimID][currentAnimation].nextFrame();
	}
	
	// Static Methods for reading in all Animations!
	public static void readAllAnimations() throws IOException {
		
		allAnimations = new Animation[playerNames.length + 1][animNames.length];
		
		for (int player = 0; player < playerNames.length; player++) {
			for (int anim = 0; anim < animNames.length; anim++) {
				
				BufferedImage[][] frames = new BufferedImage[numEachAnim[anim]][flippedAnim[anim] ? 2 : 1];
				for (int fr = 1; fr <= numEachAnim[anim]; fr++) {
					frames[fr - 1][0] = ImageIO.read(new File("Files/Animations/" + playerNames[player] + "/" + 
							animNames[anim] + "/" + animNames[anim].toLowerCase() + fr + ".png"));
					if (flippedAnim[anim]) {
						frames[fr - 1][1] = flipImageHoriz(frames[fr - 1][0]);
					}
				}
				
				allAnimations[player][anim] = new Animation(frames);
			}
		}
		
		readAllMarshallAnimations();
	}
	
	private static void readAllMarshallAnimations() throws IOException {
		allAnimations[6] = new Animation[animMNames.length];
		for (int anim = 0; anim < animMNames.length; anim++) {
			BufferedImage[][] frames = new BufferedImage[numEachMAnim[anim]][flippedMAnim[anim] ? 2 : 1];
			
			for (int fr = 1; fr <= numEachMAnim[anim]; fr++) {
				frames[fr - 1][0] = ImageIO.read(new File("Files/Animations/Marshall/" + 
						animMNames[anim] + "/" + animMNames[anim].toLowerCase() + fr + ".png"));
				if (flippedMAnim[anim]) {
					frames[fr - 1][1] = flipImageHoriz(frames[fr - 1][0]);
				}
			}
			
			allAnimations[6][anim] = new Animation(frames); 
		}
	}
	
	private static BufferedImage flipImageHoriz(BufferedImage bi) {
		BufferedImage ret = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		for (int x = 0; x < bi.getWidth(); x++) {
			for (int y = 0; y < bi.getHeight(); y++) {
				ret.setRGB(x, y, bi.getRGB(bi.getWidth() - x - 1, y));
			}
		}
		return ret;
	}
}
