package net.ddns.lucagiese.games;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

import net.ddns.lucagiese.games.game.GameManager;
import net.ddns.lucagiese.games.gui.GUIManager;
import net.ddns.lucagiese.games.listners.MouseHandler;

public class Drawable extends JLabel {

	private static final long serialVersionUID = -1984395990187239313L;

	private Color menuBackground = new Color(31, 31, 31);

	public Drawable() {
		addMouseListener(MouseHandler.getInstance());
		addMouseMotionListener(MouseHandler.getInstance());
		addMouseWheelListener(MouseHandler.getInstance());

		requestFocus(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		if (GameManager.getInstance().getPlayingGame() == null) {
			drawMainMenu(g);
		} else {
			GameManager.getInstance().getPlayingGame().draw(g);
			GUIManager.getInstance().drawNonGame(g);
		}

		repaint(0);
	}

	public void drawMainMenu(Graphics g) {
		g.setColor(menuBackground);
		g.fillRect(0, 0, getWidth(), getHeight());

		GUIManager.getInstance().drawGameButtons(g);
	}

}
