package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import entities.Player;
import imaging.Animation;
import main.MainFrame;
import main.MainState;

public class MenuPanel extends JPanel {

	public static final int WIDTH = 800, HEIGHT = 600;
	
	private BufferedImage background;
	private Animation animMcgee;
	private int bgx;
	
	public MenuPanel() {
		createPanel();
	}
	
	private void createPanel() {
		setBackground(Color.GRAY);
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		JButton btnNewButton = new JButton("Play");
		btnNewButton.setBackground(new Color(173, 216, 230));
		btnNewButton.setFont(new Font("Segoe Print", Font.BOLD, 25));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.changeState(MainState.GAME);
			}
		});
		btnNewButton.setBounds(530, 165, 219, 40);
		add(btnNewButton);
		
		JButton btnContols = new JButton("Controls");
		btnContols.setBackground(new Color(173, 216, 230));
		btnContols.setFont(new Font("Segoe Print", Font.BOLD, 25));
		btnContols.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Up & Down Keys", "Controls", JOptionPane.PLAIN_MESSAGE);
			}
		});
		btnContols.setBounds(530, 210, 219, 40);
		add(btnContols);
		
		JLabel lblGameTemplate = new JLabel("Tubin In Aruba");
		lblGameTemplate.setFont(new Font("Segoe UI Semilight", Font.BOLD, 40));
		lblGameTemplate.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameTemplate.setBackground(Color.black);
		lblGameTemplate.setBounds(480, 68, 315, 43);
		add(lblGameTemplate);
		
		JLabel hScore = new JLabel(Player.getHighScore() + "");
		hScore.setFont(new Font("Segoe UI Semilight", Font.BOLD, 30));
		hScore.setHorizontalAlignment(SwingConstants.CENTER);
		hScore.setBackground(Color.black);
		hScore.setBounds(500, 400, 315, 43);
		add(hScore);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(480, 124, 304, 2);
		add(separator);
		
		try { createBackground(); } catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	private void createBackground() throws IOException {
		background = new BufferedImage(WIDTH * 2, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		BufferedImage water = ImageIO.read(getClass().getClassLoader().getResourceAsStream("textures/water.png"));
		Graphics2D g = background.createGraphics();
		for (int x = 0; x < background.getWidth(); x += water.getWidth())
			for (int y = 0; y < background.getHeight(); y += water.getHeight())
				g.drawImage(water, x, y, null);
		BufferedImage[] k1 = {
				ImageIO.read(getClass().getClassLoader().getResourceAsStream("textures/mcgee.png")), 
				ImageIO.read(getClass().getClassLoader().getResourceAsStream("textures/mcgee2.png"))
				};
		animMcgee = new Animation(k1);
		animMcgee.setFrameTime(1000000000);
		g.dispose();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, bgx++, 0, null);
		if (bgx >= 0)
			bgx -= WIDTH;
		animMcgee.update();
		g.drawImage(animMcgee.getFrame(), 0, (int) (20 * Math.sin(System.currentTimeMillis() / 1000.0)), WIDTH - 200, WIDTH - 200, null);
	}
}
