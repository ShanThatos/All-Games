package menu;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import input.Input;
import main.MainFrame;
import main.MainState;

public class MenuPanel extends JPanel {
	
	public static final int WIDTH = 800, HEIGHT = 600;
	
	public MenuPanel() {
		createPanel();
	}
	
	private void createPanel() {
		setBackground(Color.GRAY);
		setLayout(null);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		
		JButton btnPlay = new JButton("Play");
		btnPlay.setBackground(new Color(173, 216, 230));
		btnPlay.setFont(new Font("Segoe Print", Font.BOLD, 25));
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame.changeState(MainState.GAME);
			}
		});
		btnPlay.setBounds(290, 165, 219, 75);
		add(btnPlay);
		
		JButton btnCreate = new JButton("Create");
		btnCreate.setFont(new Font("Segoe Print", Font.BOLD, 25));
		btnCreate.setBackground(new Color(244, 164, 96));
		btnCreate.setBounds(290, 269, 219, 75);
		btnCreate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.changeState(MainState.CREATE);
			}
		});
		add(btnCreate);
		
		JLabel lblGameTemplate = new JLabel("Game!");
		lblGameTemplate.setFont(new Font("Segoe UI Semilight", Font.BOLD, 29));
		lblGameTemplate.setHorizontalAlignment(SwingConstants.CENTER);
		lblGameTemplate.setBackground(Color.GRAY);
		lblGameTemplate.setBounds(242, 68, 315, 43);
		add(lblGameTemplate);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(248, 124, 304, 2);
		add(separator);
	}
}
