package game.level;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;

import create.Map;
import create.Tile;
import game.entities.EntityType;
import game.entities.UserData;
import game.jbox2d.utils.BodyCreator;
import game.jbox2d.utils.Convert;

public class Level {
	
	// Static Objects Background
	private BufferedImage bg;
	private Vec2 backgroundTopLeft;
	
	// Player starting Position
	private Vec2 playerStartPos;
	
	// World
	private GameWorld world;
	private ArrayList<Body> boxes = new ArrayList<>();
	
	public Level(Map m) {
		createWorld(m);
	}
	private void createWorld(Map m) {
		bg = m.getBackground();
		Point2D bgTl = m.getBackgroundTopLeft();
		backgroundTopLeft = new Vec2((float) bgTl.getX(), (float) bgTl.getY());
		Point2D pos = m.getPlayerPos();
		playerStartPos = new Vec2((float) pos.getX(), (float) pos.getY()); 
		
		world = new GameWorld();
		Tile[][][] tiles = m.getTiles();
		boolean[][] used = new boolean[tiles.length][tiles[0].length];
		for (int r = 0; r < tiles.length; r++) {
			for (int c = 0; c < tiles[r].length; c++) {
				if (tiles[r][c][1] == null)
					continue;
				if (tiles[r][c][1].getMeshBoxName().equals("None"))
					continue;
				if (used[r][c])
					continue;
				
				if (tiles[r][c][1].getMeshBoxName().equals("Full")) {
					int x = tiles[r][c][1].getX();
					int y = tiles[r][c][1].getY();
					int width = 64, height = 64;
					
					int cR = r, cC = c + 1;
					while (cC < tiles[0].length && tiles[cR][cC][1] != null && !used[cR][cC] && tiles[cR][cC][1].getMeshBoxName().equals("Full")) {
						used[cR][cC] = true;
						width += 64;
						cC++;
					}
					cR = r + 1; 
					cC = c;
					while (cR < tiles.length && tiles[cR][cC][1] != null && !used[cR][cC] && tiles[cR][cC][1].getMeshBoxName().equals("Full")) {
						boolean validRow = true;
						for (int cc = c; cc <= cC; cc++) 
							if (tiles[cR][cc] == null || used[cR][cc] || !tiles[cR][cc][1].getMeshBoxName().equals("Full")) 
								validRow = false;
						if (!validRow)
							break;
						for (int cc = c; cc < c + width / 64; cc++) 
							used[cR][cc] = true;
						height += 64;
						cR++;
					}
					
					Path2D shape = new Path2D.Float();
					shape.moveTo(-width / 2, -height / 2);
					shape.lineTo(-width / 2, height / 2);
					shape.lineTo(width / 2, height / 2);
					shape.lineTo(width / 2, -height / 2);
					shape.closePath();
					shape = (Path2D) shape.createTransformedShape(AffineTransform.getScaleInstance(1.0 / 64, 1.0 / 64));
					Vec2 shapeCenter = Convert.S2W(new Vec2(x + width / 2, y + height / 2));
					Body body = BodyCreator.createBody( 
							BodyCreator.createBodyDef(shapeCenter, BodyType.KINEMATIC), 
							BodyCreator.createFixtureDef(
									BodyCreator.createPolygonShape(shape), 
									1.0f, 0.3f, 0.2f));
					boxes.add(body);
					
					if (r - 1 >= 0) 
						if (tiles[r - 1][c][1] == null || !tiles[r - 1][c][1].getMeshBoxName().equals("None")) 
							body.setUserData(new UserData(body, EntityType.GROUND));
				} else {
					Object[] tileInfo = tiles[r][c][1].getWorldMeshBoxInfo();
					Vec2 tileCenter = (Vec2) tileInfo[0];
					Path2D shape = (Path2D) tileInfo[1];
					Body body = BodyCreator.createBody( 
							BodyCreator.createBodyDef(tileCenter, BodyType.KINEMATIC), 
							BodyCreator.createFixtureDef(
									BodyCreator.createPolygonShape(shape), 
									1.0f, 0.3f, 0.2f));
					used[r][c] = true;
					boxes.add(body);
					
					if (r - 1 >= 0) 
						if (tiles[r - 1][c][1] == null || !tiles[r - 1][c][1].getMeshBoxName().equals("None")) 
							body.setUserData(new UserData(body, EntityType.GROUND));
				}
			}
		}
	}
	
	public void update() {
		world.update();
	}
	
	public BufferedImage getBackground() {
		return bg;
	}
	public Vec2 getBackgroundTopLeft() {
		return backgroundTopLeft;
	}
	public Vec2 getPlayerStartPos() {
		return Convert.S2W(playerStartPos);
	}
}
