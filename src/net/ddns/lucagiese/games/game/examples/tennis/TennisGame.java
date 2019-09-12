package net.ddns.lucagiese.games.game.examples.tennis;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import net.ddns.lucagiese.games.Frame;
import net.ddns.lucagiese.games.game.Game;
import net.ddns.lucagiese.games.game.GameManager;
import net.ddns.lucagiese.games.game.GameState;
import net.ddns.lucagiese.games.gui.Button;
import net.ddns.lucagiese.games.gui.GUIManager;
import net.ddns.lucagiese.games.listners.KeyboardHandler;
import net.ddns.lucagiese.games.utilis.ImageLoader;

public class TennisGame extends Game {

	private static TennisGame instance;

	private Button startButton;
	private Button menu, resume;
	private Button restart;

	private Rectangle box;

	private double xBall, yBall;
	private int ballSize;

	private double ballMovementX = -0.2;
	private double ballMovementY = -0.2;

	private double xPlayer, yPlayer;
	private double playerWidth, playerHeight;

	private double playerMovement = 0.2;

	private int points = 0;

	private BufferedImage screenShot = null;
	private Image background = null;

	private Queue<Point> balls = new LinkedBlockingQueue<>();

	private Color ingameBack = new Color(118, 118, 118), ingameBoxColor = new Color(30, 30, 30),
			ballColor = new Color(239, 239, 239), ballBorderColor = new Color(179, 174, 174),
			ingameBorder = new Color(176, 171, 171);

	public TennisGame() {
		super("Pong", null, null);
		setCover(ImageLoader.getInstance().load("tenniscover.png"));

		String des = "Spiele Pong gegen die Wand, Versuche den Ball daran zu hindern hinter deine Blockade zu kommen";
		setDescription(des);

		GameManager.getInstance().addGame(this);
	}

	public void reset() {
		ballMovementX = -0.2;
		ballMovementY = -0.2;
		xPlayer = 0;
		yPlayer = 0;
		playerWidth = 0;
		playerHeight = 0;
		playerMovement = 0.2;

		xBall = 0;
		yBall = 0;

		points = 0;

		screenShot = null;
	}

	@Override
	public void pause() {
		takeScreen();
	}

	@Override
	public void resume() {
		GUIManager.getInstance().remove(menu);
		GUIManager.getInstance().remove(resume);
	}

	@Override
	public void drawMenu(Graphics g) {
		g.fillRect(0, 0, Frame.getInstance().getDraw().getWidth(), Frame.getInstance().getDraw().getHeight());

		if (startButton == null) {
			int width = Frame.getInstance().getDraw().getWidth() / 4;
			int height = Frame.getInstance().getDraw().getHeight() / 10;

			startButton = new Button(Frame.getInstance().getDraw().getWidth() / 2 - width / 2,
					Frame.getInstance().getDraw().getHeight() / 2 - height / 2,
					Frame.getInstance().getDraw().getWidth() / 4, Frame.getInstance().getDraw().getHeight() / 10);
			startButton.setColor(Color.GREEN);

			startButton.setText("Spielen");
			startButton.setOnClick(new Runnable() {
				@Override
				public void run() {
					setGameState(GameState.ingame);
					Frame.getInstance().getDraw().repaint();
				}
			});

			GUIManager.getInstance().add(startButton);
		}

		if (background == null)
			background = ImageLoader.getInstance().load("tennisbackground.png");

		if (background != null)
			g.drawImage(background, 0, 0, Frame.getInstance().getDraw().getWidth(),
					Frame.getInstance().getDraw().getHeight(), null);
	}

	double lastAddedX = 0;

	@Override
	public void drawIngame(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		if (startButton != null) {
			GUIManager.getInstance().remove(startButton);
			startButton = null;
		}

		g.setColor(ingameBack);
		g.fillRect(0, 0, Frame.getInstance().getDraw().getWidth(), Frame.getInstance().getDraw().getHeight());

		if (box == null) {
			int width = Frame.getInstance().getDraw().getWidth() / 2;
			int height = Frame.getInstance().getDraw().getHeight();
			box = new Rectangle(Frame.getInstance().getDraw().getWidth() / 2 - width / 2, 0, width, height);
		}

		g.setColor(ingameBoxColor);
		g.fillRect((int) box.getX(), (int) box.getY(), (int) box.getWidth(), (int) box.getHeight());

		g2d.setColor(ingameBorder);
		g2d.setStroke(new BasicStroke(10f));
		g2d.drawRect((int) box.getX(), (int) box.getY(), (int) box.getWidth(), (int) box.getHeight() + 20);

		Font font = new Font("Palatino", Font.BOLD, Frame.getInstance().getDraw().getWidth() / 50);
		FontMetrics metrics = g.getFontMetrics(font);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(points + "", (int) box.getX() + 10,
				(int) box.getY() + (int) metrics.getStringBounds(points + "", g).getHeight() - 2);

		g.setColor(ballColor);

		if (xBall == 0) {
			xBall = Frame.getInstance().getDraw().getWidth() / 2 - 50 / 2;
			yBall = Frame.getInstance().getDraw().getHeight() / 4;
			ballSize = Frame.getInstance().getDraw().getHeight() / 18;
		}

		if (lastAddedX == 0)
			lastAddedX = xBall;

		boolean added = false;
		double difference = lastAddedX - xBall;

		if (difference < 0)
			difference *= -1;

		if (difference > 20) {
			balls.add(new Point((int) xBall, (int) yBall));
			added = true;
			lastAddedX = xBall;
		}

		int counter = 0;

		Iterator<Point> iter = balls.iterator();
		while (iter.hasNext()) {
			counter++;
			Point point = iter.next();

			int trans = 100 / 4 * counter;

			Color color = new Color(ballColor.getRed(), ballColor.getGreen(), ballColor.getBlue(), trans);
			g.setColor(color);

			g.fillOval((int) point.x, (int) point.y, ballSize, ballSize);
			g2d.setStroke(new BasicStroke(5f));
			color = new Color(ballBorderColor.getRed(), ballBorderColor.getGreen(), ballBorderColor.getBlue(), trans);
			g2d.setColor(color);
			g2d.drawOval((int) point.x, (int) point.y, ballSize, ballSize);
		}

		if (added && balls.size() > 3)
			balls.poll();

		g.setColor(ballColor);
		g.fillOval((int) xBall, (int) yBall, ballSize, ballSize);

		g2d.setStroke(new BasicStroke(5f));
		g2d.setColor(ballBorderColor);
		g2d.drawOval((int) xBall, (int) yBall, ballSize, ballSize);

		if (yBall <= 0) {
			ballMovementY *= -1;
			ballMovementX *= -1;
		}

		if (box.contains(xBall, yBall) == false || box.contains(xBall + ballSize, yBall) == false) {
			ballMovementX *= -1;
		}

		if (xPlayer == 0) {
			playerWidth = Frame.getInstance().getDraw().getWidth() / 6;
			playerHeight = Frame.getInstance().getDraw().getHeight() / 15;

			xPlayer = Frame.getInstance().getDraw().getWidth() / 2 - playerWidth / 2;
			yPlayer = Frame.getInstance().getDraw().getHeight() - Frame.getInstance().getDraw().getHeight() / 7;
		}

		g.setColor(Color.WHITE);
		g.fillRect((int) xPlayer, (int) yPlayer, (int) playerWidth, (int) playerHeight);

		g2d.setStroke(new BasicStroke(5f));
		g2d.setColor(ballBorderColor);
		g2d.drawRect((int) xPlayer, (int) yPlayer, (int) playerWidth, (int) playerHeight);

		if ((KeyboardHandler.getInstance().isW() || KeyboardHandler.getInstance().isA()) && xPlayer > box.getX() + 10) {
			xPlayer += playerMovement * -1;
		} else if ((KeyboardHandler.getInstance().isS() || KeyboardHandler.getInstance().isD())
				&& xPlayer + playerWidth < box.getX() + box.getWidth() - 10) {
			xPlayer += playerMovement;
		}

		Rectangle player = new Rectangle((int) xPlayer, (int) yPlayer, (int) playerWidth, (int) playerHeight);
		if (player.contains(new Point((int) xBall, (int) yBall + ballSize))
				|| player.contains(new Point((int) xBall + ballSize, (int) yBall + ballSize))) {
			ballMovementY *= -1;
			ballMovementX *= 1.05;
			ballMovementY *= 1.05;
			points++;
		}

		if (yBall > box.getMaxY()) {
			takeScreen();

			setGameState(GameState.gameOver);
		}

		xBall += ballMovementX;
		yBall += ballMovementY;
		Frame.getInstance().getDraw().repaint((int) box.getX(), (int) box.getY(), (int) box.getWidth(),
				(int) box.getHeight());
	}

	public void takeScreen() {
		try {
			Robot robot = new Robot();
			Rectangle bounds = Frame.getInstance().getDraw().getBounds();
			bounds.setLocation(Frame.getInstance().getDraw().getLocationOnScreen());

			screenShot = robot.createScreenCapture(bounds);

			BufferedImage filteredImage = new BufferedImage(screenShot.getWidth(null), screenShot.getHeight(null),
					BufferedImage.TYPE_BYTE_GRAY);

			Graphics g1 = filteredImage.getGraphics();
			g1.drawImage(screenShot, 400, 200, null);

			float[] blurKernel = { 1 / 9f, 1 / 9f, 1 / 9f, 1 / 9f, 1 / 9f, 1 / 9f, 1 / 9f, 1 / 9f, 1 / 9f };

			BufferedImageOp blur = new ConvolveOp(new Kernel(3, 3, blurKernel));
			screenShot = blur.filter(screenShot, null);
			g1.dispose();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void drawEscaped(Graphics g) {
		g.drawImage(screenShot, 0, 0, Frame.getInstance().getDraw().getWidth(),
				Frame.getInstance().getDraw().getHeight(), null);
		g.setColor(new Color(0, 0, 0, 95));
		g.fillRect(0, 0, Frame.getInstance().getDraw().getWidth(), Frame.getInstance().getDraw().getHeight());

		if (menu == null) {
			int width = Frame.getInstance().getDraw().getWidth() / 4;
			int height = Frame.getInstance().getDraw().getHeight() / 10;

			menu = new Button(Frame.getInstance().getDraw().getWidth() / 2 - width / 2,
					Frame.getInstance().getDraw().getHeight() / 2 + height,
					Frame.getInstance().getDraw().getWidth() / 4, Frame.getInstance().getDraw().getHeight() / 10);
			menu.setColor(ballColor);
			menu.setBorder(Color.BLACK);
			menu.setText("Zurück zum Menü");
			menu.setTextColor(Color.BLACK);

			menu.setOnClick(new Runnable() {
				@Override
				public void run() {
					reset();
					GUIManager.getInstance().remove(menu);

					menu = null;
					restart = null;
					setGameState(GameState.menu);
				}
			});

			GUIManager.getInstance().add(menu);
		}

		if (resume == null) {
			int width = Frame.getInstance().getDraw().getWidth() / 4;
			int height = Frame.getInstance().getDraw().getHeight() / 10;

			resume = new Button(Frame.getInstance().getDraw().getWidth() / 2 - width / 2,
					Frame.getInstance().getDraw().getHeight() / 2 - height,
					Frame.getInstance().getDraw().getWidth() / 4, Frame.getInstance().getDraw().getHeight() / 10);
			resume.setColor(ballColor);
			resume.setBorder(Color.BLACK);
			resume.setText("Weiter");
			resume.setTextColor(Color.BLACK);

			resume.setOnClick(new Runnable() {

				@Override
				public void run() {
					GUIManager.getInstance().remove(menu);
					GUIManager.getInstance().remove(resume);

					menu = null;
					resume = null;

					setGameState(GameState.ingame);
				}

			});

			GUIManager.getInstance().add(resume);
		}
	}

	@Override
	public void drawGameOver(Graphics g) {
		g.drawImage(screenShot, 0, 0, Frame.getInstance().getDraw().getWidth(),
				Frame.getInstance().getDraw().getHeight(), null);
		g.setColor(new Color(0, 0, 0, 95));
		g.fillRect(0, 0, Frame.getInstance().getDraw().getWidth(), Frame.getInstance().getDraw().getHeight());

		if (menu == null) {
			int width = Frame.getInstance().getDraw().getWidth() / 4;
			int height = Frame.getInstance().getDraw().getHeight() / 10;

			menu = new Button(Frame.getInstance().getDraw().getWidth() / 2 - width / 2,
					Frame.getInstance().getDraw().getHeight() / 2 + height,
					Frame.getInstance().getDraw().getWidth() / 4, Frame.getInstance().getDraw().getHeight() / 10);
			menu.setColor(ballColor);
			menu.setBorder(Color.BLACK);
			menu.setText("Zurück zum Menü");
			menu.setTextColor(Color.BLACK);

			menu.setOnClick(new Runnable() {
				@Override
				public void run() {
					reset();

					GUIManager.getInstance().remove(menu);
					GUIManager.getInstance().remove(restart);

					menu = null;
					restart = null;
					setGameState(GameState.menu);
				}
			});

			GUIManager.getInstance().add(menu);
		}

		if (restart == null) {
			int width = Frame.getInstance().getDraw().getWidth() / 4;
			int height = Frame.getInstance().getDraw().getHeight() / 10;

			restart = new Button(Frame.getInstance().getDraw().getWidth() / 2 - width / 2,
					Frame.getInstance().getDraw().getHeight() / 2 - height,
					Frame.getInstance().getDraw().getWidth() / 4, Frame.getInstance().getDraw().getHeight() / 10);
			restart.setColor(ballColor);
			restart.setBorder(Color.BLACK);
			restart.setText("Neu Starten");
			restart.setTextColor(Color.BLACK);

			restart.setOnClick(new Runnable() {

				@Override
				public void run() {
					reset();

					GUIManager.getInstance().remove(menu);
					GUIManager.getInstance().remove(restart);

					menu = null;
					restart = null;

					setGameState(GameState.ingame);
				}

			});

			GUIManager.getInstance().add(restart);
		}
	}

	public static TennisGame getInstance() {
		if (instance == null)
			instance = new TennisGame();
		return instance;
	}
}
