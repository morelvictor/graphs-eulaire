import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;

public class Editeur extends JComponent{

	private LinkedList<Sommet> sommets;
	private LinkedList<Point> coordonnees;

	private VueGraphe graphe;

	public Editeur(){
		setLayout(null);
		sommets = new LinkedList<Sommet>();
		coordonnees = new LinkedList<Point>();

		for(int i = 3;i < 28;i+=5){
			sommets.add(new Sommet());
			coordonnees.add(new Point(i,i));
		}

		graphe = new VueGraphe(sommets, coordonnees);
		graphe.setBounds(20,20,800,800);
		add(graphe);
		repaint();
		

	}

}
