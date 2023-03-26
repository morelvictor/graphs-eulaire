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
	JButton aide;
	private ArrayList<Integer> solution;
	private int indice_solution = 0;
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
			next_point(solution.get(indice_solution));
			indice_solution++;
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
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		regenerer.setBounds(getWidth() - 120, 710, 90, 50);
		editeur.setBounds(getWidth() - 120, 800, 90, 50);
		aide.setBounds(getWidth() - 120, 620, 90, 50);
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

		JButton congrats = new JButton("NEXT");
		JLabel timer =
			new JLabel(Double.toString(((double)(System.currentTimeMillis() - debutTimer)) / 1000.0));
		timer.setFont(new Font("Serif", Font.PLAIN, 20));

		congrats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				suivant();
				add(g);
				add(regenerer);
				remove(congrats);
				remove(timer);
				validate();
				repaint();
				debutTimer = System.currentTimeMillis();
			}
		});

		add(congrats);
		add(timer);
		validate();
		repaint();
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
