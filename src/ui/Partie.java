import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Partie extends JPanel {
	VueGraphePartie g;
	Image background;
	JButton regenerer = new JButton(new ImageIcon("../textures/retry.png"));
	JButton editeur = new JButton(new ImageIcon("../textures/jeu-editeur.png"));
	boolean testing_editing;

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

	public Partie(JFrame frame, Image bg, String pack, int graph, boolean testing_editing) {
		current_level = graph;
		loadPack(pack);
		if (levels.size() == 0) {
			System.err.println("No levels in pack " + (pack == null ? "Ω" : pack) + ".");
			System.exit(1);
		}
		background = bg;
		g = new VueGraphePartie(this);
		add(g);
		this.testing_editing = testing_editing;
		if (testing_editing) {
			editeur.setBorderPainted(false);
			editeur.setContentAreaFilled(false);
			editeur.setFocusPainted(false);
			add(editeur);
			editeur.addActionListener(e -> {
				frame.setContentPane(new Editeur(frame, background, pack, current_level));
				frame.revalidate();
				frame.repaint();
			});
		}
		regenerer.setBorderPainted(false);
		regenerer.setContentAreaFilled(false);
		regenerer.setFocusPainted(false);
		add(regenerer);
		regenerer.addActionListener(e -> {
			System.out.println("Je regenère la team");
			g.regen();
		});
		repaint();
	}

	public Partie(JFrame frame, VueGraphe vg, Image bg, String pack, int graph, boolean testing_editing) {
		current_level = graph;
		loadPack(pack);
		if (levels.size() == 0) {
			System.err.println("No levels in pack " + (pack == null ? "Ω" : pack) + ".");
			System.exit(1);
		}
		background = bg;
		g = new VueGraphePartie(this);
		g.setGraphe(vg.getGraphe());
		g.setCoordonnes(vg.getCoordonnees());
		g.setOrigin();
		add(g);
		this.testing_editing = testing_editing;
		if (testing_editing) {
			editeur.setBorderPainted(false);
			editeur.setContentAreaFilled(false);
			editeur.setFocusPainted(false);
			add(editeur);
			editeur.addActionListener(e -> {
				frame.setContentPane(new Editeur(frame, background, pack, current_level));
				frame.revalidate();
				frame.repaint();
			});
		}
		regenerer.setBorderPainted(false);
		regenerer.setContentAreaFilled(false);
		regenerer.setFocusPainted(false);
		add(regenerer);
		regenerer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Je regenère la team");
				g.regen();
			}
		});
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		regenerer.setBounds(getWidth() - 90, getHeight() / 2, 80, 50);
	}

	public void suivant() {
		if (testing_editing) {
			g.regen();
		} else {
			g.setGrapheJeu(levels.get(current_level).pack, levels.get(current_level).n);
			current_level = (current_level + 1) % levels.size();
		}
	}
}
