package game;

import engine.EngineManager;
import engine.EntityManager;
import engine.LightManager;
import engine.ModelManager;

public class GameManager {
	
	private Camera cam;
	
	public GameManager() {
		LightManager.init();
		ModelManager.init();
		EntityManager.init();
		cam = new Camera();
		EngineManager.init(cam);
	}
	
	public void update(double deltaTime) {
		EngineManager.update();
		cam.update(deltaTime);
	}
}
