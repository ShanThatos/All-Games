package com.sskcode.platformer.main;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JPanel;

public class MainPanel extends JPanel implements Runnable{
	
	private static final long serialVersionUID = 1L;
	
    // Thread
	private Thread thread;
	private boolean running;
	
	// Dimensions
	public static final int WIDTH = 800;//640;
	public static final int HEIGHT = 600;//480;
	
	// Panels
	public MenuPanel mp;
	public GamePanel gp;
	
	public MainPanel() {
		super();
		setBackground(Color.black);
		
		mp = new MenuPanel(this);
		add(mp);
		gp = null;
//		add(new GamePanel());
	}
	
	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		running = true;
	}

	
	public void init() {
		mp.play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				remove(0);
				gp = new GamePanel();
				add(gp);
				setBackground(Color.black);
				revalidate();
//				repaint();
			}
		});
	}
	
	@Override
	public void run() {
		init();
		while (running) {
			
		}
	}
}
