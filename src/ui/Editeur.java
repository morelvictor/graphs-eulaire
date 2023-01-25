import java.awt.*;
import java.awt.geom.Ellipse2D;

import javax.swing.*;

public class Editeur extends JComponent {
	public Editeur() {
		repaint();
	}

	public void paintComponent(Graphics g) {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 10; j++) {
				((Graphics2D) g).draw(new Ellipse2D.Double(50*i, 50*j, 10, 10));
			}
		}
	}
}
