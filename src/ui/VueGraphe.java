import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

	public void supprCoordonnee(int i) {
		coordonnees.remove(i);

	}

	public Graphe getGraphe() {
		return graphe;
	}

	public void setGraphe(Graphe g) {
		graphe = g;
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

	public int supprSommet(int id) {
		coordonnees.set(id, coordonnees.get(coordonnees.size() - 1));
		coordonnees.remove(coordonnees.size() - 1);
		return graphe.supprSommet(id);
	}

	public int getId(int x, int y) {
		for (int i = 0; i < coordonnees.size(); i++) {
			if ((x <= coordonnees.get(i).getX() + DIAMETRE) &&
			    (x >= coordonnees.get(i).getX()) &&
			    (y <= coordonnees.get(i).getY() + DIAMETRE) &&
			    (y >= coordonnees.get(i).getY())) {
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

			for (int j = i; j < getGraphe().taille(); ++j) {
				if (getGraphe().getConnexion(i, j) != 0) {
					int coord_i_x = (int) (coord.get(i).getX() + diam / 2);
					int coord_i_y = (int) (coord.get(i).getY() + diam / 2);
					int coord_j_x = (int) (coord.get(j).getX() + diam / 2);
					int coord_j_y = (int) (coord.get(j).getY() + diam / 2);
					g.drawLine(coord_i_x, coord_i_y, coord_j_x, coord_j_y);
					if (getGraphe().getConnexion(i, j) > 1) {
						g.drawString(getGraphe().getConnexion(i, j) + "", // uncrustify,
						             (coord_i_x + coord_j_x) / 2, // what are you doing.
						             (coord_i_y + coord_j_y) / 2);
					}
				}
			}
		}
	}

	public String toString() {
		String res = "";

		if (coordonnees.size() == 0) return res;

		for (Point pos : coordonnees)
			res += " " + pos.getX() + ";" + pos.getY();
		
		res = res.substring(1, res.length()) + "\n"; // retrait du premier espace

		return res + graphe.toString();
	}

	public LinkedList<Point> parseCoordonnees(String inp) {
		LinkedList<Point> resultat = new LinkedList<>();

		String[] tab = inp.split(" ");
		for (String coord : tab) {
			String[] point = coord.split(";");
			int x = (int) Double.parseDouble(point[0]);
			int y = (int) Double.parseDouble(point[1]);
			resultat.add(new Point(x, y));
		}

		return resultat;
	}

	public Graphe parseGraphe(BufferedReader reader) {
		Graphe g = new Graphe();
		try {
			reader.mark(0);
			String[] ligne = (reader.readLine()).split(" ");
			reader.reset();
			int taille = ligne.length;

			for (int i = 0; i < taille; i++) {
				g.addSommet();
			}

			for (int i = 0; i < taille; i++) {
				ligne = (reader.readLine()).split(" ");
				for (int j = 0; j <= i; j++) {
					for (int k = 0; k < Integer.parseInt(ligne[j]); k++) {
						g.setConnexion(j, i, true);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return g;
	}

	public void exporter() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("graph.txt"));
			writer.write(this.toString());
			writer.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void importer() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader("graph.txt"));
			coordonnees = parseCoordonnees(reader.readLine());
			graphe = parseGraphe(reader);
			reader.close();
			repaint();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
