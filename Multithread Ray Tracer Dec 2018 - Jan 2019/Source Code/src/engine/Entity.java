package engine;

import java.awt.Color;
import java.util.Arrays;

import engine.geom.Model;
import engine.geom.Point3D;
import engine.geom.SMath;
import engine.geom.SphereModel;
import engine.geom.Triangle;
import engine.geom.TriangleModel;
import engine.geom.Vec3;

public class Entity { 
	
	private int modelID;
	private Point3D center;
	private double scale;
	private Color color;
	
	public Entity(String modelName, Point3D center, double scale, Color color) {
		modelID = ModelManager.getModelID(modelName);
		this.center = center;
		this.scale = scale;
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Object[] checkRayIntersection(Point3D source, Vec3 ray) {
		Object[] ret = new Object[2];
		Model m = ModelManager.getModel(modelID);
		if (m.getModelType() == Model.SPHERE_MODEL) {
			SphereModel sm = (SphereModel) m;
			double radius2 = scale * sm.getRadius();
			radius2 *= radius2;
			Vec3 v2 = new Vec3(source, center);
			if (SMath.dotProd(ray, v2) < 0)
				return null;
			Vec3 v1 = SMath.proj(ray, v2);
			double v2sq = v2.getLengthSquared(), 
					v1sq = v1.getLengthSquared();
			double k = v2sq - v1sq;
			if (k > radius2)
				return null;
			Vec3 dis = v1.getScaledVector(1 - Math.sqrt((radius2 - v2sq) / v1sq + 1));
			Point3D inter = new Point3D(source.getX() + dis.getDx(), source.getY() + dis.getDy(), source.getZ() + dis.getDz()); 
			Vec3 normal = new Vec3(center, inter);
			ret[0] = inter;
			ret[1] = normal.getUnitVector();
			return ret;
		} else if (m.getModelType() == Model.TRIANGLE_MODEL) {
			TriangleModel tm = (TriangleModel) m;
			Point3D[] origVerts = tm.getAllVerts();
			Point3D[] verts = new Point3D[origVerts.length];
			boolean[] cloned = new boolean[verts.length];
			Triangle[] tris = tm.getAllTriangles();
			
			
			Point3D closestP = null;
			Vec3 normalVec = null;
			for (int i = 0; i < tris.length; i++) {
				Triangle tr = tris[i];
				for (int j = 0; j < tr.pnts.length; j++) {
					int index = tr.pnts[j];
					if (!cloned[index]) {
						verts[index] = origVerts[index].clone();
						verts[index].setXYZ(verts[index].getX() * scale, verts[index].getY() * scale, verts[index].getZ() * scale);
						cloned[index] = true;
					}
				}
				
				Point3D p1 = verts[tr.pnts[0]], 
						p2 = verts[tr.pnts[1]], 
						p3 = verts[tr.pnts[2]];
				
				Vec3 n2Tri = SMath.crossProd(new Vec3(p1, p2), new Vec3(p1, p3));
				n2Tri = n2Tri.getUnitVector();
				
				if (SMath.dotProd(n2Tri, ray) == 0)
					continue;
				// Calculate D from equation for triangle's plane
				double D = -SMath.dotProd(n2Tri, p1);
				// Calculate T from equation for ray
				double T = -(SMath.dotProd(n2Tri, source) + D) / (SMath.dotProd(n2Tri, ray));
				if (T < 0)
					continue;
				// Bary-centric coordinates
				Point3D p = new Point3D(source.getX() + ray.getDx() * T, 
						source.getY() + ray.getDy() * T, 
						source.getZ() + ray.getDz() * T);
				// Area of the whole triangle
				double areax2 = SMath.crossProd(new Vec3(p1, p2), new Vec3(p1, p3)).getLength();
				Vec3 p_p2 = new Vec3(p, p2);
				// Ratio of p-p1-p2 to the whole triangle
				double rat1 = (SMath.crossProd(new Vec3(p, p1), p_p2)).getLength() / areax2;
				if (rat1 > 1 || rat1 < 0)
					continue;
				// Ratio of p-p2-p3 to the whole triangle
				double rat2 = (SMath.crossProd(p_p2, new Vec3(p, p3))).getLength() / areax2;
				if (rat2 > 1 || rat2 < 0)
					continue;
				// Ratio of p-p1-p3 to the whole triangle
				double rat3 = 1 - rat1 - rat2;
				if (rat3 > 1 || rat3 < 0)
					continue;
				
				if (closestP == null || SMath.distance(closestP, source) > SMath.distance(p, source)) {
					closestP = p;
					normalVec = n2Tri.getUnitVector();
				}
			}
			
			if (closestP == null)
				return null;
			ret[0] = closestP;
			ret[1] = normalVec;
			return ret;
		}
		return null;
	}
}
