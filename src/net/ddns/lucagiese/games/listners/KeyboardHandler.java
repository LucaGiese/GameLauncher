package net.ddns.lucagiese.games.listners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import net.ddns.lucagiese.games.game.GameManager;
import net.ddns.lucagiese.games.game.GameState;

public class KeyboardHandler implements KeyListener {

	private static KeyboardHandler instance;

	private boolean w = false, a = false, s = false, d = false;
	private boolean esc = false, space = false, shift = false, control = false;

	@Override
	public void keyPressed(KeyEvent e) {
		setKeys(e, true);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		setKeys(e, false);

		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			if (GameManager.getInstance().getPlayingGame() != null)
				if (GameManager.getInstance().getPlayingGame().getGameState() == GameState.ingame) {
					GameManager.getInstance().getPlayingGame().pause();
					GameManager.getInstance().getPlayingGame().setGameState(GameState.escaped);
				} else if (GameManager.getInstance().getPlayingGame().getGameState() == GameState.escaped) {
					GameManager.getInstance().getPlayingGame().resume();
					GameManager.getInstance().getPlayingGame().setGameState(GameState.ingame);
				}
		}

	}

	public void setKeys(KeyEvent e, boolean state) {
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_W)
			w = state;
		if (code == KeyEvent.VK_A)
			a = state;
		if (code == KeyEvent.VK_S)
			s = state;
		if (code == KeyEvent.VK_D)
			d = state;

		if (code == KeyEvent.VK_ESCAPE)
			esc = state;
		if (code == KeyEvent.VK_SPACE)
			space = state;
		if (code == KeyEvent.VK_SHIFT)
			shift = state;
		if (code == KeyEvent.VK_CONTROL)
			control = state;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public boolean isW() {
		return w;
	}

	public void setW(boolean w) {
		this.w = w;
	}

	public boolean isA() {
		return a;
	}

	public void setA(boolean a) {
		this.a = a;
	}

	public boolean isS() {
		return s;
	}

	public void setS(boolean s) {
		this.s = s;
	}

	public boolean isD() {
		return d;
	}

	public void setD(boolean d) {
		this.d = d;
	}

	public boolean isEsc() {
		return esc;
	}

	public void setEsc(boolean esc) {
		this.esc = esc;
	}

	public boolean isSpace() {
		return space;
	}

	public void setSpace(boolean space) {
		this.space = space;
	}

	public boolean isShift() {
		return shift;
	}

	public void setShift(boolean shift) {
		this.shift = shift;
	}

	public boolean isControl() {
		return control;
	}

	public void setControl(boolean control) {
		this.control = control;
	}

	public static KeyboardHandler getInstance() {
		if (instance == null)
			instance = new KeyboardHandler();
		return instance;
	}
}
