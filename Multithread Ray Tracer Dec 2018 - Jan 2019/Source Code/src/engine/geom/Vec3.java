package engine.geom;

public class Vec3 {
	private double dx, dy, dz;
	
	public Vec3(double dx, double dy, double dz) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}
	public Vec3(Point3D p1, Point3D p2) {
		dx = p2.getX() - p1.getX();
		dy = p2.getY() - p1.getY();
		dz = p2.getZ() - p1.getZ();
	}
	public double getDx() {
		return dx;
	}
	public double getDy() {
		return dy;
	}
	public double getDz() {
		return dz;
	}
	public void setDx(double dx) {
		this.dx = dx;
	}
	public void setDy(double dy) {
		this.dy = dy;
	}
	public void setDz(double dz) {
		this.dz = dz;
	}
	
	public void setDxyz(double dx, double dy, double dz) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}
	
	public double getLength() {
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
	public double getLengthSquared() {
		return dx * dx + dy * dy + dz * dz;
	}
	
	public Vec3 getUnitVector() {
		double div = getLength();
		return new Vec3(dx / div, dy / div, dz / div);
	}
	
	public Vec3 getScaledVector(double scale) {
		return new Vec3(dx * scale, dy * scale, dz * scale);
	}
	
	public Vec3 clone() {
		return new Vec3(dx, dy, dz);
	}
	
	public String toString() {
		return String.format("Vec3: (%.3f, %.3f, %.3f)", dx, dy, dz); 
	}
}
