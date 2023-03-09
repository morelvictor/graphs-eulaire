import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class Editeur extends JPanel {
	private static Image background;

	private VueGraphe vuegraphe;
	private InputHandler input_handler;

	private String pack;
	private int graphe_actuel;

	private JButton supprimer;
	private JButton suppr_all;

	private JButton exporter;
	private JButton importer;

	private JButton generer_random;
	private static Random r = new Random();
	private boolean en_generation = false;

	private int nb_sommets = 0;
	private int nb_aretes = 0;

	private JButton ajoute_sommet;
	private JButton ajoute_arete;
	private JButton enleve_sommet;
	private JButton enleve_arete;
	private JLabel sommet = new JLabel("Sommet : ");
	private JLabel arete = new JLabel("Arête : ");
	private JLabel n_sommets = new JLabel("0");
	private JLabel n_aretes = new JLabel("0");

	private JButton jouer;

	private JFrame frame;

	public Editeur(JFrame f, Image bg, String pack, int graph) {
		background = bg;
		frame = f;
		this.pack = pack;

		vuegraphe = new VueGraphe(true);
		add(vuegraphe);
		input_handler = new PlacingInputHandler();
		final var ml = new ControleurSourisEditeur();
		vuegraphe.addMouseListener(ml);
		vuegraphe.addMouseMotionListener(ml);
		graphe_actuel = graph >= 0 ? graph : get_graphe_nb();
		if (graphe_actuel < get_graphe_nb()) {
			vuegraphe.importer(pack, graphe_actuel);
		} else {
			vuegraphe.effacer();
		}

		supprimer = Utils.generate_button("suppr_sommet", e -> {
			if (input_handler instanceof DeletingInputHandler) {
				input_handler = new PlacingInputHandler();
			} else {
				input_handler = new DeletingInputHandler();
			}
			repaint();
		});
		add(supprimer);

		suppr_all = Utils.generate_button("suppr_tout", e -> {
			nb_sommets = 0;
			nb_aretes = 0;
			n_sommets.setText("0");
			n_aretes.setText("0");
			graphe_actuel = get_graphe_nb();
			vuegraphe.effacer();
			repaint();
		});
		add(suppr_all);

		exporter = Utils.generate_button("exporter", e -> {
			vuegraphe.exporter(pack, graphe_actuel);
		});
		add(exporter);

		importer = Utils.generate_button("importer", e -> {
			graphe_actuel = (graphe_actuel + 1) % (get_graphe_nb() + 1);
			if (graphe_actuel < get_graphe_nb()) {
				vuegraphe.importer(pack, graphe_actuel);
			} else {
				vuegraphe.effacer();
			}
		});
		add(importer);

		generer_random = Utils.generate_button("random", e -> {
			en_generation = !en_generation;
			if (en_generation) {
				nb_sommets = 5 + r.nextInt(20);
				nb_aretes = r.nextInt(nb_sommets);
				n_sommets.setText("" + nb_sommets);
				n_aretes.setText("" + nb_aretes);
				vuegraphe.setGraphe(getNRandomSom(nb_sommets, nb_aretes), getNRandomCoord(nb_sommets));
			}
			graphe_actuel = get_graphe_nb();
			repaint();
		});
		add(generer_random);

		ajoute_sommet = Utils.generate_button("ajoute", e -> {
			ajouteNSommets(1);
			vuegraphe.ajouteSommet(getRandomCoord());
			repaint();
		});
		add(ajoute_sommet);

		enleve_sommet = Utils.generate_button("enleve", e -> {
			if (nb_sommets > 0) {
				ajouteNSommets(-1);
				ajouteNAretes(-1 * vuegraphe.supprSommet(r.nextInt(vuegraphe.getGraphe().taille())));
			}
			repaint();
		});
		add(enleve_sommet);

		ajoute_arete = Utils.generate_button("ajoute", e -> {
			if (nb_sommets > 1) {
				ajouteNAretes(1);
				setConnexionRandom(true);
				repaint();
			}
		});
		add(ajoute_arete);

		enleve_arete = Utils.generate_button("enleve", e -> {
			ajouteNAretes(-1);
			setConnexionRandom(false);
			repaint();
		});
		add(enleve_arete);

		jouer = Utils.generate_button("jouer_editeur", e -> {
			frame.setContentPane(new Partie(frame, background, pack, vuegraphe, graphe_actuel));
			frame.revalidate();
			frame.repaint();
		});
		add(jouer);

		add(sommet);
		add(arete);
		add(n_sommets);
		add(n_aretes);
	}

	public void ajouteNSommets(int n) {
		nb_sommets += n;
		n_sommets.setText("" + nb_sommets);
	}

	public void ajouteNAretes(int n) {
		if (nb_aretes + n >= 0) {
			nb_aretes += n;
			n_aretes.setText("" + nb_aretes);
		}
	}

	public void setConnexionRandom(boolean b) {
		if (!b && vuegraphe.getGraphe().nbConnexions() < 1) {
			return;
		}
		int id1 = r.nextInt(vuegraphe.getGraphe().taille());
		while (!b && vuegraphe.getGraphe().getConnexions(id1).size() < 1) {
			id1 = r.nextInt(vuegraphe.getGraphe().taille());
		}
		int id2;
		if (!b) {
			ArrayList<Integer> l = vuegraphe.getGraphe().getConnexions(id1);
			id2 = l.get(r.nextInt(l.size()));
		} else {
			id2 = r.nextInt(vuegraphe.getGraphe().taille());
			while (id1 == id2) {
				id2 = r.nextInt(vuegraphe.getGraphe().taille());
			}
		}
		vuegraphe.getGraphe().setConnexion(id1, id2, b);
	}

	public static Point getRandomCoord() {
		return new Point(30 + r.nextInt(800), 30 + r.nextInt(800));
	}

	public static LinkedList<Point> getNRandomCoord(int n) {
		LinkedList<Point> res = new LinkedList<Point>();
		for (int i = 0; i < n; i++) {
			res.add(getRandomCoord());
		}
		return res;
	}

	public static Graphe getNRandomSom(int nb_sommets, int nb_aretes) {
		Graphe graphe = new Graphe();
		for (int i = 0; i < nb_sommets; i++) {
			graphe.addSommet();
		}
		for (int j = 0; j < nb_aretes; j++) {
			int id1 = r.nextInt(nb_sommets);
			int id2 = r.nextInt(nb_sommets);
			while (id2 == id1) {
				id2 = r.nextInt(nb_sommets);
			}
			graphe.setConnexion(id1, id2, true);
		}
		return graphe;
	}

	public int get_graphe_nb() {
		if (pack == null) {
			return (new java.io.File("../packless")).listFiles().length - 1;
		} else {
			return (new java.io.File("../packs/" + pack)).listFiles().length;
		}
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		if (input_handler instanceof DeletingInputHandler) {
			g.setColor(Color.green);
			((Graphics2D) g).draw(new Rectangle(getWidth() - 101, 49, 51, 51));
		}

		supprimer.setBounds(getWidth() - 100, 50, 50, 50);
		suppr_all.setBounds(getWidth() - 100, 110, 50, 50);

		generer_random.setBounds(getWidth() - 100, 170, 50, 50);
		ajoute_sommet.setBounds(getWidth() - 100, 230, 50, 24);
		sommet.setBounds(getWidth() - 115, 255, 75, 24);
		n_sommets.setBounds(getWidth() - 45, 255, 24, 24);
		enleve_sommet.setBounds(getWidth() - 100, 280, 50, 24);
		ajoute_arete.setBounds(getWidth() - 100, 330, 50, 24);
		arete.setBounds(getWidth() - 105, 345, 75, 24);
		n_aretes.setBounds(getWidth() - 55, 345, 24, 24);
		enleve_arete.setBounds(getWidth() - 100, 380, 50, 24);

		exporter.setBounds(getWidth() - 100, 420, 50, 50);
		importer.setBounds(getWidth() - 100, 480, 50, 50);
		jouer.setBounds(getWidth() - 120, 800, 100, 50);
	}


	public class ControleurSourisEditeur implements MouseInputListener {
		int current_vertex = -1;
		public void mouseClicked(MouseEvent e) {
			if (current_vertex == -1) {
				input_handler.on_point_click(e.getPoint());
			} else {
				input_handler.on_vertex_click(current_vertex);
			}
		}
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {
			current_vertex = vuegraphe.getId(e.getX(), e.getY());
		}
		public void mouseReleased(MouseEvent e) {}
		public void mouseDragged(MouseEvent e) {
			if (current_vertex != -1) {
				input_handler.on_vertex_drag(current_vertex, e.getPoint());
			}
		}
		public void mouseMoved(MouseEvent e) {}
	}

	private abstract class InputHandler {
		public abstract void on_vertex_click(int vertex);
		public abstract void on_point_click(Point p);
		public abstract void on_vertex_drag(int vertex, Point p);
	}
	private class PlacingInputHandler extends InputHandler {
		public void on_vertex_click(int vertex) {
			if (vuegraphe.get_selected() == -1) {
				vuegraphe.select(vertex);
			} else if (vuegraphe.get_selected() == vertex) {
				vuegraphe.select(-1);
			} else {
				vuegraphe.getGraphe().setConnexion(vuegraphe.get_selected(), vertex, true);
				vuegraphe.select(vertex);
				ajouteNAretes(1);
				repaint();
			}
		}
		public void on_point_click(Point p) {
			vuegraphe.ajouteSommet(p);
			ajouteNSommets(1);
			repaint();
		}
		public void on_vertex_drag(int vertex, Point p) {
			vuegraphe.setCoord(vertex, p.x, p.y);
		}
	}
	private class DeletingInputHandler extends InputHandler {
		int last_deleted = -1;
		public void on_vertex_click(int vertex) {
			ajouteNAretes(-vuegraphe.supprSommet(vertex));
			ajouteNSommets(-1);
			repaint();
		}
		public void on_point_click(Point p) {}
		public void on_vertex_drag(int vertex, Point p) {
			if (last_deleted == -1) {
				last_deleted = vertex;
			}
			final var other_p = vuegraphe.getId(p.x, p.y);
			if (other_p == -1 || last_deleted == other_p) {
				return;
			}
			if (vuegraphe.getGraphe().getConnexion(last_deleted, other_p) > 0) {
				ajouteNAretes(-1);
			}
			vuegraphe.getGraphe().setConnexion(last_deleted, other_p, false);
			last_deleted = other_p;
			repaint();
		}
	}
}
