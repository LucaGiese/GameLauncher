package net.ddns.lucagiese.games.listners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import net.ddns.lucagiese.games.gui.Camera;
import net.ddns.lucagiese.games.gui.GUIManager;

public class MouseHandler implements MouseListener, MouseWheelListener, MouseMotionListener {

	private static MouseHandler instance;

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		GUIManager.getInstance().checkClick(e.getX() - Camera.getInstance().getxOffset(),
				e.getY() - Camera.getInstance().getyOffset());
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		GUIManager.getInstance().checkHover(e.getX() - Camera.getInstance().getxOffset(),
				e.getY() - Camera.getInstance().getyOffset());
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		Camera.getInstance().addY(e.getWheelRotation() * 10);
	}

	public static MouseHandler getInstance() {
		if (instance == null)
			instance = new MouseHandler();
		return instance;
	}
}
