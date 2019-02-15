package create;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Map implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	// Map Info
	private transient BufferedImage bg;
	private Point2D bgTopLeft;
	private Tile[][][] tiles;
	
	// Game Info
	private Point2D playerPos;
	
	public Map(BufferedImage bg, Point2D bgTopLeft, Point2D playerPos) {
		this.bg = bg;
		this.bgTopLeft = bgTopLeft;
		tiles = new Tile[bg.getHeight() / 64][bg.getWidth() / 64][2];
		this.playerPos = playerPos;
	}
	
	public void putTile(Tile t, int r, int c) {
		tiles[r][c][t.isInBackground() ? 0 : 1] = t;
	}
	
	public void setBackground(BufferedImage bg) {
		this.bg = bg;
	}
	public BufferedImage getBackground() {
		return bg;
	}
	public Point2D getBackgroundTopLeft() {
		return bgTopLeft;
	}
	
	public ArrayList<Tile> convToList() {
		ArrayList<Tile> ret = new ArrayList<>();
		for (int k = 0; k < 2; k++)
			for (int i = 0; i < tiles.length; i++)
				for (int j = 0; j < tiles[i].length; j++)
					if (tiles[i][j][k] != null)
						ret.add(tiles[i][j][k]);
		return ret;
	}
	
	public Tile[][][] getTiles() {
		return tiles;
	}
	
	public Point2D getPlayerPos() {
		return playerPos;
	}
	
	public void writeObjectToFile(String fileName) throws IOException {
		FileOutputStream file = new FileOutputStream(fileName); 
        ObjectOutputStream out = new ObjectOutputStream(file); 
        // Method for serialization of object 
        out.writeObject(this); 
        ImageIO.write(bg, "png", out);
        out.close(); 
        file.close();
	}
	public static Map readObjectFromFile(String fileName) throws ClassNotFoundException, IOException {
		FileInputStream file = new FileInputStream(fileName); 
        ObjectInputStream in = new ObjectInputStream(file); 
          
        // Method for deserialization of object  
        Map m = (Map)in.readObject();
        m.setBackground(ImageIO.read(in));
        in.close(); 
        file.close();
        return m;
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		Map m = new Map(new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB), new Point2D.Float(0, 0), new Point2D.Float(0, 0));
		m.writeObjectToFile("test.map");
		Map m2 = Map.readObjectFromFile("test.map");
	}
}
