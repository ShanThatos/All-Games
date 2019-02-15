package coltexpress.rules;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RulesPanel extends JPanel implements Runnable {
	
	private static final long serialVersionUID = 1L;
	
	
	private JButton left;
	private JButton right;
	private JLabel pageNumber;
	
	private static final int numImages = 9;
	private static BufferedImage[] rules;
	private static int current = 0;
	
	public RulesPanel() {
		super();
		setPreferredSize(new Dimension(500, 500));
		setVisible(true);
		setLayout(null);
		setBackground(Color.black);
		left = new JButton();
		right = new JButton();
		left.setBounds(0, 225, 70, 50);
		right.setBounds(425, 225, 70, 50);
		left.setIcon(new ImageIcon("Files/UI/Buttons/left.gif"));
		right.setIcon(new ImageIcon("Files/UI/Buttons/right.gif"));
		addActionListeners();
		add(left);
		add(right);
		pageNumber = new JLabel("1");
		pageNumber.setBounds(20, 20, 100, 50);
		pageNumber.setFont(new Font("Arial", Font.BOLD + Font.ITALIC, 30));
		pageNumber.setForeground(Color.black);
		add(pageNumber);
	}
	
	private void addActionListeners() {
		left.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				current--;
				if (current < 0) 
					current = numImages - 1;
				pageNumber.setText(current + 1 + "");
				repaint();
			}
		});
		right.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				current++;
				if (current >= numImages)
					current = 0;
				pageNumber.setText(current + 1 + "");
				repaint();
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(rules[current], 0, 0, 500, 470, 0, 0, rules[current].getWidth(), rules[current].getHeight(), null);
		left.repaint();
		right.repaint();
	}
	
	@Override
	public void run() {
		
		while (true) {
			repaint();
			
			try { Thread.sleep(50); } catch (Exception e) {}
		}
		
	}
	
	public static void readAllRules() throws IOException {
		rules = new BufferedImage[numImages];
		for (int i = 0; i < numImages; i++) {
			rules[i] = ImageIO.read(new File("Files/UI/Instructions/image" + i + ".PNG"));
		}
	}
}
