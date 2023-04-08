import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class VueSettings extends JPanel {
	App app;
	Image background;

	public VueSettings(App app) {
		this.app = app;

		try {
			background = ImageIO.read(new java.io.File("../textures/background.png"));
		} catch (java.io.IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JLabel retryLabel = new JLabel("Retry: " + KeyEvent.getKeyText(app.settings.retryKey));
		retryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		retryLabel.setFont(app.settings.font);
		JLabel menuLabel = new JLabel("Menu: " + KeyEvent.getKeyText(app.settings.menuKey));
		menuLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		menuLabel.setFont(app.settings.font);

		JLabel evidentLabel =
			new JLabel("Déplacement évident: " + KeyEvent.getKeyText(app.settings.evidentMoveKey));
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
