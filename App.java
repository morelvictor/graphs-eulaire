import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class App {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			var frame = new Frame();
			frame.setTitle("EULAIRE");
			frame.setSize(1000, 1000);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setVisible(true);
		});
	}
}

class Frame extends JFrame {
	private JPanel container;

	public Frame() {
        
		container = new JPanel(new GridBagLayout());
		JPanel menu = new JPanel();
		menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
		JButton editeur = new JButton("Editeur");

		editeur.addActionListener((ActionEvent e) -> {
			lanceEditeur();
		});

	
		menu.add(editeur);		
		container.add(menu);
		add(container);
		pack();
	}


	public void lanceEditeur() {
		container.removeAll();
		add(new Editeur());
		validate();
		repaint();
	}
}
