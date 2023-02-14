import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import javax.swing.*;

public class Editeur extends JPanel {
	private static int DIAMETRE = 15;
	private static Color COULEUR = new Color(39, 78, 140);
	private static Image background;

	private VueGrapheEditeur vuegraphe;


	private JButton poser_sommet;
	private boolean peut_poser_sommet = false;

	private JButton lier;
	private boolean peut_lier = false;
	//indice dans la liste sommets du sommet à lier
	private int a_lier = -1;

	private JButton deplacer_som;
	private boolean en_deplacement = false;
	private int a_deplacer = -1;

	private JButton suppr_som;
	private boolean peut_suppr = false;

	private JButton suppr_all;

	private int graphe_actuel = vuegraphe.get_n_graphe();
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

	private JButton jouer = new JButton(new ImageIcon("../files/textures/jouer_editeur.png"));

	private JFrame frame;

	public Editeur(JFrame f, Image bg) {
		background = bg;
		frame = f;

		vuegraphe = new VueGrapheEditeur(COULEUR, DIAMETRE, this, new ControleurSourisEditeur());
		add(vuegraphe);

		poser_sommet = new JButton(new ImageIcon("../files/textures/poser_sommet.png"));
		poser_sommet.setBorderPainted(false);
		poser_sommet.setContentAreaFilled(false);
		poser_sommet.setFocusPainted(false);
		add(poser_sommet);

		poser_sommet.addActionListener((ActionEvent e) -> {
			peut_poser_sommet = !peut_poser_sommet;
			peut_lier = false;
			peut_suppr = false;
			en_deplacement = false;
			repaint();
		});

		lier = new JButton(new ImageIcon("../files/textures/lier_sommet.png"));
		lier.setBorderPainted(false);
		lier.setContentAreaFilled(false);
		lier.setFocusPainted(false);
		add(lier);

		lier.addActionListener((ActionEvent e) -> {
			peut_lier = !peut_lier;
			peut_poser_sommet = false;
			peut_suppr = false;
			en_generation = false;
			en_deplacement = false;
			a_lier = -1;
			repaint();
		});


		suppr_som = new JButton(new ImageIcon("../files/textures/suppr_sommet.png"));
		suppr_som.setBorderPainted(false);
		suppr_som.setContentAreaFilled(false);
		suppr_som.setFocusPainted(false);
		add(suppr_som);

		suppr_som.addActionListener((ActionEvent e) -> {
			peut_suppr = !peut_suppr;
			peut_poser_sommet = false;
			peut_lier = false;
			en_generation = false;
			en_deplacement = false;
			repaint();
		});

		deplacer_som = new JButton(new ImageIcon("../files/textures/deplacer_sommet.png"));
		deplacer_som.setBorderPainted(false);
		deplacer_som.setContentAreaFilled(false);
		deplacer_som.setFocusPainted(false);
		add(deplacer_som);

		deplacer_som.addActionListener((ActionEvent e) -> {
			en_deplacement = !en_deplacement;
			peut_poser_sommet = false;
			peut_lier = false;
			peut_suppr = false;
			a_deplacer = -1;
			repaint();
		});

		suppr_all = new JButton(new ImageIcon("../files/textures/suppr_tout.png"));
		suppr_all.setBorderPainted(false);
		suppr_all.setContentAreaFilled(false);
		suppr_all.setFocusPainted(false);
		add(suppr_all);

		suppr_all.addActionListener((ActionEvent e) -> {
			nb_sommets = 0;
			nb_aretes = 0;
			n_sommets.setText("0");
			n_aretes.setText("0");
			vuegraphe.viderGraphe();
			repaint();
		});

		exporter = new JButton(new ImageIcon("../files/textures/exporter.png"));
		exporter.setBorderPainted(false);
		exporter.setContentAreaFilled(false);
		exporter.setFocusPainted(false);
		add(exporter);

		exporter.addActionListener((ActionEvent e) -> {
			vuegraphe.exporter(graphe_actuel);
		});

		importer = new JButton(new ImageIcon("../files/textures/importer.png"));
		importer.setBorderPainted(false);
		importer.setContentAreaFilled(false);
		importer.setFocusPainted(false);
		add(importer);

		importer.addActionListener((ActionEvent e) -> {
			if (graphe_actuel < vuegraphe.get_n_graphe()) {
				graphe_actuel++;
			} else {
				graphe_actuel = 1;
			}
			vuegraphe.importer(graphe_actuel);
		});

		generer_random = new JButton(new ImageIcon("../files/textures/random.png"));
		generer_random.setBorderPainted(false);
		generer_random.setContentAreaFilled(false);
		generer_random.setFocusPainted(false);
		add(generer_random);


		generer_random.addActionListener((ActionEvent e) -> {
			en_generation = !en_generation;
			ajoute_sommet.setEnabled(en_generation);
			ajoute_arete.setEnabled(en_generation);
			enleve_sommet.setEnabled(en_generation);
			enleve_arete.setEnabled(en_generation);
			peut_suppr = false;
			peut_lier = false;
			peut_poser_sommet = false;
			if (en_generation) {
				nb_sommets = 5 + r.nextInt(20);
				nb_aretes = r.nextInt(nb_sommets);
				n_sommets.setText("" + nb_sommets);
				n_aretes.setText("" + nb_aretes);
				vuegraphe.setGraphe(getNRandomSom(nb_sommets, nb_aretes), getNRandomCoord(nb_sommets));
			}
			repaint();
		});

		ajoute_sommet = new JButton(new ImageIcon("../files/textures/ajoute.png"));
		ajoute_sommet.setBorderPainted(false);
		ajoute_sommet.setContentAreaFilled(false);
		ajoute_sommet.setFocusPainted(false);
		ajoute_sommet.setEnabled(false);
		add(ajoute_sommet);

		ajoute_sommet.addActionListener((ActionEvent e) -> {
			ajouteNSommets(1);
			vuegraphe.ajouteSommet(getRandomCoord());
			repaint();
		});

		enleve_sommet = new JButton(new ImageIcon("../files/textures/enleve.png"));
		enleve_sommet.setBorderPainted(false);
		enleve_sommet.setContentAreaFilled(false);
		enleve_sommet.setFocusPainted(false);
		enleve_sommet.setEnabled(false);
		add(enleve_sommet);

		enleve_sommet.addActionListener((ActionEvent e) -> {
			if (nb_sommets > 0) {
				ajouteNSommets(-1);
				ajouteNAretes(-1 * vuegraphe.supprSommet(r.nextInt(vuegraphe.getGraphe().taille())));
			}
			repaint();
		});

		ajoute_arete = new JButton(new ImageIcon("../files/textures/ajoute.png"));
		ajoute_arete.setBorderPainted(false);
		ajoute_arete.setContentAreaFilled(false);
		ajoute_arete.setFocusPainted(false);
		ajoute_arete.setEnabled(false);
		add(ajoute_arete);

		ajoute_arete.addActionListener((ActionEvent e) -> {
			if (nb_sommets > 1) {
				ajouteNAretes(1);
				setConnexionRandom(true);
				repaint();
			}
		});

		enleve_arete = new JButton(new ImageIcon("../files/textures/enleve.png"));
		enleve_arete.setBorderPainted(false);
		enleve_arete.setContentAreaFilled(false);
		enleve_arete.setFocusPainted(false);
		enleve_arete.setEnabled(false);
		add(enleve_arete);

		enleve_arete.addActionListener((ActionEvent e) -> {
			ajouteNAretes(-1);
			setConnexionRandom(false);
			repaint();
		});

		jouer.addActionListener((ActionEvent e) -> {
			frame.setContentPane(new Partie(vuegraphe, background));
			frame.revalidate();
			frame.repaint();
		});
		jouer.setBorderPainted(false);
		jouer.setContentAreaFilled(false);
		jouer.setFocusPainted(false);

		add(sommet);
		add(arete);
		add(n_sommets);
		add(n_aretes);
		add(jouer);
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


	public int getALier() {
		return a_lier;
	}

	public boolean getPeutLier() {
		return peut_lier;
	}

	public void setALier(int n) {
		a_lier = n;
	}

	public int getADeplacer() {
		return a_deplacer;
	}

	public boolean getEnDeplacement() {
		return en_deplacement;
	}

	public void setADeplacer(int n) {
		a_deplacer = n;
	}

	public boolean getPeutPoserSommet() {
		return peut_poser_sommet;
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		g.setColor(Color.WHITE);
		g.fillRect(getWidth() - 45, 435, 20, 24);
		g.fillRect(getWidth() - 55, 535, 20, 24);
		g.setColor(COULEUR);
		if (peut_poser_sommet) {
			g.setColor(Color.green);
			((Graphics2D) g).draw(new Rectangle(getWidth() - 101, 49, 51, 51));
		}
		g.setColor(COULEUR);
		if (peut_lier) {
			g.setColor(Color.green);
			((Graphics2D) g).draw(new Rectangle(getWidth() - 101, 109, 51, 51));
		}
		g.setColor(COULEUR);
		if (peut_suppr) {
			g.setColor(Color.green);
			((Graphics2D) g).draw(new Rectangle(getWidth() - 101, 169, 51, 51));
		}
		g.setColor(COULEUR);
		if (en_deplacement) {
			g.setColor(Color.green);
			((Graphics2D) g).draw(new Rectangle(getWidth() - 101, 229, 51, 51));
		}
		g.setColor(COULEUR);

		poser_sommet.setBounds(getWidth() - 100, 50, 50, 50);
		lier.setBounds(getWidth() - 100, 110, 50, 50);
		suppr_som.setBounds(getWidth() - 100, 170, 50, 50);
		deplacer_som.setBounds(getWidth() - 100, 230, 50, 50);

		suppr_all.setBounds(getWidth() - 100, 290, 50, 50);

		generer_random.setBounds(getWidth() - 100, 350, 50, 50);
		ajoute_sommet.setBounds(getWidth() - 100, 410, 50, 24);
		sommet.setBounds(getWidth() - 115, 435, 75, 24);
		n_sommets.setBounds(getWidth() - 45, 435, 24, 24);
		enleve_sommet.setBounds(getWidth() - 100, 460, 50, 24);
		ajoute_arete.setBounds(getWidth() - 100, 510, 50, 24);
		arete.setBounds(getWidth() - 105, 535, 75, 24);
		n_aretes.setBounds(getWidth() - 55, 535, 24, 24);
		enleve_arete.setBounds(getWidth() - 100, 560, 50, 24);

		exporter.setBounds(getWidth() - 100, 600, 50, 50);
		importer.setBounds(getWidth() - 100, 660, 50, 50);
		jouer.setBounds(getWidth() - 120, 800, 100, 50);
	}


	public class ControleurSourisEditeur implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (getPeutPoserSommet()) {
				ajouteNSommets(1);
				vuegraphe.ajouteSommet(new Point(e.getX() - DIAMETRE / 2, e.getY() - DIAMETRE / 2));
			}
			int id = vuegraphe.getId(e.getX(), e.getY());
			if (getPeutLier() && id >= 0) {
				if (getALier() == -1) {
					setALier(id);
				} else {
					if (getALier() != id) {
						ajouteNAretes(1);
						vuegraphe.getGraphe().setConnexion(getALier(), id, true);
						setALier(-1);
					}
				}
			}
			if (peut_suppr && id >= 0) {
				ajouteNSommets(-1);
				ajouteNAretes(-1 * vuegraphe.supprSommet(id));
			}

			if (getEnDeplacement()) {
				if (getADeplacer() == -1) {
					setADeplacer(id);
				} else {
					vuegraphe.setCoordonnees(a_deplacer, e.getX() - DIAMETRE / 2,
					                         e.getY() - DIAMETRE / 2);
					setADeplacer(-1);
				}
			}
			repaint();
		}

		public void mouseEntered(MouseEvent e) {
		}
		public void mouseExited(MouseEvent e) {
		}
		public void mousePressed(MouseEvent e) {
		}
		public void mouseReleased(MouseEvent e) {
		}
	}
}
