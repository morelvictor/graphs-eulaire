import java.awt.*;
import java.awt.geom.Ellipse2D;

import java.util.LinkedList;
import javax.swing.*;

interface ModeGraphique {
	final Color color_bg = Color.WHITE;
	final Color color_default = Color.BLUE;
	final Color color_unsolvable = Color.RED;
	final Color color_selected = Color.GREEN;
	final int point_radius = 8;

	void render_sommets(Graphics2D g, LinkedList<Point> coords, int selected);
	void render_aretes(Graphics2D g, Graphe graphe, LinkedList<Point> coords);
	default void render_background() {
	}
}
