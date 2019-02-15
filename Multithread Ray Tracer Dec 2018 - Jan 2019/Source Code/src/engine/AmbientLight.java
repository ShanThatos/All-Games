package engine;

import java.awt.Color;

public class AmbientLight extends Light {
	public AmbientLight(Color color) {
		super(Light.AMBIENT_LIGHT, color);
	}
}
