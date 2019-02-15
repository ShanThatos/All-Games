package engine.geom;

import java.util.Arrays;
import java.util.Scanner;

public class TriangleModel extends Model {
	
	private Point3D[] verts;
	private Triangle[] tris;
	
	public TriangleModel() {
		super("Undetermined Triangle Model", Model.TRIANGLE_MODEL);
	}
	
	public void loadModel(String modelName, String modelData) {
		super.modelName = modelName;
		
		Scanner in = new Scanner(modelData);
		int vnum = in.nextInt();
		verts = new Point3D[vnum];
		for (int i = 0; i < vnum; i++) 
			verts[i] = new Point3D(in.nextDouble(), in.nextDouble(), in.nextDouble());
		int tnum = in.nextInt();
		tris = new Triangle[tnum];
		for (int i = 0; i < tnum; i++)
			tris[i] = new Triangle(in.nextInt(), in.nextInt(), in.nextInt());
		in.close();
	}
	
	public Point3D[] getAllVerts() {
		return verts;
	}
	public Triangle[] getAllTriangles() {
		return tris;
	}
}