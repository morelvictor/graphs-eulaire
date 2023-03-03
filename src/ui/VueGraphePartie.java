import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import javax.swing.*;

public class VueGraphePartie extends VueGraphe {
	int oujesuis = -1;
	Graphe origin;
	Partie partie;

	public VueGraphePartie(Partie p) {
		super(Color.black, 30, null);
		partie = p;
		setOrigin();
		addMouseListener(new MouseListener() {
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				int clicked = getId(e.getX(), e.getY());
				if (clicked == -1) return;

				if (oujesuis < 0) {
					if (getId(e.getX(), e.getY()) != -1) {
						oujesuis = getId(e.getX(), e.getY());
						repaint();
					}
				} else {
					if (getGraphe().getConnexion(oujesuis, clicked) != 0) {
						getGraphe().setConnexion(oujesuis, clicked, false);
						oujesuis = clicked;
						repaint();
						if (estFinie()) {
							partie.suivant();
						}
					}
				}
			}
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 850, 850);
		g.setColor(getCouleur());

		LinkedList<Point> coord = getCoordonnees();
		int diam = getDiametre();

		for (int i = 0; i < getGraphe().taille(); ++i) {
			if (i == oujesuis) {
				g.setColor(Color.green);
			}
			((Graphics2D) g).draw(new Ellipse2D.Double(coord.get(i).getX(), coord.get(i).getY(), diam,
			                                           diam));
			if (i == oujesuis) {
				g.setColor(getCouleur());
			}
			for (int j = i; j < getGraphe().taille(); ++j) {
				if (getGraphe().getConnexion(i, j) != 0) {
					int coord_i_x = (int) (coord.get(i).getX() + diam / 2);
					int coord_i_y = (int) (coord.get(i).getY() + diam / 2);
					int coord_j_x = (int) (coord.get(j).getX() + diam / 2);
					int coord_j_y = (int) (coord.get(j).getY() + diam / 2);
					g.drawLine(coord_i_x, coord_i_y, coord_j_x, coord_j_y);
					if (getGraphe().getConnexion(i, j) > 1) {
						g.drawString(getGraphe().getConnexion(i, j) + "", // uncrustify,
						             (coord_i_x + coord_j_x) / 2, // what are you doing.
						             (coord_i_y + coord_j_y) / 2);
					}
				}
			}
		}
	}

	public void setGrapheJeu(String pack, int n) {
		super.importer(pack, n);
		setOrigin();
		regen();
	}

	public void regen() {
		try {
			setGraphe(origin.clone());
			oujesuis = -1;
			repaint();
		} catch (CloneNotSupportedException e) {
			System.out.println("This shouldn't happen.");
			// This won't happen, java's just being a dick.
		}
	}

	public void setOrigin() {
		try {
			origin = getGraphe().clone();
		} catch (CloneNotSupportedException e) {
			System.out.println("This shouldn't happen.");
			// This won't happen, java's just being a dick.
		}
	}

	public boolean estFinie() {
		return getGraphe().nbConnexions() == 0; // on peut aussi tester si la partie ne peut plus être gagnée
	}
}
