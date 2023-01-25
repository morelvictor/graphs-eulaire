import java.util.Random;

public class Sommet{

	private static int nb_graphes;
	private int id;

	public Sommet(){
		id = nb_graphes;
		++nb_graphes;
	}

	public void ajoute_arete(Sommet s1, Sommet s2){
		return;
	}

	public boolean estRelie(Sommet s){
		Random r = new Random();
		return r.nextInt(5)==1;
	}
}
