package engine;

import java.awt.Color;

import engine.geom.Point3D;

public class PointLight extends Light {
	
	private Point3D center;
	
	public PointLight(Point3D center, Color color) {
		super(Light.POINT_LIGHT, color);
		this.center = center;
	}
	
	public Point3D getCenter() {
		return center;
	}
}
