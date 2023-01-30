import java.util.ArrayList;

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
	public boolean getConnexion(int i, int j) {
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
		contenu = new byte[1][8];
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
			contenu[i / 8][j] |= (1 << (i % 8));
			contenu[j / 8][i] |= (1 << (j % 8));
		} else {
			contenu[i / 8][j] &= ~(1 << (i % 8));
			contenu[j / 8][i] &= ~(1 << (j % 8));
		}
	}
	public ArrayList<Integer> getConnexions(int i) {
		var r = new ArrayList<Integer>();
		for (int j = 0; j < contenu[0].length; j++) {
			if (getConnexion(i, j)) {
				r.add(j);
			}
		}
		return r;
	}
	public boolean getConnexion(int i, int j) {
		return (contenu[i / 8][j] & (1 << (i % 8))) != 0;
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
		if (getConnexion(i, j) == val) {
			return;
		}
		if (val == false) {
			contenu[i].remove(Integer.valueOf(j));
			if (i != j) {
				contenu[j].remove(Integer.valueOf(i));
			}
		}
		if (val == true) {
			contenu[i].add(Integer.valueOf(j));
			if (i != j) {
				contenu[j].add(Integer.valueOf(i));
			}
		}
	}
	public ArrayList<Integer> getConnexions(int i) {
		return contenu[i];
	}
	public boolean getConnexion(int i, int j) {
		return contenu[i].contains(Integer.valueOf(j));
	}

	public int taille() {
		return taille_;
	}

	private int taille_;
	private ArrayList<Integer> contenu[];
}
