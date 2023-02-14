import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Partie extends JPanel {
	VueGraphePartie g;
	Image background;
	JButton regenerer = new JButton(new ImageIcon("../files/textures/retry.png"));

	public Partie(Image bg) {
		background = bg;
		g = new VueGraphePartie(this);
		add(g);
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
		repaint();
	}

	public Partie(VueGraphe vg, Image bg) {
		background = bg;
		g = new VueGraphePartie(this);
		g.setGraphe(vg.getGraphe());
		g.setCoordonnes(vg.getCoordonnees());
		g.setOrigin();
		add(g);
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
}
