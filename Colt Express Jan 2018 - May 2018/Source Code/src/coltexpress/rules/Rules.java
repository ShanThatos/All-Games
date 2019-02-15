package coltexpress.rules;

import java.awt.AWTException;
import java.awt.Dimension;
import java.util.ConcurrentModificationException;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class Rules extends JFrame {
	
//	DirectionButton left;
//	DirectionButton right;
//	BufferedImage[] bf;
//	int curr;
	
	public Rules(String title) throws IOException, AWTException {
		super(title);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setSize(new Dimension(500, 500));
		setContentPane(new RulesPanel());
		setLocation(0,0);
		setLocation(100, 200);
		setVisible(true);
		setLayout(null);
		requestFocus();
//		left = new DirectionButton("LEFT");
//		left.setBounds(0, 400, 60, 40);
//		left.addActionListener(this);
//		left.setActionCommand("LEFT");
//		right = new DirectionButton("RIGHT");
//		right.setBounds(440, 400, 60, 40);
//		right.addActionListener(this);
//		right.setActionCommand("RIGHT");
//		add(left);
//		add(right);
		
//		curr = 0;
//		bf = new BufferedImage[numImages];
//		readImages();
//		draw();
		
//		Robot robot = new Robot();
//		robot.mouseMove(10, 430);
//		try {Thread.sleep(50);}catch(Exception e) {}
//		robot.mouseMove(450, 430);
	}
	
//	public void readImages() throws IOException {
//		for(int i = 0; i<bf.length ; i++) {
//			bf[i] = ImageIO.read(new File("Files/UI/Instructions/image"+i+".png"));
//		}
//	}
//	
//	public void draw() {
//		Graphics g = getGraphics();
//		g.drawImage(bf[curr], 0, 0, 500, 400, null);
//	}
//	
//	public void actionPerformed(ActionEvent e) {
//		if (e.getActionCommand().equals("RIGHT")) {
////			System.out.println("R");
//			moveRight();
//		} else {
////			System.out.println("L");
//			moveLeft();
//		}
//	}
//	
//	public void moveRight() {
//		curr = (curr+1)%bf.length;
//		draw();
//	}
//
//	public void moveLeft() {
//		curr = (curr-1 + bf.length)%bf.length;
//		draw();
//	}
}
