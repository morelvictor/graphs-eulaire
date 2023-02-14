import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Menu extends JPanel {
	ContentFrame contentFrame;
	Image background;

	public Menu(ContentFrame frame) throws Exception {
		contentFrame = frame;

		background = ImageIO.read(new File("../files/textures/background.png"));
		JButton editorButton = new JButton(new ImageIcon("../files/textures/editeur_bouton.png"));
		JButton gameButton = new JButton(new ImageIcon("../files/textures/jeu_bouton.png"));

		editorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(new Editeur(frame, background));
				frame.revalidate();
				frame.repaint();
			}
		});

		gameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(new Partie(background));
				frame.revalidate();
				frame.repaint();
			}
		});
		editorButton.setBorderPainted(false);
		editorButton.setContentAreaFilled(false);
		editorButton.setFocusPainted(false);
		gameButton.setBorderPainted(false);
		gameButton.setContentAreaFilled(false);
		gameButton.setFocusPainted(false);

		add(editorButton);
		add(gameButton);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
}
