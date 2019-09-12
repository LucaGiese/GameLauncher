package net.ddns.lucagiese.games.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class Button {

	private Color color = Color.WHITE, border = Color.BLACK, hover = Color.GRAY;

	private int x, y, width, height;

	private boolean isHovering;

	private Runnable clickEvent;

	private String text;
	private Color textColor;

	private boolean visible = true;

	public Button(int x, int y, int width, int height) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public boolean isHovering() {
		return isHovering;
	}

	public void setHovering(boolean isHovering) {
		this.isHovering = isHovering;
	}

	public Color getBorder() {
		return border;
	}

	public void setBorder(Color border) {
		this.border = border;
	}

	public Color getHover() {
		return hover;
	}

	public void setHover(Color hover) {
		this.hover = hover;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void onClick() {
		clickEvent.run();
	}

	public void setOnClick(Runnable run) {
		clickEvent = run;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void draw(Graphics g) {
		if (visible) {
			Color old = g.getColor();
			if (color != null) {
				g.setColor(color);

				if (hover != null)
					if (isHovering() && hover != null)
						g.setColor(hover);

				g.fillRect(x + Camera.getInstance().getxOffset(), y + Camera.getInstance().getyOffset(), width, height);
			}

			if (getText() != null) {
				if (getTextColor() == null)
					g.setColor(Color.BLACK);
				else
					g.setColor(textColor);

				int size = getHeight() / 2;

				Font font = new Font("Palatino", Font.LAYOUT_LEFT_TO_RIGHT, size);
				FontMetrics metrics = g.getFontMetrics(font);
				g.setFont(font);

				Rectangle2D t = metrics.getStringBounds(getText(), g);
				g.drawString(getText(),
						(int) (x + getWidth() / 2 - t.getWidth() / 2 + Camera.getInstance().getxOffset()),
						(int) (y + getHeight() - t.getHeight() / 2 + Camera.getInstance().getyOffset()));
			}

			if (border != null) {
				g.setColor(border);
				g.drawRect(x + Camera.getInstance().getxOffset(), y + Camera.getInstance().getyOffset(), width, height);
			}

			g.setColor(old);
		}
	}
}
