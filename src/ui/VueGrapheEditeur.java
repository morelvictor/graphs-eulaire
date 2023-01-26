import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import java.util.LinkedList;
import java.util.Random;

public class VueGrapheEditeur extends VueGraphe{

	boolean peut_poser_sommet = false;
	boolean peut_lier = false;

	int a_lier = -1;

	ControleurSourisEditeur controleurSouris;

	Editeur editeur;

	public VueGrapheEditeur(Color c, int d, Editeur e){
		super(c, d);
		controleurSouris = new ControleurSourisEditeur();
		editeur = e;
	}


	public void paintComponent(Graphics g) {
		g.setColor(super.getCouleur());

		if(editeur.getALier() != -1){
			g.fillOval((int) (super.getCoordonnees().get(a_lier).getX()),(int) (super.getCoordonnees().get(a_lier).getY()), DIAMETRE, DIAMETRE);
		}

		super.repaint();

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

public class ControleurSourisEditeur extends ControleurSouris{

	@Override

	public void mouseClicked(MouseEvent e){
		System.out.println("mouseclicked vuegraphe editeur");
		if(editeur.getPeutPoserSommet()){
			ajouteSommet(new Sommet());
			ajouteCoordonnees(new Point(e.getX(),e.getY()));
			repaint();
		}
		if(editeur.getPeutLier() && getId(e.getX(),e.getY()) >= 0){
			if(editeur.getALier() == -1){
				editeur.setALier(getId(e.getX(),e.getY()));
			}
			else{
				getSommets().get(editeur.getALier()).ajoute_arete(getSommets().get(getId(e.getX(),e.getY())));
				editeur.setALier(-1);
			}
			repaint();			
		}
	}

}

	

}
