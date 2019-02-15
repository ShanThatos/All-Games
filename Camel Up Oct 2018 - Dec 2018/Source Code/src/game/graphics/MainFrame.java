package game.graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import game.board.GameManager;
import game.input.GraphicsHandler;

public class MainFrame extends JFrame implements Runnable {
	
	// Global Info
	public static double time;
	
	// Game Settings
	public static final int WIDTH = 1600, HEIGHT = 900;
	
	// Current Content Pane
	public static final int MENU_PANEL = 0, GAME_PANEL = 1;
	private static int currentPanel = 0;
	
	private JPanel[] panels;
	
	// GameManager
	private GameManager gm;
	
	// Graphics Handler
	private GraphicsHandler input;
	
	public MainFrame(GameManager gm, GraphicsHandler input) {
		super("Camel Up");
		this.gm = gm;
		
		panels = new JPanel[2];
		panels[0] = new MenuPanel();
		panels[1] = new GamePanel(gm, input);
		
		setContentPane(panels[currentPanel]);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		this.input = input;
	}
	
	public void start() {
		(new Thread(this)).start();
	}
	
	public void update() {
		if (panels[currentPanel] != super.getContentPane()) {
			setContentPane(panels[currentPanel]);
			revalidate();
			pack();
			setLocationRelativeTo(null);
		}
	}
	
	public void run() {
		long start, elapsed, wait;
		
		while (gm.isGameRunning()) {
			repaint();
			start = System.nanoTime();
			update();
			elapsed = System.nanoTime() - start;
			wait = ((1000 / 60) - elapsed / 1000000);
			if (wait < 5)
				wait = 10;
			try { Thread.sleep(wait); } catch (Exception e) {}
			
			time = System.nanoTime() / 1000000000.0;
		}
	}
	
	public static void changePanel(int newPanel) {
		currentPanel = newPanel;
	}
}
