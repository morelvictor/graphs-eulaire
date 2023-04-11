import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Menu extends JPanel {
	App app;
	Image background;
	Classement classement;

	public Menu(App app) {
		this.app = app;
		try {
			background = ImageIO.read(new java.io.File("../textures/background.png"));
		} catch (java.io.IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		app.frame.setLayout(null);

		classement = new Classement("null", app.settings.font);
		classement.setBounds(2 * getWidth() / 3, getHeight() - 100, 300, 500);

		var packs = new java.util.ArrayList<String>();
		for (var file : (new java.io.File("../packs")).listFiles()) {
			packs.add(file.getName());
		}

		var current_pack = new Object() {
			public int v = packs.size();
		};
		JButton editorButton = Utils.generate_button("editeur_bouton", e -> {
			final String pack = current_pack.v == packs.size() ? null : packs.get(current_pack.v);
			app.frame.setContentPane(new Editeur(app, background, pack, null, -1, app.settings.font));
			app.frame.revalidate();
			app.frame.repaint();
		});
		editorButton.setBounds(getWidth() / 3, getHeight() / 4, 200, 100);
		JButton gameButton = Utils.generate_button("jeu_bouton", e -> {
			final String pack = current_pack.v == packs.size() ? null : packs.get(current_pack.v);
			app.frame.setContentPane(new Partie(app, background, pack, null, 0, app.settings.font));
			app.frame.revalidate();
			app.frame.repaint();
		});
		gameButton.setBounds(getWidth() / 2, getHeight() / 4, 200, 100);
		JLabel packLabel = new JLabel("ALL");
		packLabel.setFont(app.settings.font);
		JButton packButton = Utils.generate_button("pack_bouton", e -> {
			current_pack.v = (current_pack.v + 1) % (packs.size() + 1);
			packLabel.setText(current_pack.v == packs.size() ? "ALL" : packs.get(current_pack.v));
			classement.changePack(current_pack.v == packs.size() ? "null" : packs.get(current_pack.v));
			app.frame.repaint();
		});
		packButton.setBounds(2 * getWidth() / 3, getHeight() / 4, 200, 100);

		JButton settingsButton = Utils.generate_button("reglages", e -> {
			app.frame.setContentPane(new VueSettings(app));
			app.frame.revalidate();
			app.frame.repaint();
		});

		add(settingsButton);
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
