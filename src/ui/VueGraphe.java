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

public class VueGraphe extends JComponent {
	private final Color color_bg = Color.WHITE;
	private final Color color_default = Color.BLUE;
	private final Color color_unsolvable = Color.RED;
	private final Color color_selected = Color.GREEN;
	private final int point_radius = 8;

	private boolean editing;

	private Graphe graphe;
	private LinkedList<Point> coords;
	private int selected = -1;

	public VueGraphe(boolean editing) {
		this.editing = editing;
		setPreferredSize(new Dimension(850, 850));
		setBorder(BorderFactory.createLineBorder(Color.black));
		effacer();
	}

	public void set_editing(boolean v) {
		editing = v;
	}

	public Graphe getGraphe() {
		return graphe;
	}
	public LinkedList<Point> getCoords() {
		return coords;
	}
	// g.taille() doit valoir coords.size()
	public void setGraphe(Graphe graphe, LinkedList<Point> coords) {
		this.graphe = graphe;
		this.coords = coords;
		repaint();
	}

	public void ajouteSommet(Point p) {
		graphe.addSommet();
		coords.add(p);
		repaint();
	}

	public Point getCoord(int i) {
		return coords.get(i);
	}
	public void setCoord(int i, int x, int y) {
		coords.set(i, new Point(x, y));
		repaint();
	}

	public void effacer() {
		graphe = new Graphe();
		coords = new LinkedList<Point>();
		repaint();
	}

	public void supprSommet(int id) {
		coords.set(id, coords.get(coords.size() - 1));
		coords.remove(coords.size() - 1);
		graphe.supprSommet(id);
		repaint();
	}

	// select(-1) pour déselectionner
	public void select(int i) {
		selected = i;
		repaint();
	}
	// -1 si aucun sélectionné
	public int get_selected() {
		return selected;
	}

	public int getId(int x, int y) {
		for (int i = 0; i < coords.size(); i++) {
			final var point = coords.get(i);
			final var dist_sq = Math.pow(point.x - x, 2) + Math.pow(point.y - y, 2);
			if (dist_sq < Math.pow(point_radius, 2)) {
				return i;
			}
		}
		return -1;
	}

	public void paintComponent(Graphics g_) {
		final var g = ((Graphics2D) g_);

		g.setColor(color_bg);
		g.fillRect(0, 0, 850, 850);

		final Color color = graphe.estEulerien() || !editing ? color_default : color_unsolvable;
		g.setColor(color);

		for (int i = 0; i < coords.size(); i++) {
			final var coord1 = coords.get(i);
			final var point_coord = new Point(coord1.x - point_radius, coord1.y - point_radius);
			final var point = new Ellipse2D.Float(point_coord.x, point_coord.y, point_radius * 2, point_radius * 2);
			if (i == selected) {
				g.setColor(color_selected);
				g.draw(point);
				g.setColor(color);
			} else {
				g.draw(point);
			}

			for (int j = i; j < getGraphe().taille(); ++j) {
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


	public String serialise() {
		String res = "";

		if (coords.size() == 0) {
			return res;
		}

		for (Point pos : coords) {
			res += " " + pos.getX() + ";" + pos.getY();
		}
		res = res.substring(1, res.length()) + "\n"; // retrait du premier espace

		return res + graphe.toString();
	}

	public LinkedList<Point> deserialiseCoords(String inp) {
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

	public Graphe deserialiseConnections(BufferedReader reader) {
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

	public void exporter(String pack, int n) {
		try {
			BufferedWriter writer;
			if (pack == null) {
				writer = new BufferedWriter(new FileWriter("../packless/" + n + ".mzr"));
			} else {
				writer = new BufferedWriter(new FileWriter("../packs/" + pack + "/" + n + ".mzr"));
			}
			writer.write(serialise());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void importer(String pack, int n) {
		try {
			BufferedReader reader;
			if (pack == null) {
				reader = new BufferedReader(new FileReader("../packless/" + n + ".mzr"));
			} else {
				reader = new BufferedReader(new FileReader("../packs/" + pack + "/" + n + ".mzr"));
			}
			final var line = reader.readLine();
			if (line == null) {
				effacer();
				reader.close();
				return;
			}
			coords = deserialiseCoords(line);
			graphe = deserialiseConnections(reader);
			reader.close();
			repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
