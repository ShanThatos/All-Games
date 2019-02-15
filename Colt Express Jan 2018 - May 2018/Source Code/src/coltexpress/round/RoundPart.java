package coltexpress.round;

public class RoundPart {
	public boolean faceUp;
	public boolean speedUp;
	public boolean counterClockwise;
	public RoundPart(boolean fU, boolean speedUp, boolean reverse) {
		faceUp = fU;
		this.speedUp = speedUp;
		counterClockwise = reverse;
	}
	public boolean inATunnel() {
		return !faceUp;
	}
}
