import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import java.util.LinkedList;
import java.util.Random;

public abstract class VueGraphe extends JPanel{

	private LinkedList<Sommet> sommets;
	private LinkedList<Point> coordonnees;

	private Color COULEUR = Color.BLUE;
	public int DIAMETRE = 15;

	public VueGraphe(Color c, int d){
		COULEUR = c;
		DIAMETRE = d;
		sommets = new LinkedList<Sommet>();
		coordonnees = new LinkedList<Point>();
	}


	public LinkedList<Point> getCoordonnees(){
		return coordonnees;
	}

	public LinkedList<Sommet> getSommets(){
		return sommets;
	}

	public void ajouteSommet(Sommet s){
		sommets.add(s);
	}

	public void ajouteCoordonnees(Point p){
		coordonnees.add(p);
	}

	public Color getCouleur(){
		return COULEUR;
	}

	public int getDiametre(){
		return DIAMETRE;
	}	

	public int getId(int x, int y){
		for(int i = 0;i<coordonnees.size();i++){
			if( (x <= coordonnees.get(i).getX() + 3*DIAMETRE/2) && (x >= coordonnees.get(i).getX() - DIAMETRE/2) && (y <= coordonnees.get(i).getY() + 3*DIAMETRE/2) && (y >= coordonnees.get(i).getY() - DIAMETRE/2) ){
				return i;
			}
		}
		return -1;
	}

	public void paintComponent(Graphics g) {
		g.setColor(COULEUR);

		for (int i = 0; i < sommets.size(); ++i) {
			((Graphics2D) g).draw(new Ellipse2D.Double(coordonnees.get(i).getX(), coordonnees.get(i).getY(), DIAMETRE, DIAMETRE));
			for(int j = i; j < sommets.size(); ++j){
				if(sommets.get(i).estRelie(sommets.get(j))){
					g.drawLine((int) (coordonnees.get(i).getX() + DIAMETRE/2), (int) (coordonnees.get(i).getY() + DIAMETRE/2), (int) (coordonnees.get(j).getX() + DIAMETRE/2), (int) (coordonnees.get(j).getY() + DIAMETRE/2));
				}
			}
		}
	}

				
public abstract class ControleurSouris implements MouseListener{


		public void mouseClicked(MouseEvent e){
			System.out.println("mouseclicked vuegraphe");
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
