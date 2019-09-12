package net.ddns.lucagiese.games;

import net.ddns.lucagiese.games.game.GameManager;

public class Launcher {

	public static void main(String[] args) {
		Launcher.getInstance();
	}

	private static Launcher instance;

	public Launcher() {
		instance = this;

		Frame.getInstance();
		GameManager.getInstance();
	}

	public static Launcher getInstance() {
		if (instance == null)
			instance = new Launcher();
		return instance;
	}

}
