package net.ddns.lucagiese.games.gui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class GUIManager {

	private List<Button> buttons = new ArrayList<Button>();

	private static GUIManager instance;

	public void checkHover(int x, int y) {
		for (int i = 0; i < buttons.size(); i++) {
			Button button = buttons.get(i);

			Rectangle rectangle = new Rectangle(button.getX(), button.getY(), button.getWidth(), button.getHeight());
			if (rectangle.contains(x, y)) {
				button.setHovering(true);
			} else {
				button.setHovering(false);
			}

		}
	}

	public void checkClick(int x, int y) {
		for (int i = 0; i < buttons.size(); i++) {
			Button button = buttons.get(i);

			Rectangle rectangle = new Rectangle(button.getX(), button.getY(), button.getWidth(), button.getHeight());
			if (rectangle.contains(x, y)) {
				button.onClick();
			}
		}
	}

	public void drawAll(Graphics g) {
		for (int i = 0; i < buttons.size(); i++) {
			Button button = buttons.get(i);
			button.draw(g);
		}
	}

	public void drawNonGame(Graphics g) {
		for (int i = 0; i < buttons.size(); i++) {
			Button button = buttons.get(i);
			if (!(button instanceof GameButton))
				button.draw(g);
		}
	}

	public void drawGameButtons(Graphics g) {
		for (int i = 0; i < buttons.size(); i++) {
			Button button = buttons.get(i);
			if (button instanceof GameButton)
				button.draw(g);
		}
	}

	public void add(Button button) {
		buttons.add(button);
	}

	public void remove(Button button) {
		buttons.remove(button);
	}

	public static GUIManager getInstance() {
		if (instance == null)
			instance = new GUIManager();
		return instance;
	}
}
