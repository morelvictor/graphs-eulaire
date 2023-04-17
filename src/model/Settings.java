import java.awt.*;
import java.awt.event.*;
import java.io.*;
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
	int retryKey;
	int menuKey;
	int evidentMoveKey;
	boolean oneClickMouseMove;
	Color graphColor;
	int zoom;
	Font font;

	public Settings() {
		retryKey = KeyEvent.VK_R;
		menuKey = KeyEvent.VK_ESCAPE;
		evidentMoveKey = KeyEvent.VK_SPACE;
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
