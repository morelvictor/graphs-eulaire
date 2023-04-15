import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import java.util.LinkedList;
import javax.swing.*;

public class GraphiqueEditeur extends GraphiqueDefaut {
	GraphiqueEditeur(BufferedImage bg) {
		super(bg);
	}
	GraphiqueEditeur() {
		super(null);
	}

	public void render_aretes(Graphics2D g, Graphe graphe, LinkedList<Point> coords) {
		Color color = graphe.estEulerien() ? color_default : color_unsolvable;
		g.setColor(color);
		super.render_aretes(g, graphe, coords);
	}
}
