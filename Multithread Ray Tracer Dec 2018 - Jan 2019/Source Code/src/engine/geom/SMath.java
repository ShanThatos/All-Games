package engine.geom;

public class SMath {
	// Adds 2 Vectors
	public static Vec3 addVec(Vec3 a, Vec3 b) {
		return new Vec3(a.getDx() + b.getDx(), a.getDy() + b.getDy(), a.getDz() + b.getDz());
	}
	// Dot Product
	public static double dotProd(Vec3 a, Vec3 b) {
		return a.getDx() * b.getDx() + a.getDy() * b.getDy() + a.getDz() * b.getDz();
	}
	public static double dotProd(Vec3 a, Point3D b) {
		return a.getDx() * b.getX() + a.getDy() * b.getY() + a.getDz() * b.getZ();
	}
	// Cross Product
	public static Vec3 crossProd(Vec3 a, Vec3 b) {
		return new Vec3(a.getDx() * b.getDy() - a.getDy() * b.getDx(), 
				a.getDz() * b.getDx() - a.getDx() * b.getDz(), 
				a.getDy() * b.getDz() - a.getDz() * b.getDy());
	}
	// Rotates Vector by r radians
	public static Vec3 rotateVecAroundZAxis(Vec3 a, double r) {
		double len = a.getLength();
		double ang = Math.atan2(a.getDy(), a.getDx());
		if (a.getDx() == 0) {
			if (a.getDy() > 0)
				ang = Math.PI / 2;
			else 
				ang = -Math.PI / 2;
		}
		
		double newAng = ang + r;
		return new Vec3(len * Math.cos(newAng), len * Math.sin(newAng), a.getDz());
	}
	// Projection of vector b onto vector a
	public static Vec3 proj(Vec3 a, Vec3 b) {
		double scale = dotProd(a, b) / a.getLengthSquared();
		return new Vec3(a.getDx() * scale, a.getDy() * scale, a.getDz() * scale);
	}
	// Distance between points
	public static double distance(Point3D a, Point3D b) {
		double dx = a.getX() - b.getX(), 
				dy = a.getY() - b.getY(), 
				dz = a.getZ() - b.getZ();
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
}
