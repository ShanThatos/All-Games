package coltexpress.rules;

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
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class DirectionButton extends JButton{
	String direction;
	public DirectionButton(String d)
	{
		super(d);
		direction=d;
		setVisible(true);
		if(direction.equals("RIGHT"))
			setIcon(new ImageIcon("right.png"));
		else 
			setIcon(new ImageIcon("left.png"));
		repaint();
	}
	public int getDirection()
	{
		if(direction.equals("RIGHT"))
			return 1;
		else
			return -1;
	}
}