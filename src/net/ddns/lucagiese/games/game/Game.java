package net.ddns.lucagiese.games.game;

import java.awt.Graphics;
import java.awt.Image;

import net.ddns.lucagiese.games.gui.GameButton;

public class Game {

	private String name;
	private String description;

	private Image cover;

	private GameButton button;

	private GameState gameState = GameState.menu;

	public Game(String name, String description, Image cover) {
		this.name = name;
		this.description = description;
		this.cover = cover;
	}

	public GameButton getButton() {
		return button;
	}

	public void setButton(GameButton button) {
		this.button = button;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Image getCover() {
		return cover;
	}

	public void setCover(Image cover) {
		this.cover = cover;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public void start() {
		GameManager.getInstance().setPlayingGame(this);
	}
	
	public void resume() {
		
	}
	
	public void pause() {
		
	}

	public void draw(Graphics g) {
		if (gameState.equals(GameState.menu))
			drawMenu(g);
		else if (gameState.equals(GameState.ingame))
			drawIngame(g);
		else if (gameState.equals(GameState.escaped))
			drawEscaped(g);
		else
			drawGameOver(g);
	}

	public void drawMenu(Graphics g) {

	}

	public void drawEscaped(Graphics g) {

	}

	public void drawIngame(Graphics g) {

	}

	public void drawGameOver(Graphics g) {

	}
}
