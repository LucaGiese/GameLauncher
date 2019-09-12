package net.ddns.lucagiese.games;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import net.ddns.lucagiese.games.listners.KeyboardHandler;

public class Frame {

	private JFrame frame;
	private Drawable draw;

	private static Frame instance;

	public Frame() {
		frame = new JFrame();

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) ((int) screen.getWidth() / 1.2);
		int height = (int) ((int) screen.getHeight() / 1.2);

		frame.setSize(width, height);
		frame.setLocationRelativeTo(null);

		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

		draw = new Drawable();
		frame.add(draw);

		frame.addKeyListener(KeyboardHandler.getInstance());

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public JFrame getFrame() {
		return frame;
	}

	public Drawable getDraw() {
		return draw;
	}

	public static Frame getInstance() {
		if (instance == null)
			instance = new Frame();
		return instance;
	}
}
