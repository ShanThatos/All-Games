package game.board;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.graphics.CamelAnimator;
import game.graphics.MainFrame;
import game.player.Player;
import game.tiles.DesertTile;

public class Race {
	
	// Track
	private CamelStack[] track;
	
	// Camels
	private Camel[] camels;
	
	// Race Done
	private boolean raceDone;
	
	public Race() {
		track = new CamelStack[16];
		for (int i = 0; i < 16; i++) 
			track[i] = new CamelStack();
		camels = new Camel[5];
		for (int i = 0; i < 5; i++) {
			camels[i] = new Camel();
			track[0].addCamel(CamelStack.ON_TOP, camels[i]);
		}
		
		if (GameManager.usingGraphics)
			setDrawingData();
	}
	
	public Camel[] getAllCamels() {
		return camels;
	}
	public Camel getCamelByID(int id) {
		for (int i = 0; i < camels.length; i++)
			if (camels[i].getID() == id)
				return camels[i];
		return null;
	}
	
	public CamelStack[] getFullTrack() {
		return track;
	}
	
	// Returns camel's new position
	public int moveCamelForward(Camel c, int steps) {
		int currentStack = findStackWithCamel(c);
		int newStack = currentStack + steps;
		ArrayList<Camel> camels = track[currentStack].getSubStack(track[currentStack].getCamelsPosition(c));
		track[currentStack].removeCamelsGoingUp(track[currentStack].getCamelsPosition(c));
		if (newStack >= 16) {
			raceDone = true;
			newStack %= 16;
		}
		
		track[newStack].putCamelsOnTop(camels);
		
		createAnimationSequence(camels, currentStack, newStack);
		
		return newStack;
	}
	
	public int moveCamelBackward(Camel c, int steps) {
		int currentStack = findStackWithCamel(c);
		int newStack = currentStack - steps;
		
		ArrayList<Camel> camels = track[currentStack].getSubStack(track[currentStack].getCamelsPosition(c));
		track[currentStack].removeCamelsGoingUp(track[currentStack].getCamelsPosition(c));
		
		track[newStack].putCamelsBelow(camels);
		return newStack;
	}
	
	private void createAnimationSequence(ArrayList<Camel> camels, int st, int end) {
		final int speed = 1;
		if (st < 3)
			singleDirectionAnimation(camels, st, end, 0, speed);
		singleDirectionAnimation(camels, st, end, -speed, 0);
		singleDirectionAnimation(camels, st, end, 0, -speed);
		singleDirectionAnimation(camels, st, end, speed, 0);
		singleDirectionAnimation(camels, st, end, 0, speed);
	}
	private void singleDirectionAnimation(ArrayList<Camel> camels, int st, int end, int dx, int dy) {
		
		for (int i = 0; i < camels.size(); i++) {
			CamelAnimator canim = camels.get(i).getAnimator();
			int[] drawingData = track[end].getDrawingInfo();
			canim.startAnimation(camels.get(i).getLastPosition()[0], camels.get(i).getLastPosition()[1], dx, dy, drawingData[0] + drawingData[2] / 2, drawingData[1] + drawingData[3] / 2);
		}
		
		boolean animating = true;
		while (animating) {
			for (int i = 0; i < camels.size(); i++) {
				if (camels.get(i).getAnimator().checkEndAnimation()) {
					animating = false;
				}
				camels.get(i).getAnimator().step();
			}
			try { Thread.sleep(10); } catch (Exception e) {}
		}
		for (int i = 0; i < camels.size(); i++) {
			camels.get(i).saveLastPosition();
			camels.get(i).getAnimator().endAnimation();
		}
	}
	
	private int findStackWithCamel(Camel c) {
		int ret = -1;
		for (int i = 0; i < track.length; i++) {
			int position = track[i].getCamelsPosition(c);
			if (position >= 0)
				ret = i;
		}
		return ret;
	}
	
	public ArrayList<Camel> getCamelsInOrder() {
		ArrayList<Camel> ret = new ArrayList<>();
		for (int i = 0; i < track.length; i++) 
			ret.addAll(track[i].getSubStack(0));
		return ret;
	}
	
	public void displayRace() {
		for (int i = 0; i < track.length; i++)
			System.out.println("Spot " + (i + 1) + ": " + track[i]);
		System.out.println();
		ArrayList<Camel> order = getCamelsInOrder();
		for (int i = order.size() - 1; i >= 0; i--)
			System.out.print(order.get(i) + (i == 0 ? "" : ", "));
		System.out.println("\n");
	}
	
	public boolean isRaceDone() {
		return raceDone;
	}
	
	
	private void setDrawingData() {
		int x = 50, y = 50, w = MainFrame.HEIGHT - 100, h = w;
		int dx = w / 5, dy = dx;
		int sX = x + dx * 4, sY = y + dy * 2;
		int[] xDir = {0, -1, 0, 1, 0};
		int[] yDir = {1, 0, -1, 0, 1};
		int[] numTimes = {2, 4, 4, 4, 2};
		int k = 0;
		
		for (int i = 0, cX = sX, cY = sY; i < 16; i++, cX += xDir[k] * dx, cY += yDir[k] * dy, numTimes[k]--) {
			if (numTimes[k] == 0)
				k++;
			track[i].setDrawingInfo(cX, cY, dx, dy, new Color(255, 195, 77), i + 1);
		}
	}
	public void drawRace(Graphics2D g, Player[] players) {
		for (int i = 0; i < track.length; i++) {
			DesertTile dt = null;
			for (int j = 0; j < players.length; j++) 
				if (players[j].getDesertTile().getTileNumber() == i)
					dt = players[j].getDesertTile();
			track[i].drawCamelStack(g, dt);
		}
	}
}
