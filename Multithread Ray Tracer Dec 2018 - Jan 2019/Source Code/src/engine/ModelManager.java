package engine;

import engine.geom.Model;
import engine.geom.SphereModel;
import engine.geom.TriangleModel;

public class ModelManager {
	
	private static Model[] models;
	
	// Cube Model
	private static String cubeModel = 
			"8 "
			+ ".5 .5 .5 "
			+ ".5 .5 -.5 "
			+ ".5 -.5 .5 "
			+ ".5 -.5 -.5 "
			+ "-.5 .5 .5 "
			+ "-.5 .5 -.5 "
			+ "-.5 -.5 .5 "
			+ "-.5 -.5 -.5 "
			+ "12 "
			+ "1 2 0 "
			+ "1 2 3 "
			+ "0 5 4 "
			+ "0 5 1 "
			+ "6 5 4 "
			+ "6 5 7 "
			+ "6 3 7 "
			+ "6 3 2 "
			+ "0 6 4 "
			+ "0 6 2 "
			+ "1 7 5 "
			+ "1 7 3 ";
	
	public static void init() {
		models = new Model[2];
		models[0] = new SphereModel();
		TriangleModel tm = new TriangleModel();
		tm.loadModel("Cube", cubeModel);
		models[1] = tm;
	}
	public static int getModelID(String modelName) {
		for (int i = 0; i < models.length; i++) 
			if (models[i].getModelName().equals(modelName)) 
				return i;
		return -1;
	}
	public static Model getModel(int id) {
		return models[id];
	}
}
