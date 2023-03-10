import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Menu extends JPanel {
	ContentFrame contentFrame;
	Image background;

	public Menu(ContentFrame frame) throws Exception {
		contentFrame = frame;
		background = ImageIO.read(new File("../textures/background.png"));

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
		JButton gameButton = Utils.generate_button("jeu_bouton", e -> {
			final String pack = current_pack.v == packs.size() ? null : packs.get(current_pack.v);
			frame.setContentPane(new Partie(frame, background, pack, null, 0));
			frame.revalidate();
			frame.repaint();
		});
		JLabel packLabel = new JLabel("Ω");
		JButton packButton = Utils.generate_button("pack_bouton", e -> {
			current_pack.v = (current_pack.v + 1) % (packs.size() + 1);
			packLabel.setText(current_pack.v == packs.size() ? "Ω" : packs.get(current_pack.v));
			frame.repaint();
		});

		add(editorButton);
		add(gameButton);
		add(packButton);
		add(packLabel);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
}
