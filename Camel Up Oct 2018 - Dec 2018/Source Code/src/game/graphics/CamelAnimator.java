package game.graphics;

public class CamelAnimator {
	private boolean animate;
	private int x, y;
	private int vx, vy;
	private int stopX, stopY;
	
	public void startAnimation(int x, int y, int vx, int vy, int stopX, int stopY) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
		this.stopX = stopX;
		this.stopY = stopY;
		animate = true;
	}
	
	public void endAnimation() {
		animate = false;
	}
	
	public boolean checkEndAnimation() {
		if (Math.abs(stopX - (x + vx)) > Math.abs(stopX - x))
			return true;
		if (Math.abs(stopY - (y + vy)) > Math.abs(stopY - y))
			return true;
		return false;
	}
	
	public boolean isAnimating() {
		return animate;
	}
	
	public void step() {
		x += vx;
		y += vy;
	}
	
	public int[] getRealPosition() {
		return new int[]{x, y};
	}
	
	public int[] getPosition() {
		int dx = 0;
		if (vx == 0)
			dx = (int) (30 * Math.sin(MainFrame.time * 5));
		int dy = 0;
		if (vy == 0)
			dy = (int) (30 * Math.sin(MainFrame.time * 5));
		return new int[]{x + dx, y + dy};
	}
}
