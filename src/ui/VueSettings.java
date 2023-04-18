import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.imageio.ImageIO;
import javax.swing.*;

public class VueSettings extends JPanel {
	App app;
	Image background;
	AtomicInteger selected = null;
	JButton retryButton, menuButton, evidentButton;

	public VueSettings(App app) {
		setFocusable(true);
		SwingUtilities.invokeLater(() -> {
			requestFocus();
		});
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ev) {
				if (selected != null) {
					System.out.println(ev.getKeyCode());
					selected.lazySet(ev.getKeyCode());
					selected = null;
					revalidate();
					repaint();
				} else if (ev.getKeyCode() == app.settings.menuKey.get()) {
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

		retryButton = Utils.generate_button_text("Retry: " + Utils.getKeyText(
								 app.settings.retryKey.get()), e -> {
			selected = app.settings.retryKey;
		}, app);
		menuButton = Utils.generate_button_text("Menu: " + Utils.getKeyText(app.settings.menuKey.get()), e -> {
			selected = app.settings.menuKey;
		}, app);

		evidentButton =
			Utils.generate_button_text("Déplacement évident: " +
			                           Utils.getKeyText(app.settings.evidentMoveKey.get()), e -> {
			selected = app.settings.evidentMoveKey;
		}, app);

		add(retryButton);
		add(menuButton);
		add(evidentButton);
	}

	public void paintComponent(Graphics g) {
		g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
		retryButton.setText("Retry: " + Utils.getKeyText(app.settings.retryKey.get()));
		menuButton.setText("Menu: " + Utils.getKeyText(app.settings.menuKey.get()));
		evidentButton.setText("Déplacement évident: " + Utils.getKeyText(app.settings.evidentMoveKey.get()));
	}
}
