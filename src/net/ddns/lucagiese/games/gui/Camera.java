package net.ddns.lucagiese.games.gui;

public class Camera {

	private int xOffset = 0, yOffset = 0;
	private static Camera instance;
	private boolean activated = true;

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public void addX(int x) {
		xOffset += x;
	}

	public void addY(int y) {
		yOffset += y;
	}

	public int getxOffset() {
		if (activated)
			return xOffset;
		return 0;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getyOffset() {
		if (activated)
			return yOffset;
		return 0;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}

	public static Camera getInstance() {
		if (instance == null)
			instance = new Camera();
		return instance;
	}
}
