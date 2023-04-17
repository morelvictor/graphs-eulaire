import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class VueSettings extends JPanel {
	App app;
	Image background;

	public VueSettings(App app) {
		setFocusable(true);
		SwingUtilities.invokeLater(() -> {
			requestFocus();
		});
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ev) {
				if (ev.getKeyCode() == app.settings.menuKey) {
					app.frame.setContentPane(new Menu(app));
					app.frame.revalidate();
					app.frame.repaint();
				}
			}
		});

		this.app = app;

		try {
			background = ImageIO.read(new java.io.File("../textures/background.png"));
		} catch (java.io.IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel retryLabel = new JLabel("Retry: " + Utils.getKeyText(app.settings.retryKey));
		retryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		retryLabel.setFont(app.settings.font);
		JLabel menuLabel = new JLabel("Menu: " + Utils.getKeyText(app.settings.menuKey));
		System.out.println(Utils.getKeyText(app.settings.menuKey));
		menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		menuLabel.setFont(app.settings.font);

		JLabel evidentLabel =
			new JLabel("Déplacement évident: " + Utils.getKeyText(app.settings.evidentMoveKey));
		evidentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		evidentLabel.setFont(app.settings.font);

		add(retryLabel);
		add(menuLabel);
		add(evidentLabel);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
	}
}
