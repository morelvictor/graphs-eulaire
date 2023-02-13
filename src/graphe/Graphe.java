import java.lang.Cloneable;
import java.lang.CloneNotSupportedException;
import java.util.ArrayList;
import java.util.Collections;

public class Graphe implements Cloneable {
	public Graphe() {
		matrice = new GrapheMatrice();
		listes = new GrapheListes();
	}

	public Graphe clone() throws CloneNotSupportedException {
		var g = new Graphe();
		g.matrice = matrice.clone();
		g.listes = listes.clone();
		return g;
	}

	public void addSommet() {
		matrice.addSommet();
		listes.addSommet();
	}
	public void setConnexion(int i, int j, boolean val) {
		matrice.setConnexion(i, j, val);
		listes.setConnexion(i, j, val);
	}
	public int supprSommet(int i) {
		matrice.supprSommet(i);
		return listes.supprSommet(i);
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
	public int nbConnexions() {
		int res = 0;
		for (int i = 0; i < taille(); i++) {
			res += getConnexions(i).size();
		}
		return res;
	}

	public String toString() {
		return matrice.toString();
	}

	public ArrayList<Integer> breadthFirst(int start) {
		var result = new ArrayList<Integer>();
		result.add(Integer.valueOf(start));
		for (int i = 0; i < result.size(); i++) {
			for (Integer v : getConnexions(result.get(i))) {
				if (!result.contains(v)) {
					result.add(v);
				}
			}
		}
		return result;
	}

	public boolean estEulerien() {
		if (taille() == 0) {
			return false;
		}
		if (breadthFirst(0).size() != taille()) {
			return false;
		}
		var odd_vertices = 0;
		for (int i = 0; i < taille(); i++) {
			if ((getConnexions(i).size() - getConnexion(i, i)) % 2 == 1) {
				odd_vertices++;
			}
		}
		if (odd_vertices > 2) {
			return false;
		}
		return true;
	}

	public ArrayList<Integer> hierholzer() {
		if (taille() == 0) {
			System.err.println("Graph must not be empty for hierholzer to work.");
			return null;
		}

		if (breadthFirst(0).size() != taille()) {
			System.err.println("Graph must be connected for hierholzer to work.");
			return null;
		}
		var odd_vertices = new ArrayList<Integer>();
		for (int i = 0; i < taille(); i++) {
			if ((getConnexions(i).size() - getConnexion(i, i)) % 2 == 1) {
				odd_vertices.add(Integer.valueOf(i));
			}
		}
		if (odd_vertices.size() > 2) {
			System.err.println("Graph contains no Euler cycle or trail.");
			return null;
		}

		Graphe g;
		try {
			g = clone();
		} catch (CloneNotSupportedException err) { // This won't happen, java's just being a dick.
			System.err.println("This shouldn't happen.");
			g = null;
		}

		var result = new ArrayList<Integer>();
		if (odd_vertices.size() == 0) {
			result.add(Integer.valueOf(0));
		} else {
			result.add(odd_vertices.get(0));
		}
		int insertion_point = 0;
		while (insertion_point >= 0) {
			var nexts = g.getConnexions(result.get(insertion_point));
			if (nexts.size() == 0) {
				insertion_point--;
			} else {
				insertion_point++;
				result.add(insertion_point, nexts.get(0));
				g.setConnexion(result.get(insertion_point - 1), result.get(insertion_point), false);
			}
		}


		return result;
	}


	private GrapheMatrice matrice;
	private GrapheListes listes;
}

class GrapheMatrice implements Cloneable {
	public GrapheMatrice() {
		contenu = new byte[1][1];
		taille_ = 0;
	}

	public GrapheMatrice clone() throws CloneNotSupportedException {
		var g = new GrapheMatrice();
		g.contenu = new byte[contenu.length][contenu[0].length];
		for (int i = 0; i < taille(); i++) {
			for (int j = 0; j < taille(); j++) {
				g.contenu[i][j] = contenu[i][j];
			}
		}
		g.taille_ = taille_;
		return g;
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
		for (int n = 0; n < taille_; n++) {
			contenu[taille_ - 1][n] = 0;
			contenu[n][taille_ - 1] = 0;
		}
	}
	public void setConnexion(int i, int j, boolean val) {
		if (val) {
			contenu[i][j] += contenu[i][j] == 0xFF ? 0 : 1;
			if (i != j) {
				contenu[j][i] += contenu[j][i] == 0xFF ? 0 : 1;
			}
		} else {
			contenu[i][j] -= contenu[i][j] == 0x00 ? 0 : 1;
			if (i != j) {
				contenu[j][i] -= contenu[j][i] == 0x00 ? 0 : 1;
			}
		}
	}
	public void supprSommet(int i) {
		for (int j = 0; j < taille_; j++) {
			contenu[i][j] = contenu[taille_ - 1][j];
			contenu[j][i] = contenu[j][taille_ - 1];
		}
		taille_--;
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

	public String toString() {
		String res = "";

		if (taille_ == 0) {
			return res;
		}

		for (int i = 0; i < taille_; i++) {
			res += contenu[i][0] + "";
			for (int j = 1; j < taille_; j++) {
				res += " " + contenu[i][j];
			}
			res += "\n";
		}
		return res;
	}

	private byte contenu[][];
	private int taille_;
}

class GrapheListes implements Cloneable {
	@SuppressWarnings("unchecked")
	public GrapheListes() {
		contenu = new ArrayList[1];
		taille_ = 0;
	}

	@SuppressWarnings("unchecked")
	public GrapheListes clone() throws CloneNotSupportedException {
		var g = new GrapheListes();
		g.contenu = new ArrayList[contenu.length];
		for (int i = 0; i < g.contenu.length; i++) {
			if (contenu[i] == null) {
				g.contenu[i] = null;
			} else {
				g.contenu[i] = (ArrayList<Integer>)contenu[i].clone();
			}
		}
		g.taille_ = taille_;
		return g;
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
	public int supprSommet(int i) {
		int res = contenu[i].size();
		while (contenu[i].size() > 0) {
			setConnexion(i, contenu[i].get(0), false);
		}
		contenu[i] = contenu[taille_ - 1];
		taille_--;
		return res;
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
