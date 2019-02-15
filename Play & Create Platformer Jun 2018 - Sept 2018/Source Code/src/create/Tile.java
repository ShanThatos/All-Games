package create;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.jbox2d.common.Vec2;

import game.jbox2d.utils.Convert;

public class Tile implements Serializable {
	
	// All Tiles
	private static String[] tileNames;
	// Recommended Mesh Boxes
	private static int[] recMeshBoxes;
	// All Tile Images
	private static BufferedImage[] tileImages;
	// All MeshBoxes
	private static Path2D[] meshBoxes;
	// All MeshBox Names
	private static String[] meshBoxNames;
	// All MeshBox Images
	private static ImageIcon[] meshBoxImages;
	
	// TileSheet Index to Starting Tile Index
	private static int[] tileImageIndices;
	// TileSheet Names
	private static String[] tileSheets;
	// Number of tiles per sheet
	private static int[] tilesPerSheet;
	
	// Positioning and Tile Info
	private int x, y, ti, mbi;
	
	// Background?
	private boolean isInBackground;
	
	public Tile() {}
	
	public Tile(int x, int y, int ti, int mbi) {
		this.x = x;
		this.y = y;
		this.ti = ti;
		this.mbi = mbi;
	}
	
	public void setTileInBackground(boolean back) {
		isInBackground = back;
	}
	public boolean isInBackground() {
		return isInBackground;
	}
	
	public void display(Graphics2D g, boolean showMesh) {
		g.drawImage(tileImages[ti], x, y, 64, 64, null);
		if (isInBackground) {
			Composite c = g.getComposite();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
			g.setColor(Color.black);
			g.fillRect(x, y, 64, 64);
			g.setComposite(c);
		}
		
		if (showMesh) {
			Composite c = g.getComposite();
			AffineTransform at = g.getTransform();
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .2f));
			g.setColor(Color.red);
			g.translate(x, y);
			g.scale(64, 64);
			g.fill(meshBoxes[mbi]);
			g.setComposite(c);
			g.setTransform(at);
		}
	}
	
	public void read() throws IOException {
		ArrayList<String> tileNames = new ArrayList<>();
		ArrayList<String> recMeshes = new ArrayList<>();
		ArrayList<BufferedImage> tileImages = new ArrayList<>();
		
		Scanner in1 = new Scanner(getClass().getClassLoader().getResourceAsStream("Tilesheet/_allSheets.dat"));
		int numTileSheets = in1.nextInt();
		tileSheets = new String[numTileSheets];
		tilesPerSheet = new int[numTileSheets];
		tileImageIndices = new int[numTileSheets];
		in1.nextLine();
		for (int tileSheet = 0; tileSheet < numTileSheets; tileSheet++) {
			String fileName = in1.nextLine();
			tileSheets[tileSheet] = fileName;
			tileImageIndices[tileSheet] = tileImages.size();
			BufferedImage ts = ImageIO.read(getClass().getClassLoader().getResourceAsStream("Tilesheet/" + fileName + ".png"));
			Scanner in2 = new Scanner(getClass().getClassLoader().getResourceAsStream("Tilesheet/" + fileName + ".dat"));
			int width = in2.nextInt(), height = in2.nextInt(), numTiles = in2.nextInt();
			tilesPerSheet[tileSheet] = numTiles;
			for (int i = 0; i < numTiles; i++) {
				int r = in2.nextInt(), c = in2.nextInt();
				String tileName = in2.next().trim();
				tileNames.add(tileName);
				String recMesh = in2.nextLine().trim();
				recMeshes.add(recMesh);
				
				BufferedImage tileImg = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = tileImg.createGraphics();
				g.drawImage(ts.getSubimage(c * width, r * height, width, height), 0, 0, 64, 64, null);
				tileImages.add(tileImg);
			}
			in2.close();
		}
		in1.close();
		
		Tile.tileNames = new String[tileNames.size()];
		Tile.tileImages = new BufferedImage[tileImages.size()];
		for (int i = 0; i < tileNames.size(); i++) 
			Tile.tileNames[i] = tileNames.get(i);
		for (int i = 0; i < tileImages.size(); i++)
			Tile.tileImages[i] = tileImages.get(i);
		
		in1 = new Scanner(getClass().getClassLoader().getResourceAsStream("Tilesheet/_meshBoxes.dat"));
		int numMeshBoxes = in1.nextInt();
		meshBoxes = new Path2D[numMeshBoxes];
		meshBoxNames = new String[numMeshBoxes];
		meshBoxImages = new ImageIcon[numMeshBoxes];
		for (int mb = 0; mb < numMeshBoxes; mb++) {
			int numPoints = in1.nextInt();
			Path2D path = new Path2D.Double();
			for (int i = 0; i < numPoints; i++) {
				if (i == 0)
					path.moveTo(in1.nextDouble(), in1.nextDouble());
				else
					path.lineTo(in1.nextDouble(), in1.nextDouble());
			}
			if (numPoints != 0)
				path.closePath();
			meshBoxes[mb] = path;
			meshBoxNames[mb] = in1.nextLine().trim();
			BufferedImage meshBox = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = meshBox.createGraphics();
			Path2D mesh = (Path2D) meshBoxes[mb].createTransformedShape(AffineTransform.getScaleInstance(63, 62));
			g.translate(0, 1);
			g.setColor(new Color(1.0f, 0, 0, .5f));
			g.fill(mesh);
			g.setColor(Color.black);
			g.draw(mesh);
			meshBoxImages[mb] = new ImageIcon(meshBox);
		}
		in1.close();
		
		Tile.recMeshBoxes = new int[recMeshes.size()];
		Arrays.fill(Tile.recMeshBoxes, 0);
		for (int i = 0; i < recMeshes.size(); i++) 
			for (int j = 0; j < meshBoxNames.length; j++) 
				if (recMeshes.get(i).equals(meshBoxNames[j])) 
					Tile.recMeshBoxes[i] = j;
	}
	
	public boolean checkSamePosition(Tile t) {
		return t.x == x && t.y == y && !(t.isInBackground ^ isInBackground);
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public Path2D getLocalMeshBox() {
		return meshBoxes[mbi];
	}
	public Object[] getWorldMeshBoxInfo() {
		Object[] ret = new Object[2];
		ret[0] = Convert.S2W(new Vec2(x + 32, y + 32));
		Path2D path = (Path2D) meshBoxes[mbi].createTransformedShape(AffineTransform.getTranslateInstance(-.5, -.5));
		path.transform(AffineTransform.getScaleInstance(1, -1));
		ret[1] = path;
		return ret;
	}
	public Path2D getGlobalMeshBox() {
		Path2D cl = (Path2D)meshBoxes[mbi].clone();
		cl.transform(AffineTransform.getScaleInstance(64, 64));
		cl.transform(AffineTransform.getTranslateInstance(x, y));
		return cl;
	}
	public String getMeshBoxName() {
		return meshBoxNames[mbi];
	}
	public Vec2[] getWorldVertices() {
		Vec2 v1 = Convert.S2W(new Vec2(x + 64, y));
		Vec2 v2 = Convert.S2W(new Vec2(x + 64, y + 64));
		Vec2 v3 = Convert.S2W(new Vec2(x + 128, y + 64));
		Vec2 v4 = Convert.S2W(new Vec2(x + 128, y));
		return new Vec2[]{v1, v2, v3, v4};
	}
	
	public static String[] getAllTileNames() {
		return tileNames;
	}
	public static int[] getAllRecMeshes() {
		return recMeshBoxes;
	}
	public static String[] getAllMeshBoxNames() {
		return meshBoxNames;
	}
	public static BufferedImage[] getAllTileImages() {
		return tileImages;
	}
	public static Path2D[] getAllMeshBoxes() {
		return meshBoxes;
	}
	public static ImageIcon[] getAllMeshBoxIcons() {
		return meshBoxImages;
	}	

	public static int[] getTileImageIndices() {
		return tileImageIndices;
	}
	public static String[] getTileSheets() {
		return tileSheets;
	}
	public static int[] getNumTilesPerSheet() {
		return tilesPerSheet;
	}
	public static ImageIcon[] getImagesForTileSheet(int ts) {
		ImageIcon[] ret = new ImageIcon[tilesPerSheet[ts]];
		for (int i = 0, j = tileImageIndices[ts]; i < ret.length; i++, j++)  
			ret[i] = new ImageIcon(tileImages[j]);
		return ret;
	}
	
	public static void displayTileInLoc(Graphics2D g, int x, int y, int ti) {
		g.drawImage(tileImages[ti], x, y, 64, 64, null);
	}
}
