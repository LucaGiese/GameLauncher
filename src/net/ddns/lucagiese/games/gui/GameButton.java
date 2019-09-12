package net.ddns.lucagiese.games.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import net.ddns.lucagiese.games.game.Game;
import net.ddns.lucagiese.games.game.GameManager;

public class GameButton extends Button {

	private Game game;

	public GameButton(int x, int y, int width, int height, Game game) {
		super(x, y, width, height);
		this.game = game;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	@Override
	public void onClick() {
		GameManager.getInstance().setPlayingGame(game);
		Camera.getInstance().setActivated(false);
	}

	@Override
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Color old = g.getColor();
		g.setColor(getColor());

		g.fillRect(getX() + Camera.getInstance().getxOffset(), getY() + Camera.getInstance().getyOffset(), getWidth(),
				getHeight());

		g.setColor(Color.BLACK);
		g2d.setStroke(new BasicStroke(4f));
		g2d.drawRect(getX() + Camera.getInstance().getxOffset(), getY() + Camera.getInstance().getyOffset(), getWidth(),
				getHeight());

		g2d.drawString(getGame().getName(), getX() + (getHeight() / 6) + Camera.getInstance().getxOffset(),
				getY() + getHeight() / 5 + Camera.getInstance().getyOffset());

		if (getGame().getCover() != null)
			g.drawImage(game.getCover(), getX() + Camera.getInstance().getxOffset(),
					getY() + Camera.getInstance().getyOffset(), getWidth(), getHeight(), null);

		if (isHovering()) {
			g.setColor(new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), 150));

			g.fillRect(getX() + Camera.getInstance().getxOffset(), getY() + Camera.getInstance().getyOffset(),
					getWidth(), getHeight());
		}

		g.setColor(old);
	}

}
