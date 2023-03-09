import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public abstract class Utils {
	public static JButton generate_button(String texture, ActionListener listener) {
		JButton button = new JButton(new ImageIcon("../textures/" + texture + ".png"));
		button.setBorderPainted(false);
		button.setContentAreaFilled(false);
		button.setFocusPainted(false);
		button.addActionListener(listener);
		return button;
	}
}
