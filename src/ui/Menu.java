import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Menu extends JPanel {
	ContentFrame contentFrame;

	public Menu(ContentFrame frame) {
		contentFrame = frame;

		JButton editorButton = new JButton(new ImageIcon("../files/textures/editeur_bouton.png"));
		JButton gameButton = new JButton(new ImageIcon("../files/textures/jeu_bouton.png"));

		editorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(new Editeur(frame));
				frame.revalidate();
				frame.repaint();
			}
		});

		gameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(new Partie());
				frame.revalidate();
				frame.repaint();
			}
		});

		gameButton.setBounds(100, 200, 100, 50);

		add(editorButton);
		add(gameButton);
	}
}
