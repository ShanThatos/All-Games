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
		
		JButton btnNewButton = new JButton("Play");
		btnNewButton.setBackground(new Color(173, 216, 230));
		btnNewButton.setFont(new Font("Segoe Print", Font.BOLD, 25));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MainFrame.changeState(MainState.GAME);
			}
		});
		btnNewButton.setBounds(290, 165, 219, 75);
		add(btnNewButton);
		
		JLabel lblGameTemplate = new JLabel("Game Template");
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
