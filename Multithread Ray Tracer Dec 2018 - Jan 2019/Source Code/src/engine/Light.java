package engine;

import java.awt.Color;

public class Light {
	public static final int AMBIENT_LIGHT = 0, POINT_LIGHT = 1, SPOT_LIGHT = 2;
	
	private int lightType;
	protected Color color;
	
	public Light(int lightType, Color color) {
		this.lightType = lightType;
		this.color = color;
	}
	
	public int getLightType() {
		return lightType;
	}
	
	public Color getColor() {
		return color;
	}
}
