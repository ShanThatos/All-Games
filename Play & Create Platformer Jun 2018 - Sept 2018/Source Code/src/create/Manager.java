package create;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class Manager implements MouseMotionListener, MouseListener, MouseWheelListener, KeyListener {
	
	private Camera cam;
	private ArrayList<Tile> tiles;
	
	// Inputs
	private Point mousePos, mousePress, mouseRel;
	private Point2D mouseWorldPos, mouseWorldPress, mouseWorldRel;
	private boolean mousePressed, mouseJustReleased;
	private boolean[] keys;
	
	// General Options Inputs
	private int tileSheet, tileNumber, meshNumber, currentPage;
	private boolean meshDisplay, tileInBackground, deleteTiles;
	
	// Player Position
	private Point2D playerPos;
	
 	public Manager() {
		tiles = new ArrayList<>();
		cam = new Camera();
		
		mousePos = new Point(0, 0);
		mousePress = new Point(0, 0);
		mouseRel = new Point(0, 0);
		mouseWorldPos = new Point2D.Float();
		mouseWorldPress = new Point2D.Float();
		mouseWorldRel = new Point2D.Float();
		keys = new boolean[256];
		
		playerPos = new Point2D.Float(0, 0);
	}
 	
 	public void update() {
 		cam.update(keys);
 		updateTiles();
 		mouseWorldPos = cam.convScreenToWorld(mousePos.x, mousePos.y);
 	}
 	
 	private void updateTiles() {
 		if (currentPage != 0)
 			return;
 		if (mouseJustReleased) {
 			if (deleteTiles)
 				deleteTiles();
 			else
 				addTiles();
 			mouseJustReleased = false;
 		}
 	}
 	private void deleteTiles() {
 		Point2D p = cam.convToTileCoord(mouseWorldPress.getX(), mouseWorldPress.getY());
 		Point2D p2 = cam.convToTileCoord(mouseWorldRel.getX(), mouseWorldRel.getY());
 		for (int i = tiles.size() - 1; i >= 0; i--) {
 			int x = tiles.get(i).getX();
 			int y = tiles.get(i).getY();
 			if (x >= p.getX() && y >= p.getY() && x <= p2.getX() && y <= p2.getY()) 
 				tiles.remove(i);
 		}
 	}
 	private void addTiles() {
 		Point2D p = cam.convToTileCoord(mouseWorldPress.getX(), mouseWorldPress.getY());
 		Point2D p2 = cam.convToTileCoord(mouseWorldRel.getX(), mouseWorldRel.getY());
 		for (int xx = (int)p.getX(); xx <= p2.getX(); xx += 64) 
 			for (int yy = (int)p.getY(); yy <= p2.getY(); yy += 64) {
 				Tile t = new Tile(xx, yy, tileNumber, meshNumber);
 				t.setTileInBackground(tileInBackground);
 				if (tileInBackground)
 					tiles.add(0, t);
 				else 
 					tiles.add(t);
 			}
 		removeDuplicateTiles();
 	}
	private void removeDuplicateTiles() {
		for (int i = tiles.size() - 1; i >= 0; i--) 
			for (int j = i - 1; j >= 0; j--) 
				if (tiles.get(i).checkSamePosition(tiles.get(j))) { 
					if (tiles.get(i).isInBackground())
						tiles.remove(i);
					else
						tiles.remove(j);
					i--;
				}
	}
 	
	public void display(Graphics g) {
		cam.startDisplay();
		pageDisplays(g);
		cam.endDisplay(g);
	}
	private void pageDisplays(Graphics g) {
		switch (currentPage) {
		case 0:
			cam.displayTiles(tiles, meshDisplay);
			cam.displayMouseHoverTiles(tileNumber, mouseWorldPress, mouseWorldPos, mousePressed, deleteTiles);
			cam.displayPlayerPos(playerPos, 0.5f);
			break;
		case 1:
			cam.displayTiles(tiles, false);
			cam.displayPlayerPos(playerPos, 1.0f);
			break;
		}
	}
	
	
	public void displaySingleTile(Graphics g) {
		int x = 176 / 2 - 32;
		int y = 176 / 2 - 32;
		BufferedImage tile = Tile.getAllTileImages()[tileNumber];
		g.drawImage(tile, x, y, 64, 64, null);
		
		if (meshDisplay) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.translate(x, y);
			Path2D mesh = (Path2D) Tile.getAllMeshBoxes()[meshNumber].clone();
			mesh.transform(AffineTransform.getScaleInstance(64, 64));
			g2d.setColor(Color.black);
			g2d.draw(mesh);
			g2d.setColor(new Color(1.0f, 0f, 0f, .2f));
			g2d.fill(mesh);
		}
	}
	
	// Input Handling - Mouse
	@Override
	public void mouseDragged(MouseEvent e) {
		mousePos = e.getPoint();
		mouseWorldPos = cam.convScreenToWorld(mousePos.x, mousePos.y);
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		mousePos = e.getPoint();
		mouseWorldPos = cam.convScreenToWorld(mousePos.x, mousePos.y);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		e.getComponent().requestFocus();
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		mousePressed = true;
		mousePress = e.getPoint();
		mouseWorldPress = cam.convScreenToWorld(mousePress.x, mousePress.y);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
		mouseRel = e.getPoint();
		mouseWorldRel = cam.convScreenToWorld(mouseRel.x, mouseRel.y);
		mouseJustReleased = true;
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double move = Math.min(.5, Math.max(-.5, e.getPreciseWheelRotation()));
		cam.zoomScale(1 + move);
	}
	// Input Handling - Keyboard
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}
	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	
	public void setTileSheet(int ts) {
		tileSheet = ts;
	}
	public void setTileNumber(int tn) {
		tileNumber = tn;
	}
	public void setMeshNumber(int mn) {
		meshNumber = mn;
	}
	public void setMeshDisplay(boolean md) {
		meshDisplay = md;
	}
	public void setTileInBackground(boolean tib) {
		tileInBackground = tib;
	}
	public void setDeleteTiles(boolean dt) {
		deleteTiles = dt;
	}
	public void setCurrentPage(int cp) {
		currentPage = cp;
	}
	public void setCameraCenterToPlayer() {
		cam.setCenter(playerPos);
	}
	public String getTilingInfo() {
		String tileInfo = "";
		tileInfo += String.format("%-20s%10s<br/>", "Tile Name:", Tile.getAllTileNames()[tileNumber]);
		tileInfo += String.format("%-20s%10s<br/>", "Mesh Name:", Tile.getAllMeshBoxNames()[meshNumber]);
		tileInfo += String.format("%-20s%10s<br/>", "Background Tiling:", (tileInBackground ? "ON" : "OFF"));
		return tileInfo;
	}
	
	public void save() throws IOException {
	    JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());	
	    chooser.setAcceptAllFileFilterUsed(false);
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("*.map", "map");
		chooser.addChoosableFileFilter(filter);
	    int retrival = chooser.showSaveDialog(null);
		
		int xMin = Integer.MAX_VALUE, xMax = Integer.MIN_VALUE, yMin = xMin, yMax = xMax;
		for (int i = 0; i < tiles.size(); i++) {
			xMin = Math.min(tiles.get(i).getX(), xMin);
			xMax = Math.max(tiles.get(i).getX() + 64, xMax);
			yMin = Math.min(tiles.get(i).getY(), yMin);
			yMax = Math.max(tiles.get(i).getY() + 64, yMax);
		}
		
		BufferedImage bg = new BufferedImage(xMax - xMin, yMax - yMin, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bg.createGraphics();
		g.translate(-xMin, -yMin);
		for (int i = 0; i < tiles.size(); i++)
			tiles.get(i).display(g, false);
		
//		Point2D newPlayerPos = new Point2D.Float((float) (playerPos.getX() - xMin), (float) (playerPos.getY() - yMin));
		
		Map m = new Map(bg, new Point2D.Float(xMin, yMin), playerPos);
		for (int i = 0; i < tiles.size(); i++) 
			m.putTile(tiles.get(i), (tiles.get(i).getY() - yMin)/ 64, (tiles.get(i).getX() - xMin) / 64);
		
		if (retrival == JFileChooser.APPROVE_OPTION) 
	        m.writeObjectToFile(chooser.getSelectedFile().toString().replaceAll(".map", "") + ".map");
	}
	public void read() throws IOException, ClassNotFoundException {
		JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
	    chooser.setDialogTitle("Choose a Map");
	    chooser.setAcceptAllFileFilterUsed(false);
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("*.map", "map");
		chooser.addChoosableFileFilter(filter);
	    int retrival = chooser.showOpenDialog(null);
	    if (retrival != JFileChooser.APPROVE_OPTION)
	    	return;
	    tiles = Map.readObjectFromFile(chooser.getSelectedFile().toString().replaceAll(".map", "") + ".map").convToList();
	}
}
