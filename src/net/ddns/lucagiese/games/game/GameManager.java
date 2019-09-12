package net.ddns.lucagiese.games.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.ddns.lucagiese.games.Frame;
import net.ddns.lucagiese.games.game.examples.rocket.RocketGame;
import net.ddns.lucagiese.games.game.examples.tennis.TennisGame;
import net.ddns.lucagiese.games.gui.GUIManager;
import net.ddns.lucagiese.games.gui.GameButton;

public class GameManager {

	private List<Game> games = new ArrayList<>();

	private static GameManager instance;

	private Game playingGame;

	public GameManager() {
		instance = this;

		registerDefaults();

		createButtons();
	}

	public void registerDefaults() {
		TennisGame.getInstance();
		RocketGame.getInstance();
	}

	public void createButtons() {
		int lastX = 50;
		int lastY = 30;

		int width = Frame.getInstance().getDraw().getWidth() / 10;
		int height = (int) (Frame.getInstance().getDraw().getHeight() / 4);

		int counter = 0;
		for (Game game : games) {
			int x = lastX;
			int y = lastY;

			if (counter > 0) {
				x += width + 50;
			}

			GameButton button = new GameButton(x, y, width, height, game);

			button.setColor(Color.WHITE);

			GUIManager.getInstance().add(button);

			counter++;
		}

	}

	public void addGame(Game g) {
		games.add(g);
	}

	public void removeGame(Game g) {
		games.remove(g);
	}

	public Game getPlayingGame() {
		return playingGame;
	}

	public void setPlayingGame(Game playingGame) {
		this.playingGame = playingGame;
	}

	public static GameManager getInstance() {
		if (instance == null)
			instance = new GameManager();
		return instance;
	}

}
