import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Menu extends JPanel {
	JFrame contentFrame;
	Image background;
	Classement classement;

	public Menu(JFrame frame) {
		contentFrame = frame;
		try {
			background = ImageIO.read(new java.io.File("../textures/background.png"));
		} catch (java.io.IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		frame.setLayout(null);

		classement = new Classement("null");
		classement.setBounds(2 * getWidth() / 3, getHeight() - 100, 200, 500);

		var packs = new java.util.ArrayList<String>();
		for (var file : (new java.io.File("../packs")).listFiles()) {
			packs.add(file.getName());
		}

		var current_pack = new Object() {
			public int v = packs.size();
		};
		JButton editorButton = Utils.generate_button("editeur_bouton", e -> {
			final String pack = current_pack.v == packs.size() ? null : packs.get(current_pack.v);
			frame.setContentPane(new Editeur(frame, background, pack, null, -1));
			frame.revalidate();
			frame.repaint();
		});
		editorButton.setBounds(getWidth() / 3, getHeight() / 4, 200, 100);
		JButton gameButton = Utils.generate_button("jeu_bouton", e -> {
			final String pack = current_pack.v == packs.size() ? null : packs.get(current_pack.v);
			frame.setContentPane(new Partie(frame, background, pack, null, 0));
			frame.revalidate();
			frame.repaint();
		});
		gameButton.setBounds(getWidth() / 2, getHeight() / 4, 200, 100);
		JLabel packLabel = new JLabel("Ω");
		JButton packButton = Utils.generate_button("pack_bouton", e -> {
			current_pack.v = (current_pack.v + 1) % (packs.size() + 1);
			packLabel.setText(current_pack.v == packs.size() ? "Ω" : packs.get(current_pack.v));
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
