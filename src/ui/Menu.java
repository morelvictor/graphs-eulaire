import java.awt.*;
import java.io.*;
import java.util.Collections;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Menu extends JPanel {
	JFrame contentFrame;
	Image background;
	Classement classement;
	Font font;

	public Menu(JFrame frame) {
		contentFrame = frame;
		try {
			background = ImageIO.read(new java.io.File("../textures/background.png"));
		} catch (java.io.IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		frame.setLayout(null);

		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT,
			                                new File("../textures/IAmTheCrayonMaster.ttf")));
			font = new Font("IAmTheCrayonMaster", Font.BOLD, 30);
		} catch (IOException | FontFormatException e) {
			font = new Font("Serif", Font.PLAIN, 20);
		}


		var packs = new java.util.ArrayList<String>();
		for (var file : (new java.io.File("../packs")).listFiles()) {
			packs.add(file.getName());
		}
		Collections.sort(packs);

		var current_pack = new Object() {
			public int v = 0;
		};
		classement = new Classement(current_pack.v == packs.size() ? "null" : packs.get(current_pack.v), font);
		classement.setBounds(2 * getWidth() / 3, getHeight() - 100, 300, 500);

		JButton editorButton = Utils.generate_button("editeur_bouton", e -> {
			final String pack = current_pack.v == packs.size() ? null : packs.get(current_pack.v);
			frame.setContentPane(new Editeur(frame, background, pack, null, -1, font));
			frame.revalidate();
			frame.repaint();
		});
		editorButton.setBounds(getWidth() / 3, getHeight() / 4, 200, 100);
		JButton gameButton = Utils.generate_button("jeu_bouton", e -> {
			final String pack = current_pack.v == packs.size() ? null : packs.get(current_pack.v);
			frame.setContentPane(new Partie(frame, background, pack, null, 0, font));
			frame.revalidate();
			frame.repaint();
		});
		gameButton.setBounds(getWidth() / 2, getHeight() / 4, 200, 100);
		JLabel packLabel = new JLabel(current_pack.v == packs.size() ? "ALL" : packs.get(current_pack.v));
		packLabel.setFont(font);
		JButton packButton = Utils.generate_button("pack_bouton", e -> {
			current_pack.v = (current_pack.v + 1) % (packs.size() + 1);
			packLabel.setText(current_pack.v == packs.size() ? "ALL" : packs.get(current_pack.v));
			classement.changePack(current_pack.v == packs.size() ? "null" : packs.get(current_pack.v));
			frame.repaint();
		});
		packButton.setBounds(2 * getWidth() / 3, getHeight() / 4, 200, 100);

		add(editorButton);
		add(gameButton);
		add(packButton);
		add(packLabel);
		add(classement);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
}
