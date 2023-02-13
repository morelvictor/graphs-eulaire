import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Partie extends JPanel {
	VueGraphePartie g;

	JButton regenerer = new JButton("Regen");

	public Partie() {
		g = new VueGraphePartie(this);
		add(g);
		add(regenerer);
		regenerer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Je regenère la team");
				g.regen();
			}
		});
	}

	public Partie(VueGraphe vg) {
		g = new VueGraphePartie(this);
		g.setGraphe(vg.getGraphe());
		g.setCoordonnes(vg.getCoordonnees());
		g.setOrigin();
		add(g);
		add(regenerer);
		regenerer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Je regenère la team");
				g.regen();
			}
		});
	}
}
