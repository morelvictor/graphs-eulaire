import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

import java.util.LinkedList;
import javax.swing.*;

public class GraphiqueDefaut extends ModeGraphique {
	GraphiqueDefaut(BufferedImage bg) {
		super(bg);
	}
	GraphiqueDefaut() {
		super(null);
	}

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
			final var coord1_ = coords.get(i);
			for (int j = i; j < graphe.taille(); ++j) {
				final var connexion_n = graphe.getConnexion(i, j);
				if (connexion_n == 0) {
					continue;
				}
				final var coord2_ = coords.get(j);
				final var coord_dir = new Point(coord2_.x - coord1_.x, coord2_.y - coord1_.y);
				final var coord_dir_mag =
					Math.sqrt(coord_dir.x * coord_dir.x + coord_dir.y * coord_dir.y);
				final var coord_dir_norm =
					new Point((int)(coord_dir.x * point_radius / coord_dir_mag),
					          (int)(coord_dir.y * point_radius / coord_dir_mag));
				final var coord1 =
					new Point(coord1_.x + coord_dir_norm.x, coord1_.y + coord_dir_norm.y);
				final var coord2 =
					new Point(coord2_.x - coord_dir_norm.x, coord2_.y - coord_dir_norm.y);
				final var orth = new Point(coord1.y - coord2.y, coord2.x - coord1.x);
				final var orth_mag = Math.sqrt(orth.x * orth.x + orth.y * orth.y);
				final var orth_norm =
					new Point((int)(orth.x * 25 / orth_mag), (int)(orth.y * 25 / orth_mag));
				final var coord_center =
					new Point((coord1.x + coord2.x) / 2, (coord1.y + coord2.y) / 2);
				for (int k = 0; k < connexion_n; k++) {
					final var index = k - connexion_n / 2;
					final var ctrl = new Point(coord_center.x + orth_norm.x * index,
					                           coord_center.y + orth_norm.y * index);
					final var curve = new Path2D.Float();
					curve.moveTo(coord1.x, coord1.y);
					final int count = (int)coord_dir_mag / 10;
					for (int l = 1; l <= count; l++) {
						final var t = l / (float)count;
						final var tt = 1 - t;
						final var p =
							new Point((int)(tt * tt * coord1.x + 2 * tt * t * ctrl.x + t *
							                t * coord2.x),
							          (int)(tt * tt * coord1.y + 2 * tt * t * ctrl.y + t *
							                t * coord2.y));
						final var offset = new Point(Integer.hashCode(l) % 3 - 1,
						                             p.hashCode() % 3 - 1);
						curve.lineTo(p.x + offset.x, p.y + offset.y);
					}
					g.draw(curve);
				}
			}
		}
	}

	void render_background(Graphics2D g) {
		if (image_bg == null) {
			g.setColor(color_bg);
			g.fillRect(0, 0, 850, 850);
		} else {
			g.drawImage(image_bg, null, 0, 0);
		}
	}
}
