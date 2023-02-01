import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.*;
import java.util.ArrayList;

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
	private boolean en_generation = false;

	private int nb_sommets = 5 + r.nextInt(20);
	private int nb_aretes = r.nextInt(nb_sommets);

	private JButton ajoute_sommet;
	private JButton ajoute_arete;
	private JButton enleve_sommet;
	private JButton enleve_arete;
	private JLabel sommet = new JLabel("Sommet");
	private JLabel arete = new JLabel("Arête");

	public Editeur() {

		vuegraphe = new VueGrapheEditeur(COULEUR, DIAMETRE, this, new ControleurSourisEditeur());
		add(vuegraphe);

		poser_sommet = new JButton("o");
		add(poser_sommet);

		poser_sommet.addActionListener(
			(ActionEvent e) -> {
			peut_poser_sommet = !peut_poser_sommet;
			peut_lier = false;
			peut_suppr = false;
			en_generation = false;
			repaint();
		});


		lier = new JButton("x");
		add(lier);

		lier.addActionListener(
			(ActionEvent e) -> {
			peut_lier = !peut_lier;
			peut_poser_sommet = false;
			peut_suppr = false;
			en_generation = false;
			a_lier = -1;
			repaint();
		});


		suppr_som = new JButton("☒");
		add(suppr_som);

		suppr_som.addActionListener(
			(ActionEvent e) -> {
			peut_suppr = !peut_suppr;
			peut_poser_sommet = false;
			peut_lier = false;
			en_generation = false;
			repaint();
			});


		suppr_all = new JButton("⟲");
		add(suppr_all);

		suppr_all.addActionListener(
			(ActionEvent e) -> {
			nb_sommets = 0;
			nb_aretes = 0;
			vuegraphe.viderGraphe();
			repaint();
		});


		generer_random = new JButton("?");
		add(generer_random);


		generer_random.addActionListener(
			(ActionEvent e) -> {
			en_generation = !en_generation;
			ajoute_sommet.setEnabled(en_generation);
			ajoute_arete.setEnabled(en_generation);
			enleve_sommet.setEnabled(en_generation);
			enleve_arete.setEnabled(en_generation);
			peut_suppr = false;
			peut_lier = false;
			peut_poser_sommet = false;
			if(en_generation){
				vuegraphe.setGraphe(getNRandomSom(nb_sommets, nb_aretes), getNRandomCoord(nb_sommets));
			}
			repaint();
		});

		ajoute_sommet = new JButton("+");
		ajoute_sommet.setEnabled(false);
		add(ajoute_sommet);

		ajoute_sommet.addActionListener(
			(ActionEvent e) -> {
			nb_sommets++;
			vuegraphe.ajouteSommet(getRandomCoord());
			repaint();
		});

		enleve_sommet = new JButton("-");
		enleve_sommet.setEnabled(false);
		add(enleve_sommet);

		enleve_sommet.addActionListener(
			(ActionEvent e) -> {
			if(nb_sommets > 0){
				nb_sommets--;
				nb_aretes -= vuegraphe.supprSommet(r.nextInt(vuegraphe.getGraphe().taille()));
			}
			repaint();
		});

		ajoute_arete = new JButton("+");
		ajoute_arete.setEnabled(false);
		add(ajoute_arete);

		ajoute_arete.addActionListener(
			(ActionEvent e) -> {
			nb_aretes++;
			setConnexionRandom(true);
			repaint();
		});

		enleve_arete = new JButton("-");
		enleve_arete.setEnabled(false);
		add(enleve_arete);

		enleve_arete.addActionListener(
			(ActionEvent e) -> {
			nb_aretes--;
			setConnexionRandom(false);
			repaint();
		});

		add(sommet);
		add(arete);


	}

	public void setConnexionRandom(boolean b){
		if(!b && vuegraphe.getGraphe().nbConnexions() < 1){
			return;
		}
		int id1 = r.nextInt(vuegraphe.getGraphe().taille());
		while(!b && vuegraphe.getGraphe().getConnexions(id1).size()<1){
			id1 = r.nextInt(vuegraphe.getGraphe().taille());
		}
		int id2;
		if(!b){
			ArrayList<Integer> l = vuegraphe.getGraphe().getConnexions(id1);
			id2 = l.get(r.nextInt(l.size()));
		}
		else{
			id2 = r.nextInt(vuegraphe.getGraphe().taille());
			while(id1 == id2){
				id2 = r.nextInt(vuegraphe.getGraphe().taille());
			}
		}
		vuegraphe.getGraphe().setConnexion(id1, id2, b);
	}

	public static Point getRandomCoord(){
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
			while(id2 == id1){
				id2 = r.nextInt(nb_sommets);
			}
			graphe.setConnexion(id1, id2, true);
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
		ajoute_sommet.setBounds(getWidth() - 100, 350, 50, 24);
		sommet.setBounds(getWidth() - 105, 375, 75, 24);
		enleve_sommet.setBounds(getWidth() - 100, 400, 50, 24);
		ajoute_arete.setBounds(getWidth() - 100, 450, 50, 24);
		arete.setBounds(getWidth() - 95, 475, 75, 24);
		enleve_arete.setBounds(getWidth() - 100, 500, 50, 24);
	}


	public class ControleurSourisEditeur implements MouseListener {
		public void mouseClicked(MouseEvent e) {
			if (getPeutPoserSommet()) {
				vuegraphe.ajouteSommet(new Point(e.getX() - DIAMETRE/2, e.getY() - DIAMETRE/2));
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
				nb_sommets--;
				nb_aretes -= vuegraphe.supprSommet(id);
			}
			repaint();
		}

		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
	}
}
