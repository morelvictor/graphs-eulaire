import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

import java.util.LinkedList;
import java.util.Random;

public class Editeur extends JPanel {
	private static int DIAMETRE = 15;
	private static Color COULEUR = new Color(39, 78, 140);

	private VueGrapheEditeur vuegraphe;


	private JButton poser_sommet;
	private boolean peut_poser_sommet = false;

	private JButton lier;
	private boolean peut_lier = false;
	//indice dans la liste sommets du sommet à lier
	private int a_lier = -1;

	private JButton generer_random;
	private Random r = new Random();


	public Editeur() {

		vuegraphe = new VueGrapheEditeur(COULEUR, DIAMETRE, this, new ControleurSourisEditeur());
		vuegraphe.setBounds(20,20,800,800);
		add(vuegraphe);

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

		generer_random = new JButton("?");
		generer_random.setBounds(900,170,50,50);
		add(generer_random);
	

		generer_random.addActionListener(
			(ActionEvent e) -> {
				int taille = r.nextInt(20);
				int proba = r.nextInt(10);
				vuegraphe.setSommets(getNRandomSom(taille, proba));
				vuegraphe.setCoordonnees(getNRandomCoord(taille));
				repaint();
			});

		repaint();
	}

	public LinkedList<Point> getNRandomCoord(int n){
		LinkedList<Point> res = new LinkedList<Point>();
		for(int i = 0;i<n;i++){
			res.add(new Point(20+r.nextInt(830),20+r.nextInt(830)));
		}
		return res;
	}

	public LinkedList<Sommet> getNRandomSom(int n, int inverse_proba_arete){
		LinkedList<Sommet> res = new LinkedList<Sommet>();
		for(int i = 0;i<n;i++){
			res.add(new Sommet());
		}
		for(int id = 0;id<n;id++){
			for(int j = id+1;j<n;j++){
				if(r.nextInt(inverse_proba_arete+1)==1){
					res.get(id).ajoute_arete(res.get(j));
				}
			}
		}
		return res;
	}


	public int getALier(){
		return a_lier;
	}

	public void setALier(int n){
		a_lier = n;
	}

	public boolean getPeutPoserSommet(){
		return peut_poser_sommet;
	}

	public boolean getPeutLier(){
		return peut_lier;
	}

	public void paintComponent(Graphics g) {
		lier.setBounds(getWidth() - 100,110,50,50);
		poser_sommet.setBounds(getWidth() - 100,50,50,50);
		generer_random.setBounds(getWidth() - 100,170,50,50);
		g.drawRect(20,20,850,850);
	}


public class ControleurSourisEditeur extends ControleurSouris{

	@Override

	public void mouseClicked(MouseEvent e){
		if(getPeutPoserSommet()){
			vuegraphe.ajouteSommet(new Sommet());
			vuegraphe.ajouteCoordonnees(new Point(e.getX(),e.getY()));
		}
		int id = vuegraphe.getId(e.getX(), e.getY());
		if(getPeutLier() && id >= 0){
			if(getALier() == -1){
				setALier(id);
			}
			else{
				vuegraphe.getSommets().get(getALier()).ajoute_arete(vuegraphe.getSommets().get(id));
				setALier(-1);
			}			
		}
		repaint();
	}

}

public abstract class ControleurSouris implements MouseListener{


		public void mouseClicked(MouseEvent e){
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
