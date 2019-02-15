package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import engine.geom.Point3D;
import engine.geom.SMath;
import engine.geom.Vec3;
import game.Camera;
import game.GamePanel;

public class RayTracingThread implements Runnable {
	
	// Pixel Density - Size of drawing pixel compared to screen pixel
	private static final int pixelDensity = 2;
	
	// Camera obj
	private Camera cam;
	
	// Viewplane Data
	private static boolean alreadySet;
	private static Vec3 currentDirection, horizPlaneVec, vertPlaneVec;
	
	// Graphics Object & Display
	private Graphics2D g;
	private BufferedImage display;
	private boolean doneWithImage;
	
	// This thread's segment data
	private int stx, sty, width, height, edx, edy; 
	
	public RayTracingThread(int stx, int sty, int width, int height) {
		this.stx = stx;
		this.sty = sty;
		this.width = width;
		this.height = height;
		edx = stx + width;
		edy = sty + height;
		
		display = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g = display.createGraphics();
		g.transform(AffineTransform.getTranslateInstance(-stx, -sty));
	}
	
	public void setCamera(Camera cam) {
		this.cam = cam;
	}
	
	@Override
	public void run() {
		double disToPlane = GamePanel.WIDTH / 2 / Math.tan(Camera.FOV / 2);
		while (true) {
			while (doneWithImage) 
				try { Thread.sleep(1); } catch (Exception e) {}
			
			if (!alreadySet) {
				currentDirection = 
						new Vec3(disToPlane * Math.cos(cam.getRot().getR2()) * Math.cos(cam.getRot().getR1()), 
								disToPlane * Math.cos(cam.getRot().getR2()) * Math.sin(cam.getRot().getR1()), 
								disToPlane * Math.sin(cam.getRot().getR2()));
				
				// Both vectors point in the positive x & y axis on the 2D viewing plane
				horizPlaneVec = new Vec3(currentDirection.getDy(), -currentDirection.getDx(), 0);
				horizPlaneVec = horizPlaneVec.getUnitVector();
				
				vertPlaneVec = 
						new Vec3((currentDirection.getDz()) * Math.cos(cam.getRot().getR1() + Math.PI), 
								(currentDirection.getDz()) * Math.sin(cam.getRot().getR1() + Math.PI), 
								Math.sqrt(currentDirection.getDx() * currentDirection.getDx() + 
										currentDirection.getDy() * currentDirection.getDy()));
				vertPlaneVec = vertPlaneVec.getUnitVector();
				alreadySet = true;
			}
			
			for (int kx = stx + pixelDensity / 2; kx < edx; kx += pixelDensity) {
				for (int ky = sty + pixelDensity / 2; ky < edy; ky += pixelDensity) {
					Vec3 ray = currentDirection.clone();
					ray = SMath.addVec(ray, horizPlaneVec.getScaledVector(kx));
					ray = SMath.addVec(ray, vertPlaneVec.getScaledVector(ky));
					ray = ray.getUnitVector();
					
					Color c = traceRay(ray);
					g.setColor(c);
					g.fillRect(kx - pixelDensity / 2, ky - pixelDensity / 2, pixelDensity, pixelDensity);
				}
			}
			
			doneWithImage = true;
			alreadySet = false;
		}
	}
	
	private Color traceRay(Vec3 ray) {
		Point3D p = null;
		Vec3 normal = null;
		Color c = Color.black;
		double distance = Double.MAX_VALUE;
		LinkedList<Entity> entities = EntityManager.getEntities();
		for (int i = 0; i < entities.size(); i++) {
			Object[] prop = entities.get(i).checkRayIntersection(cam.getCenter(), ray);
			if (prop == null)
				continue;
			Point3D inter = (Point3D) prop[0];
			double newdist = Double.MAX_VALUE;
			if (p == null || (newdist = SMath.distance(cam.getCenter(), inter)) < distance) {
				if (newdist == Double.MAX_VALUE) 
					newdist = SMath.distance(cam.getCenter(), inter);
				p = inter;
				normal = (Vec3) prop[1];
				c = entities.get(i).getColor();
				distance = newdist;
			}
		}
		
		
		if (c != Color.black)
			c = LightManager.applyLights(p, normal, c);
		return c;
	}
	
	public BufferedImage getDisplay() {
		return display;
	}
	public int[] getSegmentData() {
		return new int[] {stx, sty, width, height};
	}
	
	public boolean isDoneWithImage() {
		return doneWithImage;
	}
	public void resetDoneWithImage() {
		doneWithImage = false;
	}
}
