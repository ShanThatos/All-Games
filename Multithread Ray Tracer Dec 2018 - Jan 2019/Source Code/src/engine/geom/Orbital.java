package engine.geom;

public class Orbital {
	private double r1, r2;
	public Orbital(double r1, double r2) {
		this.r1 = r1;
		this.r2 = r2;
	}
	public double getR1() {
		return r1;
	}
	public double getR2() {
		return r2;
	}
	public void setR1(double r1) {
		this.r1 = r1;
	}
	public void setR2(double r2) {
		this.r2 = r2;
	}
	
	public void setR12(double r1, double r2) {
		this.r1 = r1;
		this.r2 = r2;
	}
}
