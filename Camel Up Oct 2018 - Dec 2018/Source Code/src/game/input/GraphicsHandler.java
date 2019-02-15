package game.input;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

import game.board.CamelStack;
import game.board.GameBoard;
import game.board.GameManager;
import game.board.RoundEndDisplay;
import game.graphics.MainFrame;
import game.graphics.UI.CButton;
import game.player.Player;
import game.tiles.DesertTile;
import game.tiles.LegBetTile;
import game.tiles.LegBetTileStack;

public class GraphicsHandler implements InputHandler, MouseListener, MouseMotionListener {
	
	private static Point2D mouseLoc, mousePress;
	private boolean mousePressed;
	
	// Button Handler
	private ArrayList<CButton> btns;
	private ArrayList<Label> lbls;
	private CButton pressedBtn;
	
	// Player RaceBets areas
	
	public GraphicsHandler() {
		mouseLoc = new Point2D.Double(0, 0);
		mousePress = new Point2D.Double(0, 0);
		
		btns = new ArrayList<>();
		lbls = new ArrayList<>();
	}
	
	@Override
	public void displayPlayerStartTurn(GameManager gm) {
		int id = gm.getCurrentPlayer().getID();
		CButton playerConf = new CButton("Is Player " + (id + 1) + " Ready?", 0); 
		playerConf.setBackground(Color.red);
		playerConf.setShape(new Point2D.Double(GameBoard.btnAreaCX, GameBoard.btnAreaCY), CButton.createRoundedRectangularShape(GameBoard.btnAreaWidth - 30, 60, 20));
		btns.add(playerConf);
		
		while (pressedBtn == null || pressedBtn != playerConf) 
			try { Thread.sleep(10); } catch (Exception e) {}
		clearBtns();
	}
	@Override
	public int requestTypeOfTurn(GameManager gm) {
		int btnWidth = GameBoard.btnAreaWidth - 50, btnHeight = 60, btnBorderRadius = 20;
		Font f = new Font("Arial", Font.BOLD, 20);
		
		CButton[] options = new CButton[4];
		String[] text = {"Pick up a Leg Betting Tile", "Place/Move your Desert Tile", "Pick up a Pyramid Tile", "Bet on a Camel"};
		Color bg = Color.orange;
		for (int i = 0, xx = GameBoard.btnAreaCX, yy = GameBoard.btnAreaCY - 3 * GameBoard.btnAreaHeight / 10; i < options.length; i++, yy += GameBoard.btnAreaHeight / 5) {
			options[i] = new CButton(text[i], i + 1);
			options[i].setBackground(bg);
			options[i].setShape(new Point2D.Double(xx, yy), CButton.createRoundedRectangularShape(btnWidth, btnHeight, btnBorderRadius));
			options[i].setFont(f);
			btns.add(options[i]);
		}
		
		while (pressedBtn == null) 
			try { Thread.sleep(10); } catch (Exception e) {}
		int ret = pressedBtn.getUserData();
		clearBtns();
		
		return ret;
	}
	@Override
	public LegBetTile requestLegBettingTile(GameManager gm) {
		LegBetTileStack[] legBetStacks = gm.getGameBoard().getLegBetsStacks();
		
		int cardWidth = 100, cardHeight = 50;
		for (int i = 0, xx = MainFrame.WIDTH - cardWidth - 50, yy = MainFrame.HEIGHT / 2 + 50; i < legBetStacks.length; i++, yy += cardHeight + 20) {
			if (legBetStacks[i].getSize() == 0)
				continue;
			int btnX = xx + (legBetStacks[i].getSize() - 1) * 5;
			int btnY = yy - (legBetStacks[i].getSize() - 1) * 5;
			CButton bt = new CButton("", i);
			bt.setShape(new Point2D.Double(btnX + cardWidth / 2, btnY + cardHeight / 2), CButton.createRoundedRectangularShape(cardWidth, cardHeight, 5));
			bt.setClear(true);
			bt.setBackground(Color.white);
			btns.add(bt);
		}
		
		while (pressedBtn == null)
			try { Thread.sleep(10); } catch (Exception e) {}
		int ret = pressedBtn.getUserData();
		clearBtns();
		
		return legBetStacks[ret].seeTop();
	}
	@Override
	public Object[] requestDesertTilePlacement(GameManager gm) {
		TreeSet<Integer> possibleSpots = new TreeSet<>();
		for (int i = 0; i < 16; i++) 
			possibleSpots.add(i);
		
		CamelStack[] track = gm.getRace().getFullTrack();
		for (int i = 0; i < track.length; i++) 
			if (track[i].getNumberOfCamels() > 0) 
				possibleSpots.remove(i);
		
		ArrayList<DesertTile> dst = Player.getAllDesertTiles();
		for (int i = 0; i < dst.size(); i++) {
			DesertTile dt = dst.get(i);
			if (dt.getPlayerID() == gm.getCurrentPlayer().getID())
				continue;
			if (dt.isOnBoard()) {
				possibleSpots.remove(dt.getTileNumber() + 1);
				possibleSpots.remove(dt.getTileNumber());
				possibleSpots.remove(dt.getTileNumber() - 1);
			}
		}
		
		possibleSpots.remove(0);
		
		for (int spot : possibleSpots) {
			CButton bt = new CButton("", spot);
			int[] pos = track[spot].getDrawingInfo();
			bt.setShape(new Point2D.Double(pos[0] + pos[2] / 2, pos[1] + pos[3] / 2), 
					CButton.createRectangularShape(pos[2], pos[3]));
			bt.setClear(true);
			bt.setBackground(Color.white);
			btns.add(bt);
		}
		
		while (pressedBtn == null)
			try { Thread.sleep(10); } catch (Exception e) {}
		int ret1 = pressedBtn.getUserData();
		clearBtns();
		
		CButton bt = new CButton("Oasis Up (+1)", 1);
		bt.setShape(new Point2D.Double(GameBoard.btnAreaCX, GameBoard.btnAreaCY - 50), CButton.createRoundedRectangularShape(GameBoard.btnAreaWidth - 20, 40, 10));
		Color c = new Color(153, 77, 0);
		bt.setBackground(c.brighter().brighter());
		CButton bt2 = new CButton("Mirage Up (-1)", 2);
		bt2.setShape(new Point2D.Double(GameBoard.btnAreaCX, GameBoard.btnAreaCY + 50), CButton.createRoundedRectangularShape(GameBoard.btnAreaWidth - 20, 40, 10));
		bt2.setBackground(c.brighter().brighter());
		btns.add(bt);
		btns.add(bt2);
		
		while (pressedBtn == null)
			try { Thread.sleep(10); } catch (Exception e) {}
		int ret2 = pressedBtn.getUserData();
		clearBtns();
		
		Object[] ret = new Object[2];
		ret[0] = ret1;
		ret[1] = ret2 == 1;
		return ret;
	}
	@Override
	public Object[] requestRaceBetType(GameManager gm) {
		
		TreeMap<String, Object> info = gm.getGraphicsInfo();
		if (info.get("RaceBetLocations") == null)
			return null;
		
		Rectangle2D[][] locs = (Rectangle2D[][]) (info.get("RaceBetLocations"));
		
		if (locs[gm.getCurrentPlayer().getID()].length != gm.getCurrentPlayer().getRaceBets().size())
			return null;
		
		for (int i = 0; i < locs[gm.getCurrentPlayer().getID()].length; i++) {
			Rectangle2D loc = locs[gm.getCurrentPlayer().getID()][i];
			CButton bt = new CButton("", i);
			bt.setShape(new Point2D.Double(loc.getX() + loc.getWidth() / 2, loc.getY() + loc.getHeight() / 2), CButton.createRoundedRectangularShape((int)loc.getWidth(), (int)loc.getHeight(), 5));
			bt.setClear(true);
			bt.setBackground(Color.white);
			btns.add(bt);
		}
		
		while (pressedBtn == null)
			try { Thread.sleep(10); } catch (Exception e) {}
		int raceBetTile = pressedBtn.getUserData();
		clearBtns();
		
		int dy = 0;
		if (gm.getGameBoard().getWinnerBets().size() > 0)
			dy = (gm.getGameBoard().getWinnerBets().size() - 1) * 8 + 10;
		CButton bt1 = new CButton("", 0), bt2 = new CButton("", 1);
		bt1.setShape(new Point2D.Double(GameBoard.rbwtAreaCX, GameBoard.rbwtAreaCY + dy), CButton.createRoundedRectangularShape(GameBoard.rbtAreaWidth, GameBoard.rbtAreaHeight, 5));
		bt1.setClear(true);
		bt1.setBackground(Color.white);
		btns.add(bt1);
		
		dy = 0;
		if (gm.getGameBoard().getLoserBets().size() > 0) 
			dy = (gm.getGameBoard().getLoserBets().size() - 1) * 8 + 10;
		bt2.setShape(new Point2D.Double(GameBoard.rbltAreaCX, GameBoard.rbltAreaCY + dy), CButton.createRoundedRectangularShape(GameBoard.rbtAreaWidth, GameBoard.rbtAreaHeight, 5));
		bt2.setClear(true);
		bt2.setBackground(Color.white);
		btns.add(bt2);
		
		while (pressedBtn == null)
			try { Thread.sleep(10); } catch (Exception e) {}
		boolean placeInWinnerBets = pressedBtn.getUserData() == 0;  
		clearBtns();
		
		Object[] ret = new Object[2];
		ret[0] = gm.getCurrentPlayer().getRaceBets().get(raceBetTile);
		ret[1] = placeInWinnerBets;
		return ret;
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseLoc = e.getPoint();
		updateButtons(false, false);
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		mouseLoc = e.getPoint();
		updateButtons(false, false);
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	@Override
	public void mousePressed(MouseEvent e) {
		mousePress = e.getPoint();
		mousePressed = true;
		updateButtons(true, false);
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		mousePressed = false;
		updateButtons(false, true);
	}
	
	public void updateButtons(boolean mousePressed, boolean mouseReleased) {
		if (mousePressed) 
			for (int i = 0; i < btns.size(); i++) 
				if (btns.get(i).checkContains(mouseLoc)) {
					btns.get(i).setPressed(true);
				}
		if (mouseReleased) 
			for (int i = 0; i < btns.size(); i++) 
				if (btns.get(i).checkContains(mouseLoc) && btns.get(i).getPressed())
					pressedBtn = btns.get(i);
		if (!mousePressed && !mouseReleased) 
			for (int i = 0; i < btns.size(); i++) 
				if (!btns.get(i).checkContains(mouseLoc))
					btns.get(i).setPressed(false);
	}
	public void drawInputs(Graphics2D g) {
		if (RoundEndDisplay.isDisplaying()) 
			RoundEndDisplay.drawDisplay((Graphics2D) g.create());
		drawBtns(g);
		drawStrs(g);
	}
	private void drawBtns(Graphics2D g) {
		for (int i = 0; i < btns.size(); i++)
			btns.get(i).drawButton(g);
	}
	private void drawStrs(Graphics2D g) {
		for (int i = 0; i < lbls.size(); i++)
			lbls.get(i).drawLabel(g);
	}
	public void clearBtns() {
		btns.clear();
		pressedBtn = null;
	}
	
	public static Point2D getMouseLoc() {
		return mouseLoc;
	}

	@Override
	public void displayRoundEnd(GameManager gm) {
		RoundEndDisplay.createDisplayImage(gm.getPlayers(), gm.getRace(), gm.getGameBoard());
		CButton bt = new CButton("Continue", 0);
		bt.setShape(new Point2D.Double(MainFrame.WIDTH / 2, MainFrame.HEIGHT - 100),
				CButton.createRoundedRectangularShape(200, 80, 10));
		bt.setBackground(Color.red.darker());
		bt.setForeground(Color.white);
		btns.add(bt);
		
		while (pressedBtn == null) {
			try { Thread.sleep(10); } catch (Exception e) {}
		}
		clearBtns();
		
		RoundEndDisplay.stopDisplay();
	}
}

class Label {
	private String str;
	private int x, y;
	private Font f;
	
	public Label(String s, int x, int y) {
		str = s;
		this.x = x;
		this.y = y;
		f = new Font("Arial", Font.BOLD, 25);
	}
	
	public void setFont(Font f) {
		this.f = f;
	}
	
	public void drawLabel(Graphics2D gg) {
		Graphics2D g = (Graphics2D) gg.create();
		
		g.setFont(f);
		FontMetrics met = g.getFontMetrics();
		g.drawString(str, x - met.stringWidth(str) / 2, y - met.getHeight() / 2 + met.getAscent());
	}
}