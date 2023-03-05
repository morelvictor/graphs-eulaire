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

		background = ImageIO.read(new File("../textures/background.png"));
		JButton editorButton = new JButton(new ImageIcon("../textures/editeur_bouton.png"));
		JButton gameButton = new JButton(new ImageIcon("../textures/jeu_bouton.png"));
		JLabel packLabel = new JLabel("Ω");
		JButton packButton = new JButton(new ImageIcon("../textures/pack_bouton.png"));

		var packs = new java.util.ArrayList<String>();

		for (var file : (new java.io.File("../packs")).listFiles()) {
			packs.add(file.getName());
		}

		class Current {
			public int v;
		}
		Current current_pack = new Current();
		current_pack.v = packs.size();

		editorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(new Editeur(frame, background, (current_pack.v == packs.size() ? null : packs.get(current_pack.v)), -1));
				frame.revalidate();
				frame.repaint();
			}
		});
		gameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setContentPane(new Partie(frame, background, (current_pack.v == packs.size() ? null : packs.get(current_pack.v)), 0, false));
				frame.revalidate();
				frame.repaint();
			}
		});
		packButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				current_pack.v = (current_pack.v + 1) % (packs.size() + 1);
				packLabel.setText(current_pack.v == packs.size() ? "Ω" : packs.get(current_pack.v));
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
		packButton.setBorderPainted(false);
		packButton.setContentAreaFilled(false);
		packButton.setFocusPainted(false);

		add(editorButton);
		add(gameButton);
		add(packButton);
		add(packLabel);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
}
