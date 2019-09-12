package net.ddns.lucagiese.games.game.examples.rocket;

import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

import net.ddns.lucagiese.games.Frame;
import net.ddns.lucagiese.games.utilis.ImageLoader;

public class RocketCloud {

	private int basicWidth, basicHeight;

	private double x, y;

	private Image image;

	public RocketCloud() {
		basicWidth = Frame.getInstance().getDraw().getWidth() / 7;
		basicHeight = Frame.getInstance().getDraw().getHeight() / 5;

		Random ran = new Random();

		y = -basicHeight * ran.nextInt(10);

		x = Frame.getInstance().getDraw().getWidth() / 8;
		x *= ran.nextInt(7);

		image = ImageLoader.getInstance().load("rocketCloud.png").getScaledInstance(basicWidth, basicHeight,
				Image.SCALE_SMOOTH);
	}

	public void up() {

	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void draw(Graphics g) {
		g.drawImage(image, (int) x, (int) y, null);
	}

	public void moveDown(double amount) {
		y += amount;
		if (y > Frame.getInstance().getDraw().getHeight()) {
			Random ran = new Random();
			y = -basicHeight * ran.nextInt(5);
		}
	}

}
