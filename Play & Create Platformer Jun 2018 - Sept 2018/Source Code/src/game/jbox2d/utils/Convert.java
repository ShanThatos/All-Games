package game.jbox2d.utils;

import org.jbox2d.common.Vec2;

import game.GamePanel;

public class Convert {
	private static final int pixelPerMeter = 64;
	
	public static Vec2 W2S(Vec2 v) {
		Vec2 ret = v.clone();
		ret.mulLocal(pixelPerMeter);
		ret.y = GamePanel.HEIGHT - ret.y;
		return ret;
	}
	public static float W2S(float d) {
		return d * pixelPerMeter;
	}
	
	public static Vec2 S2W(Vec2 v) {
		Vec2 ret = v.clone();
		ret.y = GamePanel.HEIGHT - ret.y;
		ret.mulLocal(1f / pixelPerMeter);
		return ret;
	}
	public static float S2W(float d) {
		return d / pixelPerMeter;
	}
}
