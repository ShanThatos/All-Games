package coltexpress.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import coltexpress.game.GameFrame;

public class EndPanel extends JPanel implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	private BufferedImage player;
	private BufferedImage[] background;
	private String[] names = {"Belle", "Cheyenne", "Django", "Doc", "Ghost", "Tuco"};
	private int money;
	private Thread thread;
	
	public EndPanel(int animIDWinner, int money) {
		super();
		setPreferredSize(new Dimension(GameFrame.WIDTH, GameFrame.HEIGHT));
		setFocusable(true);
		requestFocus();
		setLayout(null);
		setVisible(true);
		try {
			readFiles(animIDWinner);
		} catch (Exception e) {System.out.println("HI");System.exit(0);}
		this.money = money;
		thread = new Thread(this);
		thread.start();
	}
	
	private void readFiles(int animIDWinner) throws IOException {
		player = ImageIO.read(new File("Files/Animations/" + names[animIDWinner] + "/" + names[animIDWinner].toLowerCase() + ".png"));
		background = new BufferedImage[4];
		for (char a = 'A', i = 0; a <= 'D' && i < 4; a++, i++) {
//			System.out.println("HEY");
			background[i] = ImageIO.read(new File("Files/Background/Sliding/" + ((char)a) + ".png"));
//			System.out.println(background[i]);
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 3; i >= 0; i--) {
			g.drawImage(background[i], 1, 1, 1000, 800, null);
		}
		g.drawImage(player, 200, 730 - player.getHeight(), null);
		g.setFont(new Font("Times New Roman", Font.BOLD + Font.ITALIC, 80));
		g.setColor(Color.black);
		g.drawString("The Real Bandit", 400, 500);
		String m = "$" + String.format("%.2f", (float)money);
		int dx = g.getFontMetrics().stringWidth(m);
		g.drawString(m, 650 - dx / 2, 600);
	}
	
	@Override
	public void run() {
		while (true) {
			repaint();
			try { Thread.sleep(50); } catch (Exception e) {}
		}
	}
}
