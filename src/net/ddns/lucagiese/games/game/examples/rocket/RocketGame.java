package net.ddns.lucagiese.games.game.examples.rocket;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.util.List;

import net.ddns.lucagiese.games.Frame;
import net.ddns.lucagiese.games.game.Game;
import net.ddns.lucagiese.games.game.GameManager;
import net.ddns.lucagiese.games.game.GameState;
import net.ddns.lucagiese.games.gui.Button;
import net.ddns.lucagiese.games.gui.GUIManager;
import net.ddns.lucagiese.games.listners.KeyboardHandler;
import net.ddns.lucagiese.games.utilis.ImageLoader;

public class RocketGame extends Game {

	private static RocketGame instance;

	public RocketGame() {
		super("Rocket Tycoon", null, null);

		String description = "Lass deiene Rackete steigen, und erkunde andere Planetten";
		setDescription(description);

		GameManager.getInstance().addGame(this);
	}

	@Override
	public void drawMenu(Graphics g) {
		setGameState(GameState.ingame);
	}

	private Color ground = new Color(96, 170, 56);
	private Color sky = new Color(135, 206, 235);

	private Color factoryColor = Color.GRAY, factoryBorderColor = Color.BLACK;

	private int skyHeight;
	private int groundHeight;
	private int factoryHeight, factoryWidth, factoryY, factoryX;
	private int factoryChimneyHeight, factoryChimneyWidth;

	private int startPlatformX, startPlatformY, startPlatformWidth, startPlatformHeight;
	private Polygon startPlatform;

	private int rocketX, rocketY, rocketWidth, rocketHeight;

	private Image rocket;

	private RocketState rocketState = RocketState.earth;

	private Button startFlying;

	private double height = 0;
	private int fuel = 100;
	private int fuelEfficiency;
	private double acceleration;
	private double actualSpeed;
	private double maximumSpeed;
	private double drag;

	private int flyingDefaultGroundY;
	private int flyingDefaultGroundHeight;

	private double flyingRocketY;
	private double flyingRocketDefaultY;
	private int flyingRocketX;
	private int flyingRocketWidth;
	private int flyingRocketHeight;
	private Image flyingRocket;

	private int flameX, flameWidth, flameHeight;
	private double flameY;
	private double flameDefaultY;
	private int flameDirection = 0;
	private Image flame;

	private RocketCloud[] clouds = new RocketCloud[5];

	private int speedometerX, speedometerY, speedometerWidth, speedometerHeight;
	private Color speedometer = Color.BLACK, speedometerBorder = Color.RED;

	@Override
	public void drawIngame(Graphics g) {

		if (rocketState.equals(RocketState.earth)) {
			drawEarth(g);
		} else
			drawFlying(g);
	}

	public void drawFlying(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		if (flyingDefaultGroundHeight == 0) {
			flyingDefaultGroundHeight = Frame.getInstance().getDraw().getHeight() / 10;
			flyingDefaultGroundY = Frame.getInstance().getDraw().getHeight() - flyingDefaultGroundHeight;

			flyingRocketWidth = Frame.getInstance().getDraw().getWidth() / 10;
			flyingRocketHeight = Frame.getInstance().getDraw().getHeight() / 3;
			flyingRocketX = Frame.getInstance().getDraw().getWidth() / 2 - flyingRocketWidth / 2;
			flyingRocketY = flyingDefaultGroundY - flyingRocketHeight;
			flyingRocketDefaultY = flyingRocketY;

			flyingRocket = ImageLoader.getInstance().load("rocket.png").getScaledInstance(flyingRocketWidth,
					flyingRocketHeight, Image.SCALE_SMOOTH);

			flameWidth = flyingRocketWidth / 2;
			flameHeight = flyingRocketHeight / 2;
			flameX = flyingRocketX + flyingRocketWidth / 2 - flameWidth / 2;
			flameY = (int) (flyingRocketY + flyingRocketHeight - flyingRocketHeight / 3);
			flameDefaultY = flameY;
			flame = ImageLoader.getInstance().load("rocketFlame.png").getScaledInstance(flameWidth, flameHeight,
					Image.SCALE_SMOOTH);

			for (int i = 0; i < clouds.length; i++)
				clouds[i] = new RocketCloud();

			acceleration = 0.00009;
			maximumSpeed = 5;
			drag = 0.00003;

			speedometerWidth = Frame.getInstance().getDraw().getWidth() / 5;
			speedometerHeight = speedometerWidth;
			speedometerX = -speedometerWidth / 6;
			speedometerY = Frame.getInstance().getDraw().getHeight() - speedometerHeight / 10 * 8;
		}

		g.setColor(sky);
		g.fillRect(0, 0, Frame.getInstance().getDraw().getWidth(), Frame.getInstance().getDraw().getHeight());

		g.setColor(ground);
		g.fillRect(0, (int) (flyingDefaultGroundY + height), Frame.getInstance().getDraw().getWidth(),
				flyingDefaultGroundHeight);

		for (int i = 0; i < clouds.length; i++) {
			clouds[i].draw(g);
		}

		if (flameDirection < 100) {
			flameDirection++;
			g.drawImage(flame, flameX, (int) flameY, null);
		} else if (flameDirection >= 100) {
			g.drawImage(flame, flameX + flameWidth, (int) flameY, -flameWidth, flameHeight, null);
			flameDirection++;
			if (flameDirection > 200)
				flameDirection = 0;
		}

		g.drawImage(flyingRocket, flyingRocketX, (int) flyingRocketY, null);

		g.setColor(speedometer);

		g.fillOval(speedometerX, speedometerY, speedometerWidth, speedometerHeight);

		g2d.setStroke(new BasicStroke(20f));
		g2d.setColor(speedometerBorder);
		g2d.drawOval(speedometerX, speedometerY, speedometerWidth, speedometerHeight);

		Font font = new Font("Palatino", Font.BOLD, Frame.getInstance().getDraw().getWidth() / 50);
		FontMetrics metrics = g.getFontMetrics(font);
		g.setFont(font);
		g.setColor(Color.WHITE);
		String speed;
		if (String.valueOf(actualSpeed * 7000).length() > 5)
			speed = String.valueOf(actualSpeed * 7000).substring(0, 5);
		else
			speed = String.valueOf(actualSpeed * 7000);
		g.drawString(speed,
				(int) (speedometerX + speedometerWidth / 2 - metrics.getStringBounds(speed, g).getWidth() / 2),
				speedometerY);

		String heightString = height + "";
		g.drawString(heightString,
				(int) (speedometerX + speedometerWidth / 2 - metrics.getStringBounds(speed, g).getWidth() / 2),
				(int) (speedometerY + metrics.getStringBounds(speed, g).getHeight() * 2));

		if (KeyboardHandler.getInstance().isSpace()) {
			if (actualSpeed <= maximumSpeed)
				actualSpeed += acceleration;
			height += acceleration;

			if (height < 70) {
				flyingRocketY -= actualSpeed;
				flameY -= actualSpeed;
			}
		} else {
			if (actualSpeed > 0) {
				actualSpeed -= drag;
				if (actualSpeed > 0 && actualSpeed < 0.00009)
					actualSpeed = 0;
			}

			if (height > 0.0006)
				height -= drag;

			if (height < 0)
				height = 0;

			if (height < 100 && height > 1) {
				flyingRocketY += actualSpeed;
				flameY += actualSpeed;
			} else if (height < 1) {
				flyingRocketY = flyingRocketDefaultY;
				flameY = flameDefaultY;
			}
		}

		for (int i = 0; i < clouds.length; i++) {
			clouds[i].moveDown(actualSpeed);
		}
	}

	public void drawEarth(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		if (skyHeight == 0) {
			skyHeight = Frame.getInstance().getDraw().getHeight() / 3 * 2;
			groundHeight = Frame.getInstance().getDraw().getHeight() / 3;

			factoryHeight = Frame.getInstance().getDraw().getHeight() / 4;
			factoryWidth = Frame.getInstance().getDraw().getWidth() / 4;

			factoryY = skyHeight - skyHeight / 5;
			factoryX = Frame.getInstance().getDraw().getWidth() / 20;

			factoryChimneyHeight = Frame.getInstance().getDraw().getHeight() / 5;
			factoryChimneyWidth = Frame.getInstance().getDraw().getWidth() / 50;

			startPlatformX = Frame.getInstance().getDraw().getWidth() / 10 * 8;
			startPlatformY = (int) (Frame.getInstance().getDraw().getHeight() / 10 * 3.8);
			startPlatformWidth = Frame.getInstance().getDraw().getWidth() / 50;
			startPlatformHeight = Frame.getInstance().getDraw().getHeight() / 3;
			startPlatform = new Polygon();

			startPlatform.addPoint(startPlatformX - startPlatformWidth * 3, startPlatformY + startPlatformHeight);
			startPlatform.addPoint(startPlatformX + startPlatformWidth * 4, startPlatformY + startPlatformHeight);

			startPlatform.addPoint(startPlatformX + startPlatformWidth * 6,
					startPlatformY + startPlatformHeight + startPlatformHeight / 6);

			startPlatform.addPoint(startPlatformX + startPlatformWidth * 6,
					startPlatformY + startPlatformHeight + startPlatformHeight / 3);

			startPlatform.addPoint(startPlatformX + startPlatformWidth * 4,
					startPlatformY + startPlatformHeight + startPlatformHeight / 2);
			startPlatform.addPoint(startPlatformX - startPlatformWidth * 3,
					startPlatformY + startPlatformHeight + startPlatformHeight / 2);

			startPlatform.addPoint(startPlatformX - startPlatformWidth * 5,
					startPlatformY + startPlatformHeight + startPlatformHeight / 3);

			startPlatform.addPoint(startPlatformX - startPlatformWidth * 5,
					startPlatformY + startPlatformHeight + startPlatformHeight / 6);

			rocketY = startPlatformY + startPlatformHeight / 3;
			rocketWidth = startPlatformWidth * 4;
			rocketHeight = startPlatformHeight - startPlatformHeight / 6;

			rocketX = startPlatformX + startPlatformWidth / 2 - rocketWidth / 2;
			rocket = ImageLoader.getInstance().load("rocket.png");
			rocket = rocket.getScaledInstance(rocketWidth, rocketHeight, Image.SCALE_SMOOTH);

			startFlying = new Button(rocketX, rocketY, rocketWidth, rocketHeight);
			startFlying.setVisible(false);
			startFlying.setOnClick(new Runnable() {
				@Override
				public void run() {
					rocketState = RocketState.flying;
					GUIManager.getInstance().remove(startFlying);
				}
			});
			GUIManager.getInstance().add(startFlying);
		}

		g.setColor(sky);
		g.fillRect(0, 0, Frame.getInstance().getDraw().getWidth(), skyHeight);

		g.setColor(ground);
		g.fillRect(0, skyHeight, Frame.getInstance().getDraw().getWidth(), groundHeight);

		g.setColor(factoryColor);
		g.fill3DRect(factoryX, factoryY, factoryWidth, factoryHeight, true);

		g.fill3DRect(factoryX, factoryY - factoryChimneyHeight, factoryChimneyWidth, factoryChimneyHeight, true);

		g.setColor(factoryBorderColor);
		g.drawRect(factoryX, factoryY, factoryWidth, factoryHeight);
		g.drawRect(factoryX, factoryY - factoryChimneyHeight, factoryChimneyWidth, factoryChimneyHeight);

		g2d.drawRect(startPlatformX, startPlatformY, startPlatformWidth, startPlatformHeight);
		g2d.fillPolygon(startPlatform);

		g.setColor(Color.red);

		g.drawImage(rocket, rocketX, rocketY, rocketWidth, rocketHeight, null);

	}

	public static RocketGame getInstance() {
		if (instance == null)
			instance = new RocketGame();
		return instance;
	}
}
