import java.awt.*;
import javax.swing.border.Border;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.LinkedList;

public class VueGraphe extends JPanel{

	private LinkedList<Sommet> sommets;
	private LinkedList<Point> coordonnees;

	private static Color color = Color.BLUE;

	public VueGraphe(LinkedList<Sommet> s, LinkedList<Point> c){
		setLayout(null);
		sommets = s;
		coordonnees = c;
	}

	public void paintComponent(Graphics g){
		//on affiche seulement les sommets pour l'instant
		super.paintComponent(g);
		g.setColor(color);
		for(int i = 0;i<sommets.size();i++){
			((Graphics2D) g).draw(new Ellipse2D.Double(coordonnees.get(i).getX(), coordonnees.get(i).getY(), 10, 10));
		}

	}

}
