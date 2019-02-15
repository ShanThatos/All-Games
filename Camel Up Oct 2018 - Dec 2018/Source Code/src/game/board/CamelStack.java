package game.board;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;

import game.tiles.DesertTile;

public class CamelStack {
	
	public static final int ON_TOP = -1, BOTTOM = 0;
	
	// 0'th position is the bottom of the stack
	private ArrayList<Camel> camels;
	
	// Drawing
	private int x, y, width, height, stNum;
	private Color c;
	
	public CamelStack() {
		camels = new ArrayList<>();
	}
	
	public ArrayList<Camel> getSubStack(int bottomCamel) {
		ArrayList<Camel> ret = new ArrayList<>();
		for (int i = bottomCamel; i < camels.size(); i++) 
			ret.add(camels.get(i));
		return ret;
	}
	
	public void putCamelsOnTop(ArrayList<Camel> onTop) {
		camels.addAll(onTop);
	}
	
	public void putCamelsBelow(ArrayList<Camel> below) {
		camels.addAll(0, below);
	}
	
	public void addCamel(int pos, Camel c) {
		if (pos == ON_TOP)
			camels.add(c);
		else if (pos == BOTTOM)
			camels.add(0, c);
		else
			camels.add(pos, c);
	}
	
	public void removeCamelsGoingUp(int pos) {
		while (camels.size() > pos)
			camels.remove(pos);
	}
	
	public int getCamelsPosition(Camel c) {
		return camels.indexOf(c);
	}
	
	public int getNumberOfCamels() {
		return camels.size();
	}
	
	public String toString() {
		return "Bottom " + camels.toString() + " Top";
	}
	
	public void setDrawingInfo(int x, int y, int w, int h, Color c, int stackNumber) {
		this.x = x;
		this.y = y;
		width = w;
		height = h;
		this.c = c;
		stNum = stackNumber;
	}
	
	public int[] getDrawingInfo() {
		return new int[]{x, y, width, height, stNum};
	}
	
	public void drawCamelStack(Graphics2D g, DesertTile dt) {
		g.setColor(c);	
		g.fillRect(x, y, width, height);
		
		g.setColor(Color.black);
		g.drawRect(x, y, width, height);
		g.setFont(new Font("Arial", Font.BOLD, 30));
		g.drawString(stNum + "", x + 5, y + g.getFontMetrics().getHeight());
		
		if (dt != null) 
			dt.drawDesertTile(g, x + width / 2, y + height / 2, (int)(width * .7), (int)(height * .7));
		
		for (int i = 0, cX = x + width / 2, cY = y + height - 25; i < camels.size(); i++, cY -= 25) {
			camels.get(i).drawCamel(g, cX, cY, 0, 50, stNum <= 7);
		}
	}
}
