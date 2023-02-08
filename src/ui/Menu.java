import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Menu extends JPanel {
	ContentFrame contentFrame;

	public Menu(ContentFrame frame) {
		contentFrame = frame;

		JButton editorButton = new JButton("Editeur");
		JButton gameButton = new JButton("Jeu");

		editorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(new Editeur());
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


		add(editorButton);
		add(gameButton);
	}
}
