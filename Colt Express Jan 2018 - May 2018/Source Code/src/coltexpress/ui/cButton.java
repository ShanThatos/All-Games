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

public class cButton extends JButton {

	// Random Stuff
	private static final long serialVersionUID = 1L;
	
	// All Buttons
	private static BufferedImage[][] allButtons;
	private static String[] buttonNames;
	// Index
	private int buttonID;
	
	// Dimensions
	public Rectangle dim;
	
	// UI
	public boolean pressed;
	
	
	// Stuff for the train Buttons
	public int trainPointingTo;
	// Stuff for lootbuttons
	public int lootType;
	
	// Stuff for playermoves
	public boolean isBulletCard;
	
	public cButton(int x, int y, String id) {
		super();
		for (int i = 0; i < buttonNames.length; i++) {
			if (buttonNames[i].contains(id))
				buttonID = i;
		}
		setBounds(x, y, allButtons[buttonID][0].getWidth(), allButtons[buttonID][0].getHeight());
		dim = new Rectangle(x, y, allButtons[buttonID][0].getWidth(), allButtons[buttonID][0].getHeight());
	}
	
	public void setDimensions(int w, int h) {
		dim.setBounds(dim.x, dim.y, w, h);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(allButtons[buttonID][pressed?1:0], dim.x + (pressed?3:0), dim.y + (pressed?3:0), dim.x + (int)dim.getWidth(), dim.y + (int)dim.getHeight(), 0, 0, allButtons[buttonID][pressed?1:0].getWidth(), allButtons[buttonID][pressed?1:0].getHeight(), null);
	}
	
	public boolean contains(Point p) {
		return dim.contains(p);
	}
	
	public static void readAllButtons() throws IOException {
		Scanner in = new Scanner(new File("Files/UI/Buttons/allButtonNames.dat"));
		int numButtons = in.nextInt();
		in.nextLine();
		allButtons = new BufferedImage[numButtons][2];
		buttonNames = new String[numButtons];
		
		for (int i = 0; i < numButtons; i++) {
			buttonNames[i] = in.nextLine();
			String pressed = in.nextLine();
			allButtons[i][0] = ImageIO.read(new File("Files/UI/Buttons/" + buttonNames[i]));
			allButtons[i][1] = ImageIO.read(new File("Files/UI/Buttons/" + pressed));
		}
		in.close();
	}
}
