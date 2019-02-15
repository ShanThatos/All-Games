package com.sskcode.external.imageprocessing;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import com.sskcode.platformer.physics.Vector2D;

public class RotatedImage {
	public BufferedImage i;
	public double ang;
	private int originX, originY;
	
	public RotatedImage(BufferedImage i, double offsetRot) {
		originX = i.getWidth() / 2;
		originY = i.getHeight() / 2;
		
		AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(offsetRot), originX, originY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		this.i = op.filter(i, null);
	}
	
	public BufferedImage getImage() {
		return i;
	}
	
	public int getWidth() {
		return i.getWidth();
	}
	
	public int getHeight() {
		return i.getHeight();
	}
	
	public BufferedImage getRotatedImage(Vector2D v) {
		ang = Math.atan(v.dy / v.dx);
		if (v.dx < 0)
			ang -= Math.PI;
		
		AffineTransform tx = AffineTransform.getRotateInstance(ang, originX, originY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(i, null);
	}
	
	public BufferedImage getRotatedImage(double a) {
		this.ang = a;
		
		AffineTransform tx = AffineTransform.getRotateInstance(ang, originX, originY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		return op.filter(i, null);
	}
}
