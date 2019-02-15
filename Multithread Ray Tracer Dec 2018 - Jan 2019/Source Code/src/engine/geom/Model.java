package engine.geom;

public class Model {
	
	public static final int SPHERE_MODEL = 1, TRIANGLE_MODEL = 2;
	
	protected String modelName;
	private int modelType;
	
	public Model(String modelName, int modelType) {
		this.modelName = modelName;
		this.modelType = modelType;
	}
	
	public String getModelName() {
		return modelName;
	}
	public int getModelType() {
		return modelType;
	}
}
