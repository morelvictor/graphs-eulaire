import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.event.MouseInputListener;

public class Partie extends JPanel {
	VueGraphe g;
	Graphe current_g;
	LinkedList<Point> current_c;

	Image background;
	JButton regenerer;
	JButton editeur;
	JLabel timer;
	JLabel aides;
	JLabel pack_actuel;
	String packname;
	boolean omega = false;
	JButton aide;
	JTextArea nomjoueur;
	JButton save_score;
	JButton congrats;
	private ArrayList<Integer> solution;
	private int indice_solution = 0;
	private int nb_aide = 0;
	boolean testing_editing;
	long debutTimer;

	private static class Level {
		public Level(String pack, int n) {
			this.pack = pack;
			this.n = n;
		}
		public String pack;
		public int n;
	}

	private java.util.ArrayList<Level> levels = new java.util.ArrayList<Level>();
	private int current_level = 0;

	private void loadPack(String pack) {
		if (pack == null) {
			for (var p : (new java.io.File("../packs")).listFiles()) {
				loadPack(p.getName());
			}
		} else {
			int n = (new java.io.File("../packs/" + pack)).listFiles().length;
			for (int i = 0; i < n; i++) {
				levels.add(new Level(pack, i));
			}
		}
	}

	public Partie(JFrame frame, Image bg, String pack, VueGraphe vg, int level) {
		packname = pack;
		if (pack == null) {
			omega = true;
		}

		loadPack(pack);
		if (levels.size() == 0) {
			System.err.println("No levels in pack " + (pack == null ? "Ω" : pack) + ".");
			System.exit(1);
		}
		current_level = level;
		background = bg;

		MouseInputListener ml = new MouseInputListener() {
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				next_point(e);
			}
			public void mouseMoved(MouseEvent e) {}
			public void mouseDragged(MouseEvent e) {
				next_point(e);
			}
		};
		if (vg != null) {
			g = vg;
			g.set_editing(false);
			for (var l : g.getMouseListeners()) {
				g.removeMouseListener(l);
			}
			for (var l : g.getMouseMotionListeners()) {
				g.removeMouseMotionListener(l);
			}
		} else {
			g = new VueGraphe(false);
		}
		g.select(-1);
		add(g);
		g.addMouseListener(ml);
		g.addMouseMotionListener(ml);

		editeur = Utils.generate_button("jeu-editeur", e -> {
			reset();
			frame.setContentPane(new Editeur(frame, background, pack, g, current_level));
			frame.revalidate();
			frame.repaint();
		});

		regenerer = Utils.generate_button("retry", e -> {
			indice_solution = 0;
			g.setGraphe(current_g, current_c);
			g.select(-1);
			update_current();
		});

		aide = Utils.generate_button("aide_jeu", e -> {
			next_point(solution.get(indice_solution++));
			nb_aide++;
		});

		add(regenerer);
		add(aide);
		testing_editing = vg != null;
		if (testing_editing) {
			add(editeur);
		} else {
			g.importer(pack, current_level);
			g.select(-1);
		}

		update_current();
		solution = g.getGraphe().hierholzer();
		debutTimer = System.currentTimeMillis();
		timer =
			new JLabel("TEMPS : " +
			           Double.toString(((double)(System.currentTimeMillis() - debutTimer)) / 1000.0));
		timer.setFont(new Font("Serif", Font.PLAIN, 20));
		add(timer);
		aides = new JLabel("Nombre d'Aides : " + nb_aide);
		aides.setFont(new Font("Serif", Font.PLAIN, 20));
		add(aides);
		pack_actuel = new JLabel("Pack : " + packname);
		pack_actuel.setFont(new Font("Serif", Font.PLAIN, 20));
		add(pack_actuel);

	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		regenerer.setBounds(getWidth() - 120, 710, 90, 50);
		editeur.setBounds(getWidth() - 120, 800, 90, 50);
		aide.setBounds(getWidth() - 120, 620, 90, 50);
		pack_actuel.setBounds(getWidth() - 300, 250, 200, 200);
		timer.setBounds(getWidth() - 300, 280, 200, 200);
		aides.setBounds(getWidth() - 300, 310, 300, 200);
		aides.setText("Nombre d'Aides : " + nb_aide);
		pack_actuel.setText("Pack : " + packname);
	}

	public void finDePartie() {
		if (testing_editing) {
			reset();
			return;
		}
		if (current_level + 1 < levels.size()) {
			suivant();
			return;
		}
		remove(g);
		remove(regenerer);
		remove(aide);

		setLayout(null);


		double temps = (System.currentTimeMillis() - debutTimer) / 1000.0;
		double score_ = Math.floor((temps + 3 * nb_aide) * 1000) / 1000;
		JLabel score =
			new JLabel("VOTRE SCORE : " + score_);
		score.setFont(new Font("Serif", Font.PLAIN, 20));
		score.setBounds(getWidth() - 300, 360, 400, 200);

		nomjoueur = new JTextArea("Entrez votre nom");
		nomjoueur.setBounds(getWidth() - 300, 500, 120, 15);

		Classement classement;
		if (omega) {
			classement = new Classement("null");
		} else {
			classement = new Classement(packname);
		}
		classement.setBounds(getWidth() - 550, 360, 200, 500);

		save_score = Utils.generate_button("save", e -> {
			String tmp = nomjoueur.getText();
			classement.ajouteScore(tmp, score_);
			repaint();
			score_ajoute(classement);
		});
		save_score.setBounds(getWidth() - 100, 500, 90, 15);

		JButton congrats = new JButton(new ImageIcon("../textures/retry.png"));
		congrats.setBorderPainted(false);
		congrats.setContentAreaFilled(false);
		congrats.setFocusPainted(false);
		congrats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				suivant();
				add(g);
				add(regenerer);
				add(aide);
				remove(congrats);
				remove(classement);
				remove(nomjoueur);
				remove(save_score);
				remove(score);
				validate();
				repaint();
				debutTimer = System.currentTimeMillis();
				timer.setText("TEMPS : " +
				              Double.toString(((double)(System.currentTimeMillis() - debutTimer)) /
				                              1000.0));
				nb_aide = 0;
			}
		});
		congrats.setBounds(getWidth() / 2, 500, 90, 50);


		add(save_score);
		add(nomjoueur);
		add(classement);
		add(congrats);
		add(score);
		validate();
		repaint();
	}

	private void score_ajoute(Classement classement) {
		remove(nomjoueur);
		remove(save_score);
		classement.exporter(classement.getNom(), classement.serialise());
	}

	private void next_point(int point) {
		if (point == -1) {
			return;
		}
		if (g.get_selected() == -1) {
			g.select(point);
		} else if (g.getGraphe().getConnexion(g.get_selected(), point) != 0) {
			g.getGraphe().addConnections(g.get_selected(), point, -1);
			g.select(point);
			if (estFinie()) {
				finDePartie();
			}
		}
	}

	private void next_point(MouseEvent e) {
		int n = g.getId(e.getX(), e.getY());
		next_point(n);
	}

	private void next_point(Point p) {
		int n = g.getId((int) p.getX(), (int) p.getY());
		next_point(n);
	}

	public boolean estFinie() {
		return !g.getGraphe().hasConnexions(); // on peut aussi tester si la partie ne peut plus être gagnée
	}

	public void suivant() {
		current_level = (current_level + 1) % levels.size();
		final var lvl = levels.get(current_level);
		g.importer(lvl.pack, lvl.n);
		solution = g.getGraphe().hierholzer();
		indice_solution = 0;
		g.select(-1);
		update_current();
		packname = lvl.pack;
		timer.setText("TEMPS : " +
		              Double.toString(((double)(System.currentTimeMillis() - debutTimer)) / 1000.0));
	}

	private void update_current() {
		current_g = g.getGraphe();
		current_c = g.getCoords();
		reset();
	}
	private void reset() {
		g.setGraphe(current_g.clone(), current_c);
		g.select(-1);
	}
}
