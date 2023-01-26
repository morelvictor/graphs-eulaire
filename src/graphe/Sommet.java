import java.util.LinkedList;

public class Sommet{

	public LinkedList<Sommet> voisins;

	public Sommet(){
		voisins = new LinkedList<Sommet>();
	}

	public void ajoute_arete(Sommet s2){
		voisins.add(s2);
		s2.voisins.add(this);
	}

	public boolean estRelie(Sommet s){
		return voisins.contains(s);
	}
}
