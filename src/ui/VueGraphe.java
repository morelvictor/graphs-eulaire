import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import java.util.LinkedList;
import java.util.Random;

public abstract class VueGraphe extends JComponent {
	private Graphe graphe;
	private LinkedList<Point> coordonnees;

	private Color COULEUR = Color.BLUE;
	public int DIAMETRE = 15;

	public VueGraphe(Color c, int d, MouseListener controleur) {
		setPreferredSize(new Dimension(850, 850));
		COULEUR = c;
		DIAMETRE = d;
		graphe = new Graphe();
		coordonnees = new LinkedList<Point>();
		addMouseListener(controleur);
		setBorder(BorderFactory.createLineBorder(Color.black));
	}

	public LinkedList<Point> getCoordonnees() {
		return coordonnees;
	}

	public Graphe getGraphe() {
		return graphe;
	}

	public void viderGraphe() {
		graphe = new Graphe();
		coordonnees = new LinkedList<Point>();
	}
	// g.taille() doit valoir coords.size()
	public void setGraphe(Graphe g, LinkedList<Point> coords) {
		graphe = g;
		coordonnees = coords;
	}
	public void ajouteSommet(Point p) {
		graphe.addSommet();
		coordonnees.add(p);
	}

	public Color getCouleur() {
		return COULEUR;
	}

	public int getDiametre() {
		return DIAMETRE;
	}

	public int getId(int x, int y) {
		for (int i = 0; i < coordonnees.size(); i++) {
			if ((x <= coordonnees.get(i).getX() + 3 * DIAMETRE / 2) &&
			    (x >= coordonnees.get(i).getX() - DIAMETRE / 2) &&
			    (y <= coordonnees.get(i).getY() + 3 * DIAMETRE / 2) &&
			    (y >= coordonnees.get(i).getY() - DIAMETRE / 2)) {
				return i;
			}
		}
		return -1;
	}

	public void paintComponent(Graphics g) {
		g.setColor(getCouleur());

		LinkedList<Point> coord = getCoordonnees();
		int diam = getDiametre();

		for (int i = 0; i < graphe.taille(); ++i) {
			((Graphics2D) g).draw(new Ellipse2D.Double(coord.get(i).getX(), coord.get(i).getY(), diam,
			                                           diam));
			for (int j = i; j < graphe.taille(); ++j) {
				if (graphe.getConnexion(i, j)) {
					g.drawLine((int) (coord.get(i).getX() + diam / 2), // Make formatter happy
					           (int) (coord.get(i).getY() + diam / 2), // until I figure out
					           (int) (coord.get(j).getX() + diam / 2), // the setting I need.
					           (int) (coord.get(j).getY() + diam / 2));
				}
			}
		}
	}
}
