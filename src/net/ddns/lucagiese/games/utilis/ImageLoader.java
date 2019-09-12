package net.ddns.lucagiese.games.utilis;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {

	private static ImageLoader instance;

	public Image load(String file) {
		try {
			if (getClass().getResource("/resources/" + file) == null)
				throw new IOException("notfound");
			ImageIO.read(getClass().getResource("/resources/" + file));
			return ImageIO.read(getClass().getResource("/resources/" + file));
		} catch (IOException e) {
			try {
				return ImageIO.read(new File("resources/" + file));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}

	public static ImageLoader getInstance() {
		if (instance == null)
			instance = new ImageLoader();
		return instance;
	}
}
