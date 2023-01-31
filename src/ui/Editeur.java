import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.*;

public class Editeur extends JPanel {
	private static int DIAMETRE = 15;
	private static Color COULEUR = new Color(39, 78, 140);

	private VueGrapheEditeur vuegraphe;


	private JButton poser_sommet;
	private boolean peut_poser_sommet = false;

	private JButton lier;
	private boolean peut_lier = false;
	//indice dans la liste sommets du sommet à lier
	private int a_lier = -1;

	private JButton suppr_som;
	private boolean peut_suppr = false;

	private JButton suppr_all;

	private JButton generer_random;
	private static Random r = new Random();


	public Editeur() {

		vuegraphe = new VueGrapheEditeur(COULEUR, DIAMETRE, this, new ControleurSourisEditeur());
		vuegraphe.setBounds(20, 20, 800, 800);
		add(vuegraphe);

		poser_sommet = new JButton("o");
		poser_sommet.setBounds(900, 50, 50, 50);
		add(poser_sommet);

		poser_sommet.addActionListener(
			(ActionEvent e) -> {
			peut_poser_sommet = !peut_poser_sommet;
			peut_lier = false;
			peut_suppr = false;
			repaint();
		});


		lier = new JButton("x");
		lier.setBounds(900, 110, 50, 50);
		add(lier);

		lier.addActionListener(
			(ActionEvent e) -> {
			peut_lier = !peut_lier;
			peut_poser_sommet = false;
			peut_suppr = false;
			a_lier = -1;
			repaint();
		});


		suppr_som = new JButton("☒");
		suppr_som.setBounds(900, 170, 50, 50);
		add(suppr_som);

		suppr_som.addActionListener(
			(ActionEvent e) -> {
			peut_suppr = !peut_suppr;
			peut_poser_sommet = false;
			peut_lier = false;
			repaint();
			});


		suppr_all = new JButton("⟲");
		suppr_all.setBounds(900, 230, 50, 50);
		add(suppr_all);

		suppr_all.addActionListener(
			(ActionEvent e) -> {
			vuegraphe.viderGraphe();
			repaint();
		});
		repaint();


		generer_random = new JButton("?");
		generer_random.setBounds(900, 290, 50, 50);
		add(generer_random);


		generer_random.addActionListener(
			(ActionEvent e) -> {
			int taille = r.nextInt(20);
			int proba = r.nextInt(10);
			vuegraphe.setGraphe(getNRandomSom(taille, proba), getNRandomCoord(taille));
			repaint();
		});

	}

	public static LinkedList<Point> getNRandomCoord(int n) {
		LinkedList<Point> res = new LinkedList<Point>();
		for (int i = 0; i < n; i++) {
			res.add(new Point(30 + r.nextInt(800), 30 + r.nextInt(800)));
		}
		return res;
	}

	public static Graphe getNRandomSom(int n, int inverse_proba_arete) {
		Graphe graphe = new Graphe();
		for (int i = 0; i < n; i++) {
			graphe.addSommet();
		}
		for (int id = 0; id < n; id++) {
			for (int j = id + 1; j < n; j++) {
				if (r.nextInt(inverse_proba_arete + 1) == 1) {
					graphe.setConnexion(id, j, true);
				}
			}
		}
		return graphe;
	}


	public int getALier() {
		return a_lier;
	}

	public void setALier(int n) {
		a_lier = n;
	}

	public boolean getPeutPoserSommet() {
		return peut_poser_sommet;
	}

	public boolean getPeutLier() {
		return peut_lier;
	}

	public void paintComponent(Graphics g) {
		g.setColor(COULEUR);
		if(peut_poser_sommet){
			g.setColor(Color.green);
		}
		((Graphics2D) g).draw(new Rectangle(getWidth() - 101, 49, 51, 51));
		g.setColor(COULEUR);
		if(peut_lier){
			g.setColor(Color.green);
		}
		((Graphics2D) g).draw(new Rectangle(getWidth() - 101, 109, 51, 51));
		g.setColor(COULEUR);
		if(peut_suppr){
			g.setColor(Color.green);
		}
		((Graphics2D) g).draw(new Rectangle(getWidth() - 101, 169, 51, 51));
		g.setColor(COULEUR);
		poser_sommet.setBounds(getWidth() - 100, 50, 50, 50);
		lier.setBounds(getWidth() - 100, 110, 50, 50);
		suppr_som.setBounds(getWidth() - 100, 170, 50, 50);
		suppr_all.setBounds(getWidth() - 100, 230, 50, 50);
		generer_random.setBounds(getWidth() - 100, 290, 50, 50);
	}


	public class ControleurSourisEditeur implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (getPeutPoserSommet()) {
				vuegraphe.ajouteSommet(new Point(e.getX() - 7, e.getY() - 7));
			}
			int id = vuegraphe.getId(e.getX(), e.getY());
			if (getPeutLier() && id >= 0) {
				if (getALier() == -1) {
					setALier(id);
				} else {
					vuegraphe.getGraphe().setConnexion(getALier(), id, true);
					setALier(-1);
				}
			}
			if(peut_suppr && id >= 0) {
				vuegraphe.getGraphe().supprSommet(id);
			}
			repaint();
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
}
