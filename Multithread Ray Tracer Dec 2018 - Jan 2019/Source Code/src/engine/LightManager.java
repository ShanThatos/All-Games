package engine;

import java.awt.Color;
import java.util.LinkedList;

import engine.geom.Point3D;
import engine.geom.SMath;
import engine.geom.Vec3;

public class LightManager {
	private static LinkedList<Light> lights;
	
	public static void init() {
		lights = new LinkedList<>();
		lights.add(new AmbientLight(Color.white));
		lights.add(new PointLight(new Point3D(-30, 0, 0), Color.white));
	}
	
	public static Color applyLights(Point3D source, Vec3 normal, Color c) {
		normal = normal.getUnitVector();
		Color ret = new Color(c.getRed(), c.getGreen(), c.getBlue());
		Color darkness = Color.black;
		for (Light l : lights) {
			int lightType = l.getLightType();
			if (lightType == Light.AMBIENT_LIGHT) { 
				darkness = shineLight(l.getColor(), .5, darkness);
			}
			else if (lightType == Light.POINT_LIGHT) {
				PointLight pl = (PointLight) l;
				Vec3 v1 = new Vec3(source, pl.getCenter());
				v1 = v1.getUnitVector();
				double intensity = SMath.dotProd(v1, normal);
				darkness = shineLight(l.getColor(), intensity, darkness);
			}
		}
		return combineColors(darkness, .5, ret, .5);
	}
	
	// Shines light onto c
	private static Color shineLight(Color light, double intensity, Color c) {
		int newRed = Math.max(Math.min((int) (c.getRed() + light.getRed() * intensity), 255), 0);
		int newGreen = Math.max(Math.min((int) (c.getGreen() + light.getGreen() * intensity), 255), 0);
		int newBlue = Math.max(Math.min((int) (c.getBlue() + light.getBlue() * intensity), 255), 0);
		return new Color(newRed, newGreen, newBlue);
	}
	// Combines colors
	private static Color combineColors(Color c1, double w1, Color c2, double w2) {
		double sum = w1 + w2;
		if (sum != 1) {
			w1 /= sum;
			w2 /= sum;
		}
		Color c3 = new Color(
				(int) (c1.getRed() * w1 + c2.getRed() * w2), 
				(int) (c1.getGreen() * w1 + c2.getGreen() * w2), 
				(int) (c1.getBlue() * w1 + c2.getBlue() * w2));
		return c3;
	}
}
