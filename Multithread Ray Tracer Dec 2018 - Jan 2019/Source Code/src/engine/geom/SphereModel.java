package engine.geom;

public class SphereModel extends Model {
	
	private double radius;
	
	public SphereModel() {
		super("Sphere", Model.SPHERE_MODEL);
		radius = 1;
	}
	
	public double getRadius() {
		return radius;
	}
}
