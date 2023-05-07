import java.lang.Cloneable;
import java.util.ArrayList;

class Graphe implements Cloneable {
	public Graphe() {
		contenu = new byte[1][1];
		taille_ = 0;
	}

	public Graphe clone() {
		var g = new Graphe();
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
	public void addConnections(int i, int j, int count) {
		contenu[i][j] = (byte)Math.min(0xFF, Math.max(0x00, (int)contenu[i][j] + count)); // Clamp to a byte.
		if (i != j) {
			contenu[j][i] = (byte)Math.min(0xFF, Math.max(0x00, (int)contenu[j][i] + count));
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
		for (int j = 0; j < taille_; j++) {
			for (int n = 0; n < getConnexion(i, j); n++) {
				r.add(j);
			}
		}
		return r;
	}
	public boolean hasConnexions() {
		for (int i = 0; i < taille_; i++) {
			for (int j = i; j < taille_; j++) {
				if (getConnexion(i, j) > 0) {
					return true;
				}
			}
		}
		return false;
	}
	public int getConnexion(int i, int j) {
		return contenu[i][j];
	}

	public int taille() {
		return taille_;
	}

	public String toString() {
		String res = "";

		for (int i = 0; i < taille_; i++) {
			res += contenu[i][0] + "";
			for (int j = 1; j < taille_; j++) {
				res += " " + contenu[i][j];
			}
			res += "\n";
		}
		return res;
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
		if (!estEulerien()) {
			System.err.println("Graph isn't Eulerian.");
			return null;
		}

		var odd_vertex = 0;
		for (int i = 0; i < taille(); i++) {
			if ((getConnexions(i).size() - getConnexion(i, i)) % 2 == 1) {
				odd_vertex = i;
			}
		}
		return hierholzer_from(odd_vertex);
	}

	public ArrayList<Integer> hierholzer_from(int vertex) {
		Graphe g = clone();

		var result = new ArrayList<Integer>();
		result.add(Integer.valueOf(vertex));

		int insertion_point = 0;
		while (insertion_point >= 0) {
			var nexts = g.getConnexions(result.get(insertion_point));
			if (nexts.size() == 0) {
				insertion_point--;
			} else {
				insertion_point++;
				result.add(insertion_point, nexts.get(0));
				g.addConnections(result.get(insertion_point - 1), result.get(insertion_point), -1);
			}
		}

		return result;
	}

	private byte contenu[][];
	private int taille_;
}
