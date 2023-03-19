import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;

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
		select(-1);
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

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(color_bg);
		g.fillRect(0, 0, 850, 850);

		final Color color = graphe.estEulerien() || !editing ? color_default : color_unsolvable;
		g.setColor(color);

		g.setStroke(new BasicStroke(5));
		for (int i = 0; i < coords.size(); i++) {
			final var coord1_ = coords.get(i);
			final var point_coord = new Point(coord1_.x - point_radius, coord1_.y - point_radius);
			final var point = new Ellipse2D.Float(point_coord.x, point_coord.y, point_radius * 2,
			                                      point_radius * 2);
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
					for (int l = 1; l <= 10; l++) {
						final var t = l / 10.0;
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
					g.addConnections(j, i, Integer.parseInt(ligne[j]));
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
