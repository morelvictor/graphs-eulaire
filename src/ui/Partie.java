import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.event.MouseInputListener;

public class Partie extends JPanel {
	VueGraphe g;
	Graphe current_g;
	LinkedList<Point> current_c;

	Font font;
	Image background;
	JButton regenerer;
	JButton editeur;
	JLabel timer;
	JLabel aides;
	JLabel pack_actuel;
	String packname;
	boolean omega;
	JButton aide;
	JTextArea nomjoueur;
	JButton save_score;
	JButton congrats;
	private int nb_aide = 0;
	boolean testing_editing;
	long debutTimer;
	boolean en_memory;

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

	private int get_graphe_nb(String pack) {
		final var files = new java.io.File("../packs/" + pack).listFiles();
		int n = 0;
		for (var f : files) {
			if (f.getName().endsWith(".mzr")) {
				n++;
			}
		}
		return n;
	}

	private void loadPack(String pack) {
		if (pack == null) {
			for (var p : (new java.io.File("../packs")).listFiles()) {
				if (!p.getName().equals("aa-tutorial")) {
					loadPack(p.getName());
				}
			}
		} else {
			int n = get_graphe_nb(pack);
			for (int i = 0; i < n; i++) {
				levels.add(new Level(pack, i));
			}
		}
	}

	public Partie(App app, Image bg, String pack, VueGraphe vg, int level, Font font) {
		setFocusable(true);
		SwingUtilities.invokeLater(() -> {
			requestFocus();
		});
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ev) {
				if (ev.getKeyCode() == app.settings.retryKey.get()) {
					regenerer.doClick();
				} else if (ev.getKeyCode() == app.settings.menuKey.get()) {
					app.frame.setContentPane(new Menu(app));
					app.frame.revalidate();
					app.frame.repaint();
				} else if (ev.getKeyCode() == app.settings.evidentMoveKey.get()) {
					if (g.estMemory() || g.get_selected() == -1) {
						return;
					}
					var moves = g.getGraphe().getConnexions(g.get_selected());
					if (moves.size() == 1) {
						next_point(moves.get(0));
					}
				}
			}
		});

		this.font = font;
		packname = pack != null ? pack : "ALL";
		omega = pack == null;

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
			g.setModeGraphique(new GraphiqueDefaut(g.getModeGraphique().image_bg));
			for (var l : g.getMouseListeners()) {
				g.removeMouseListener(l);
			}
			for (var l : g.getMouseMotionListeners()) {
				g.removeMouseMotionListener(l);
			}
		} else {
			g = new VueGraphe(new GraphiqueDefaut(null));
		}
		g.select(-1);
		add(g);
		g.addMouseListener(ml);
		g.addMouseMotionListener(ml);

		editeur = Utils.generate_button("jeu-editeur", e -> {
			reset();
			app.frame.setContentPane(new Editeur(app, background, pack, g, current_level, font));
			app.frame.revalidate();
			app.frame.repaint();
		});

		regenerer = Utils.generate_button("retry", e -> {
			g.setGraphe(current_g, current_c);
			g.select(-1);
			setMemory(false);
			update_current();
		});

		aide = Utils.generate_button("aide_jeu", e -> {
			if (!en_memory) {
				if (g.get_selected() == -1) {
					next_point(g.getGraphe().hierholzer().get(0));
				} else {
					var solution = g.getGraphe().hierholzer_from(g.get_selected());
					if (solution.size() >= 2) {
						next_point(solution.get(1));
					}
				}
			}
			nb_aide++;
			setMemory(false);
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
		debutTimer = System.currentTimeMillis();
		setMemory(false);
		timer =
			new JLabel("TEMPS : " +
			           Double.toString(((double)(System.currentTimeMillis() - debutTimer)) / 1000.0));
		timer.setFont(font);
		add(timer);
		aides = new JLabel("Nombre d'Aides : " + nb_aide);
		aides.setFont(font);
		add(aides);
		pack_actuel = new JLabel("PACK : " + packname);
		pack_actuel.setFont(font);
		add(pack_actuel);

	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		regenerer.setBounds(getWidth() - 120, 710, 90, 50);
		editeur.setBounds(getWidth() - 200, 800, 90, 50);
		aide.setBounds(getWidth() - 120, 620, 90, 50);
		pack_actuel.setBounds(getWidth() - 300, 250, 300, 200);
		timer.setBounds(getWidth() - 300, 280, 300, 200);
		aides.setBounds(getWidth() - 300, 310, 300, 200);
		aides.setText("Nombre d'Aides : " + nb_aide);
		pack_actuel.setText("PACK : " + packname);
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
		double score_ = Math.floor((temps + 6 * nb_aide) * 1000) / 1000;
		JLabel score =
			new JLabel("VOTRE SCORE : " + score_);
		score.setFont(font);
		score.setBounds(getWidth() - 300, 360, 400, 200);

		nomjoueur = new JTextArea("Nom");
		nomjoueur.setFont(font);
		nomjoueur.setBounds(getWidth() - 300, 500, 160, 50);

		Classement classement;
		if (omega) {
			classement = new Classement("null", font);
		} else {
			classement = new Classement(packname, font);
		}
		classement.setBounds(getWidth() - 650, 360, 300, 500);

		save_score = Utils.generate_button("save", e -> {
			String tmp = nomjoueur.getText().replaceAll(" ", "-");
			classement.ajouteScore(tmp.substring(0, Math.min(10, tmp.length())), score_);
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
				setMemory(false);
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

		if (!en_memory && g.estMemory()) {
			setMemory(true);
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
		update_current();
		setMemory(false);
		// packname = lvl.pack;
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

	private void setMemory(boolean b) {
		if (b) {
			en_memory = true;
			g.setModeGraphique(new GraphiqueMemory(g.getModeGraphique().image_bg));
		} else {
			en_memory = false;
			g.setModeGraphique(new GraphiqueDefaut(g.getModeGraphique().image_bg));
		}
		g.repaint();
	}
}
