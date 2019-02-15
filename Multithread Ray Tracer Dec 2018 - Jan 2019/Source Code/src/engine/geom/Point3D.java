package engine.geom;

public class Point3D {
	private double x, y, z;
	public Point3D(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public double getZ() {
		return z;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public void setXYZ(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void translate(double dx, double dy, double dz) {
		x += dx;
		y += dy;
		z += dz;
	}
	
	public String toString() {
		return String.format("Point3D: (%.3f, %.3f, %.3f)", x, y, z); 
	}
	
	public Point3D clone() {
		return new Point3D(x, y, z);
	}
}
