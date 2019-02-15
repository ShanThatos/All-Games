package game.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {
	
	private static final int WIDTH = 800, HEIGHT = 600;
	
	public MenuPanel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setBackground(Color.gray);
		setLayout(null);
		
		JButton play = new JButton("Play");
		play.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 30));
		int width = play.getFontMetrics(play.getFont()).stringWidth("Play") * 2 + 100;
		play.setHorizontalAlignment(JButton.CENTER);
		play.setForeground(Color.pink);
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.changePanel(MainFrame.GAME_PANEL);
			}
		});
		play.setBounds(WIDTH / 2 - width / 2, 100, width, 100);
		add(play);
	}
}
