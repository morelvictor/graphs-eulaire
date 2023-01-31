import java.util.ArrayList;
import java.util.Collections;

public class Graphe {
	public Graphe() {
		matrice = new GrapheMatrice();
		listes = new GrapheListes();
	}

	public void addSommet() {
		matrice.addSommet();
		listes.addSommet();
	}
	public void setConnexion(int i, int j, boolean val) {
		matrice.setConnexion(i, j, val);
		listes.setConnexion(i, j, val);
	}
	public ArrayList<Integer> getConnexions(int i) {
		return listes.getConnexions(i);
	}
	public int getConnexion(int i, int j) {
		return matrice.getConnexion(i, j);
	}

	public int taille() {
		return matrice.taille();
	}

	private GrapheMatrice matrice;
	private GrapheListes listes;
}

class GrapheMatrice {
	public GrapheMatrice() {
		contenu = new byte[1][1];
		taille_ = 0;
	}

	public void addSommet() {
		taille_++;
		if (taille_ > contenu[0].length) {
			var nouveau_contenu = new byte[contenu.length * 2][contenu[0].length * 2];
			for (int i = 0; i < contenu.length; i++) {
				for (int j = 0; j < contenu[i].length; j++) {
					nouveau_contenu[i][j] = contenu[i][j];
				}
			}
			contenu = nouveau_contenu;
		}
	}
	public void setConnexion(int i, int j, boolean val) {
		if (val) {
			contenu[i][j] += contenu[i][j] == 0xFF ? 0 : 1;
			if (i != j) {
				contenu[j][i] += contenu[i][j] == 0xFF ? 0 : 1;
			}
		} else {
			contenu[i][j] -= contenu[i][j] == 0x00 ? 0 : 1;
			if (i != j) {
				contenu[j][i] -= contenu[j][i] == 0x00 ? 0 : 1;
			}
		}
	}
	public ArrayList<Integer> getConnexions(int i) {
		var r = new ArrayList<Integer>();
		for (int j = 0; j < contenu[0].length; j++) {
			for (int n = 0; n < getConnexion(i, j); n++) {
				r.add(j);
			}
		}
		return r;
	}
	public int getConnexion(int i, int j) {
		return contenu[i][j];
	}

	public int taille() {
		return taille_;
	}

	private byte contenu[][];
	private int taille_;
}

class GrapheListes {
	@SuppressWarnings("unchecked")
	public GrapheListes() {
		contenu = new ArrayList[1];
		taille_ = 0;
	}

	@SuppressWarnings("unchecked")
	public void addSommet() {
		taille_++;
		if (taille_ > contenu.length) {
			var nouveau_contenu = new ArrayList[contenu.length * 2];
			for (int i = 0; i < contenu.length; i++) {
				nouveau_contenu[i] = contenu[i];
			}
			contenu = nouveau_contenu;
		}
		contenu[taille_ - 1] = new ArrayList<Integer>();
	}
	public void setConnexion(int i, int j, boolean val) {
		if (val) {
			contenu[i].add(Integer.valueOf(j));
			if (i != j) {
				contenu[j].add(Integer.valueOf(i));
			}
		} else {
			contenu[i].remove(Integer.valueOf(j));
			if (i != j) {
				contenu[j].remove(Integer.valueOf(i));
			}
		}
	}
	public ArrayList<Integer> getConnexions(int i) {
		return contenu[i];
	}
	public int getConnexion(int i, int j) {
		return Collections.frequency(contenu[i], Integer.valueOf(j));
	}

	public int taille() {
		return taille_;
	}

	private int taille_;
	private ArrayList<Integer> contenu[];
}
