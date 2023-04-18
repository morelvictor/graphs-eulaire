import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.*;

/**
 * Retry -> r
 * Menu -> echap
 * Avancer evident -> space
 * Mode de deplacement souris -> bool
 * Couleur des graphe -> ColorMode
 * Zoom -> entier
 */

public class Settings {
	AtomicInteger retryKey;
	AtomicInteger menuKey;
	AtomicInteger evidentMoveKey;
	boolean oneClickMouseMove;
	Color graphColor;
	int zoom;
	Font font;

	public Settings() {
		retryKey = new AtomicInteger(KeyEvent.VK_R);
		menuKey = new AtomicInteger(KeyEvent.VK_ESCAPE);
		evidentMoveKey = new AtomicInteger(KeyEvent.VK_SPACE);
		oneClickMouseMove = false;
		graphColor = Color.BLUE;
		zoom = 1;
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
			                                new File("../textures/IAmTheCrayonMaster.ttf")));
			font = new Font("IAmTheCrayonMaster", Font.BOLD, 30);
		} catch (IOException | FontFormatException e) {}
	}
}
