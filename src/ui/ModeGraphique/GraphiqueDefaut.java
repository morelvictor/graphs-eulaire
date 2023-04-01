import java.awt.*;
import java.awt.geom.Ellipse2D;

import java.util.LinkedList;
import javax.swing.*;

public class GraphiqueDefaut implements ModeGraphique {
	public void render_sommets(Graphics2D g, LinkedList<Point> coords, int selected) {
		for (int i = 0; i < coords.size(); i++) {
			final var coord1 = coords.get(i);
			final var point_coord = new Point(coord1.x - point_radius, coord1.y - point_radius);
			final var point = new Ellipse2D.Float(point_coord.x, point_coord.y, point_radius * 2,
			                                      point_radius * 2);
			if (i == selected) {
				g.setColor(color_selected);
				g.draw(point);
				g.setColor(color_default);
			} else {
				g.draw(point);
			}
		}
	}

	public void render_aretes(Graphics2D g, Graphe graphe, LinkedList<Point> coords) {
		for (int i = 0; i < coords.size(); i++) {
			final var coord1 = coords.get(i);
			for (int j = i; j < graphe.taille(); ++j) {
				final var connexion_n = graphe.getConnexion(i, j);
				if (connexion_n == 0) {
					continue;
				}
				final var coord2 = coords.get(j);
				g.drawLine(coord1.x, coord1.y, coord2.x, coord2.y);
				if (connexion_n <= 1) {
					continue;
				}
				final var text_x = (coord1.x + coord2.x) / 2;
				final var text_y = (coord1.y + coord2.y) / 2;
				g.drawString(Integer.toString(connexion_n), text_x, text_y);
			}
		}

	}
}
