package coltexpress.ui;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JButton;

public class CharacterButton extends JButton {

	// Random Stuff
	private static final long serialVersionUID = 1L;
	
	// All Buttons
	private static BufferedImage[][] allButtons;
	// Index
	public int animButtonID;
	
	// Dimensions
	public Rectangle dim;
	
	// UI
	public boolean pressed;
	
	
	
	public CharacterButton(int x, int y, int id) {
		super();
		animButtonID = id;
		setBounds(x, y, allButtons[animButtonID][0].getWidth(), allButtons[animButtonID][0].getHeight());
		dim = new Rectangle(x, y, allButtons[animButtonID][0].getWidth(), allButtons[animButtonID][0].getHeight());
	}
	
	public void setDimensions(int w, int h) {
		dim.setBounds(dim.x, dim.y, w, h);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(allButtons[animButtonID][pressed?1:0], dim.x + (pressed?3:0), dim.y + (pressed?3:0), dim.x + (int)dim.getWidth(), dim.y + (int)dim.getHeight(), 0, 0, allButtons[animButtonID][pressed?1:0].getWidth(), allButtons[animButtonID][pressed?1:0].getHeight(), null);
	}
	
	public boolean contains(Point p) {
		return dim.contains(p);
	}
	
	public static void readAllCharacterButtons() throws IOException {
		String[] playerNames = {"Belle", "Cheyenne", "Django", "Doc", "Ghost", "Tuco"};
		allButtons = new BufferedImage[playerNames.length][2];
		for (int i = 0; i < playerNames.length; i++) {
			allButtons[i][0] = ImageIO.read(new File("Files/Animations/" + playerNames[i] + "/Button/" + playerNames[i] + "Button1.png"));
			allButtons[i][1] = ImageIO.read(new File("Files/Animations/" + playerNames[i] + "/Button/" + playerNames[i] + "Button2.png"));
		}
	}
}
