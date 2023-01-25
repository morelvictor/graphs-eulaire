import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import java.util.LinkedList;
import java.util.Random;

public class Editeur extends JComponent {
	private static int DIAMETRE = 10;
	private static Color COULEUR = new Color(39, 78, 140);

	private LinkedList<Sommet> sommets;
	private LinkedList<Point> coordonnees;

	public ControleurSouris controleurSouris;

	private JButton poser_sommet;
	private boolean peut_poser_sommet = false;

	private JButton lier;
	private boolean peut_lier = false;
	//indice dans la liste sommets du sommet à lier
	private int a_lier = -1;


	public Editeur() {

		addMouseListener(new ControleurSouris());

		sommets = new LinkedList<Sommet>();
		coordonnees = new LinkedList<Point>();

		poser_sommet = new JButton("o");

		poser_sommet.setBounds(900,50,50,50);
		add(poser_sommet);

		poser_sommet.addActionListener(
			(ActionEvent e) -> {
				peut_poser_sommet = !peut_poser_sommet;
				lier.setEnabled(!peut_poser_sommet);
			});

		lier = new JButton("x");
		lier.setBounds(900,110,50,50);
		add(lier);

		lier.addActionListener(
			(ActionEvent e) -> {
				peut_lier = !peut_lier;
				poser_sommet.setEnabled(!peut_lier);
				a_lier = -1;
			});
	
		repaint();
	}


	public void paintComponent(Graphics g) {
		validate();
		System.out.println(getHeight()+" : "+getWidth());
		g.setColor(COULEUR);
		if(a_lier != -1){
			g.fillOval((int) (coordonnees.get(a_lier).getX()),(int) (coordonnees.get(a_lier).getY()), DIAMETRE, DIAMETRE);

		}

		lier.setBounds(getWidth() - 100,110,50,50);
		poser_sommet.setBounds(getWidth() - 100,50,50,50);

		for (int i = 0; i < sommets.size(); ++i) {
			((Graphics2D) g).draw(new Ellipse2D.Double(coordonnees.get(i).getX(), coordonnees.get(i).getY(), DIAMETRE, DIAMETRE));
			for(int j = i; j < sommets.size(); ++j){
				if(sommets.get(i).estRelie(sommets.get(j))){
					g.drawLine((int) (coordonnees.get(i).getX() + DIAMETRE/2), (int) (coordonnees.get(i).getY() + DIAMETRE/2), (int) (coordonnees.get(j).getX() + DIAMETRE/2), (int) (coordonnees.get(j).getY() + DIAMETRE/2));
				}
			}
		}
	}

	public int getId(int x, int y){
		for(int i = 0;i<sommets.size();i++){
			if( (x <= coordonnees.get(i).getX() + 3*DIAMETRE/2) && (x >= coordonnees.get(i).getX() - DIAMETRE/2) && (y <= coordonnees.get(i).getY() + 3*DIAMETRE/2) && (y >= coordonnees.get(i).getY() - DIAMETRE/2) ){
				return i;
			}
		}
		return -1;
	}

	public class ControleurSouris implements MouseListener{

		public ControleurSouris(){

		}

		public void mouseClicked(MouseEvent e){
			if(peut_poser_sommet){
				sommets.add(new Sommet());
				coordonnees.add(new Point(e.getX(),e.getY()));
				repaint();
			}
			if(peut_lier && getId(e.getX(),e.getY()) >= 0){
				if(a_lier == -1){
					a_lier = getId(e.getX(),e.getY());
				}
				else{
					sommets.get(a_lier).ajoute_arete(sommets.get(getId(e.getX(),e.getY())));
					a_lier = -1;
				}
				repaint();
				
			}
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
