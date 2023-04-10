import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import java.util.LinkedList;
import javax.swing.*;

abstract class ModeGraphique {
	final Color color_bg = Color.WHITE;
	final BufferedImage image_bg;
	final Color color_default = Color.BLUE;
	final Color color_unsolvable = Color.RED;
	final Color color_selected = Color.GREEN;
	final int point_radius = 8;

	ModeGraphique(BufferedImage bg) {
		this.image_bg = bg;
	}
	ModeGraphique() {
		this(null);
	}

	abstract void render_sommets(Graphics2D g, LinkedList<Point> coords, int selected);
	abstract void render_aretes(Graphics2D g, Graphe graphe, LinkedList<Point> coords);
	abstract void render_background(Graphics2D g);
}
