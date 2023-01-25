import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import java.util.LinkedList;
import java.util.Random;

public class Editeur extends JComponent {

	private LinkedList<Sommet> sommets;
	private LinkedList<Point> coordonnees;

	public ControleurSouris controleurSouris;

	public Editeur() {

		addMouseListener(new ControleurSouris());

		sommets = new LinkedList<Sommet>();
		coordonnees = new LinkedList<Point>();

			repaint();
	}


	public void paintComponent(Graphics g) {
		for (int i = 0; i < sommets.size(); ++i) {
			((Graphics2D) g).draw(new Ellipse2D.Double(coordonnees.get(i).getX(), coordonnees.get(i).getY(), 10, 10));
			for(int j = i; j < sommets.size(); ++j){
				if(sommets.get(i).estRelie(sommets.get(j))){
					g.drawLine((int) coordonnees.get(i).getX() + 5, (int) coordonnees.get(i).getY() + 5, (int) coordonnees.get(j).getX() + 5, (int) coordonnees.get(j).getY() + 5);	
				}
			}
		}
	}

	public class ControleurSouris implements MouseListener{

		public ControleurSouris(){

		}

		public void mouseClicked(MouseEvent e){
			System.out.println(e.getX()+" : "+e.getY());
			sommets.add(new Sommet());
			coordonnees.add(new Point(e.getX(),e.getY()));
			repaint();
		}

		public void mouseEntered(MouseEvent e){

		}

		public void mouseExited(MouseEvent e){

		}

		public void mousePressed(MouseEvent e){

		}

		public void mouseReleased(MouseEvent e){

		}

	}
}
