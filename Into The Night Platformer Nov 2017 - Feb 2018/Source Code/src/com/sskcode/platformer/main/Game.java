package com.sskcode.platformer.main;

import javax.swing.JFrame;

public class Game {
	public static void main(String[] args) {
		JFrame window = new JFrame("Into The Night");
		window.setContentPane(new MainPanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}



// Eventually....

// Ranged Enemies
// Menu, Title Screen
// Controlled Arrows

// Trigger Boxes for:
//  Sound
// 	Text Bubbles
// 	Environment Interaction
// 	Cutscenes