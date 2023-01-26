import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import java.util.LinkedList;
import java.util.Random;

public class VueGrapheEditeur extends VueGraphe{

	boolean peut_poser_sommet = false;
	boolean peut_lier = false;


	Editeur editeur;

	public VueGrapheEditeur(Color c, int d, Editeur e, MouseListener controleur){
		super(c, d, controleur);
		editeur = e;
	}


	public void paintComponent(Graphics g) {
		g.setColor(super.getCouleur());

		LinkedList<Point> coord = super.getCoordonnees();
		LinkedList<Sommet> som = super.getSommets();
		int diam = super.getDiametre();



		for (int i = 0; i < som.size(); ++i) {
			((Graphics2D) g).draw(new Ellipse2D.Double(coord.get(i).getX(), coord.get(i).getY(), diam, diam));
			for(int j = i; j < som.size(); ++j){
				if(som.get(i).estRelie(som.get(j))){
					g.drawLine((int) (coord.get(i).getX() + diam/2), (int) (coord.get(i).getY() + diam/2), (int) (coord.get(j).getX() + diam/2), (int) (coord.get(j).getY() + diam/2));
				}
			}
		}

	}



	public LinkedList<Point> getCoordonnees(){
		return super.getCoordonnees();
	}

	public LinkedList<Sommet> getSommets(){
		return super.getSommets();
	}

	public void ajouteSommet(Sommet s){
		super.ajouteSommet(s);
	}

	public void ajouteCoordonnees(Point p){
		super.ajouteCoordonnees(p);
	}

	public int getId(int x, int y){
		return super.getId(x, y);
	}



	

}
