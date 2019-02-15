package create;

import java.io.IOException;

import javax.swing.JFrame;

public class Runner {
	public static void main(String[] args) throws IOException {
		read();
		JFrame frame = new JFrame("Creator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CreatePanel cp = new CreatePanel(new Manager());
		frame.setContentPane(cp);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
//		(new Thread(cp)).start();
	}
	
	private static void read() throws IOException {
		(new Tile()).read();
	}
}
